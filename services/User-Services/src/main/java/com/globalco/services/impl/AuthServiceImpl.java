package com.globalco.services.impl;

import com.globalco.domain.UserRole;
import com.globalco.domain.UserStatus;
import com.globalco.exceptions.EmailAlreadyExistsException;
import com.globalco.exceptions.InvalidOperationException;
import com.globalco.mapper.UserMapper;
import com.globalco.models.User;
import com.globalco.payload.AuthResponse;
import com.globalco.payload.LoginRequest;
import com.globalco.payload.SignupRequest;
import com.globalco.repositories.UserRepository;
import com.globalco.security.CustomUserDetailsService;
import com.globalco.security.JwtProvider;
import com.globalco.services.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (signupRequest.getRole() == UserRole.ROLE_ADMIN) {
            throw new InvalidOperationException("Cannot create an admin user");
        }
        User user = User.builder()
                .fullName(signupRequest.getFullName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword())) // In a real application, make sure to hash the password
                .role(signupRequest.getRole())
                .phoneNumber(signupRequest.getPhoneNumber())
                .lastLogin(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .build();
        User savedUser = userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("User registered successfully");
        authResponse.setJwt(jwt);
        authResponse.setUserResponse(UserMapper.toDTO(savedUser));
        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        String jwt = jwtProvider.generateToken(authentication, user.getId());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Welcome Back, " + user.getFullName());
        authResponse.setMessage("User logged in successfully");
        authResponse.setJwt(jwt);
        authResponse.setUserResponse(UserMapper.toDTO(user));
        return authResponse;
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
