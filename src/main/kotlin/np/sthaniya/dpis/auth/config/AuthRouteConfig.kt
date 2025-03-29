package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers auth endpoints in RouteRegistry for security filtering.
 * 
 * Implementation details:
 * - Public endpoints skip JWT validation
 * - Protected endpoints require valid JWT
 * - All endpoints use POST to prevent CSRF
 * - Refresh token requires expired but valid JWT
 * - Password reset uses two-step verification
 * 
 * Base path: /api/v1/auth/*
 */
@Configuration
class AuthRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Public routes
        routeRegistry.register("/api/v1/auth/login", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/register", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/refresh", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/password-reset/request", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/password-reset/reset", HttpMethod.POST, isPublic = true)

        // Protected routes
        routeRegistry.register("/api/v1/auth/logout", HttpMethod.POST)
    }
}
