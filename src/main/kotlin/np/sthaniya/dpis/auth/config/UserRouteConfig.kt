package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers user management endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 * 
 * Endpoint patterns:
 * - Base path: /api/v1/users
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 * 
 * Implementation note: Using regex instead of {id} for Spring security pattern matching
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
