package com.globalco.client;

import com.globalco.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICES")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(
            @PathVariable Long userId);
}