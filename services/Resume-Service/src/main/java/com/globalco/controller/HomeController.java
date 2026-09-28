package com.globalco.controller;


import com.globalco.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public ResponseEntity<ApiResponse> homeController() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to the Resume Service API!");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
