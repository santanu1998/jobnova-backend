package com.globalco.config;

import com.globalco.jwt.JwtConstant;
import com.globalco.jwt.JwtUtil;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouteConfig {

    private final JwtUtil jwtUtil;

    public RouteConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ==================== Public Routes (no JWT) ====================

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return GatewayRouterFunctions.route("auth-routes")
                .route(RequestPredicates.path("/auth/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("User-Services"))
                .build();
    }

    // ==================== Admin-only Routes (JWT + ROLE_ADMIN) ====================
    // No admin endpoints exist yet on User-Services — this route is wired up so it's
    // ready as soon as one gets added under /api/admin/**.

    @Bean
    public RouterFunction<ServerResponse> adminRoutes() {
        return GatewayRouterFunctions.route("admin-routes")
                .route(RequestPredicates.path("/api/admin/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("User-Services"))
                .before(this::jwtAuthFilter)
                .before(request -> requireRole(request, "ROLE_ADMIN"))
                .build();
    }

    private ServerRequest requireRole(ServerRequest request, String roleAdmin) {
        String roles = request.headers().firstHeader("X-User-Role");
        if (roles == null || !roles.contains(roleAdmin)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for role " + roleAdmin);
        }
        return request;
    }

    // ==================== Protected Routes (JWT required) ====================

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes() {
        return GatewayRouterFunctions.route("user-service-routes")
                .route(RequestPredicates.path("/api/users/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("User-Services"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> companyServiceRoutes() {
        return GatewayRouterFunctions.route("company-service-routes")
                .route(RequestPredicates.path("/api/companies/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("Company-Services"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> jobServiceRoutes() {
        return GatewayRouterFunctions.route("job-service-routes")
                .route(RequestPredicates.path("/api/jobs/**")
                                .or(RequestPredicates.path("/api/job-categories/**"))
                                .or(RequestPredicates.path("/api/job-skills/**"))
                                .or(RequestPredicates.path("/api/job-tags/**")),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("Job-Service"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> applicationServiceRoutes() {
        return GatewayRouterFunctions.route("application-service-routes")
                .route(RequestPredicates.path("/api/applications/**")
                                .or(RequestPredicates.path("/api/application-notes/**")),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("Application-Service"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> resumeServiceRoutes() {
        return GatewayRouterFunctions.route("resume-service-routes")
                .route(RequestPredicates.path("/api/resumes/**")
                                .or(RequestPredicates.path("/api/resume-skills/**"))
                                .or(RequestPredicates.path("/api/work-experiences/**")),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("Resume-Service"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> preferenceServiceRoutes() {
        return GatewayRouterFunctions.route("preference-service-routes")
                .route(RequestPredicates.path("/api/saved-jobs/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("Job-Preferences"))
                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> aiServiceRoutes() {
        return GatewayRouterFunctions.route("ai-service-routes")
                .route(RequestPredicates.path("/api/ai/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("AI-Service"))
                .before(this::jwtAuthFilter)
                .build();
    }

    // ==================== JWT filter ====================

    private ServerRequest jwtAuthFilter(ServerRequest request) {
        String authHeader = request.headers().firstHeader(JwtConstant.JWT_HEADER);

        if (authHeader == null || !authHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "missing or invalid authorization header");
        }

        String token = authHeader.substring(JwtConstant.TOKEN_PREFIX.length());

        if (!jwtUtil.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "invalid or expired JWT token");
        }

        String email = jwtUtil.extractEmail(token);
        String authorities = jwtUtil.extractAuthorities(token);
        Long userId = jwtUtil.extractUserId(token);

        return ServerRequest.from(request)
                .header("X-User-Id", String.valueOf(userId))
                .header("X-User-Email", email)
                .header("X-User-Role", authorities)
                .build();
    }
}