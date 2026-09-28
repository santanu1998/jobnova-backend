package com.globalco.controllers;

import com.globalco.domain.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public String home() {
        return "Welcome to the User Services API! ________" + UserRole.ROLE_JOBSEEKER;
    }
}
