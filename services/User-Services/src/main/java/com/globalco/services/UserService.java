package com.globalco.services;

import com.globalco.dto.response.UserResponse;
import com.globalco.models.User;
import com.globalco.payload.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUsers();
    UserResponse updateProfile(String email, UpdateUserRequest request);

    // For admin use only
    UserResponse suspendUser(Long id);
    UserResponse activateUser(Long id);
    UserResponse deleteUser(Long id);
}
