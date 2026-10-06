package com.globalco.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Comma-separated origin patterns allowed to call the gateway.
    // Defaults cover the Vite development server and the JobNova Vercel production site.
    // Override in deployment with CORS_ALLOWED_ORIGINS when using a custom frontend domain.
    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:*,http://127.0.0.1:*,https://jobnova-frontend-eight.vercel.app}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
