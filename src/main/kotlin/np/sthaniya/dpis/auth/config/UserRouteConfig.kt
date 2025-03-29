package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Configuration class for user management route registration.
 *
 * This class is responsible for registering all user management endpoints with the [RouteRegistry].
 * All routes in this configuration require authentication and appropriate permissions through
 * [UserController].
 *
 * Route Categories:
 * 1. User Creation and Search:
 *    - Create User (/api/v1/users) [POST]
 *    - Search Users (/api/v1/users/search) [GET]
 *
 * 2. User Management Operations:
 *    - Get User Details (/api/v1/users/{id}) [GET]
 *    - Update User (/api/v1/users/{id}) [PUT]
 *    - Delete User (/api/v1/users/{id}) [DELETE]
 *    - Approve User (/api/v1/users/{id}/approve) [POST]
 *    - Update Permissions (/api/v1/users/{id}/permissions) [PUT]
 *    - Reset Password (/api/v1/users/{id}/reset-password) [POST]
 *
 * Note: All routes use [^/]+ pattern for dynamic segments to support UUID path parameters
 *
 * @property routeRegistry The central registry for all application routes
 */
@Configuration
class UserRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // All user routes are protected
        routeRegistry.register("/api/v1/users", HttpMethod.POST)
        routeRegistry.register("/api/v1/users/search", HttpMethod.GET)
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.GET)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.PUT)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.DELETE) // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/approve", HttpMethod.POST)       // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/permissions", HttpMethod.PUT)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/reset-password", HttpMethod.POST) // Changed {id} to [^/]+
    }
}
