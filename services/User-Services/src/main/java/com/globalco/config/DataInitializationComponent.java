package com.globalco.config;

import com.globalco.domain.UserRole;
import com.globalco.domain.UserStatus;
import com.globalco.models.User;
import com.globalco.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser(){
        String adminEmail = "santanu.singha021@gmail.com";

        if(!userRepository.existsByEmail(adminEmail)){
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setFullName("Santanu Singha");
            admin.setPassword(passwordEncoder.encode("Santanu@2027"));
            admin.setRole(UserRole.ROLE_ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
        }
    }
}