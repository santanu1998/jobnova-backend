package com.globalco.controllers;

import com.globalco.domain.UserRole;
import com.globalco.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse home() {
        return new ApiResponse("Service for managing job postings, searching, and filtering  " + UserRole.ROLE_EMPLOYER, true);
    }
}
