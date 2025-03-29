package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Configuration class for authentication-related route registration.
 *
 * This class is responsible for registering all authentication-related endpoints with the [RouteRegistry].
 * It defines which routes are publicly accessible and which require authentication.
 *
 * Route Categories:
 * 1. Public Routes:
 *    - Login (/api/v1/auth/login)
 *    - Registration (/api/v1/auth/register)
 *    - Token Refresh (/api/v1/auth/refresh)
 *    - Password Reset Request (/api/v1/auth/password-reset/request)
 *    - Password Reset Execution (/api/v1/auth/password-reset/reset)
 *
 * 2. Protected Routes:
 *    - Logout (/api/v1/auth/logout)
 *
 * These routes correspond to the endpoints defined in [AuthController].
 *
 * @property routeRegistry The central registry for all application routes
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
