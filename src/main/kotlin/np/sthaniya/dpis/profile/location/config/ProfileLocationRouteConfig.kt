package np.sthaniya.dpis.profile.location.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers profile location endpoints to RouteRegistry for security filtering. GET endpoints are
 * publicly accessible. POST/PUT/DELETE endpoints require authentication and appropriate permission.
 *
 * Base paths:
 * - /api/v1/profile/location/municipality
 * - /api/v1/profile/location/wards
 * - /api/v1/profile/location/settlements
 */
@Configuration
class ProfileLocationRouteConfig(
        routeRegistry: RouteRegistry,
) {
    init {
        // Municipality endpoints
        routeRegistry.register(
                "/api/v1/profile/location/municipality",
                HttpMethod.GET,
                isPublic = true
        ) // Get municipality info
        routeRegistry.register(
                "/api/v1/profile/location/municipality",
                HttpMethod.POST
        ) // Create/get municipality
        routeRegistry.register(
                "/api/v1/profile/location/municipality/info",
                HttpMethod.PUT
        ) // Update municipality info
        routeRegistry.register(
                "/api/v1/profile/location/municipality/geo-location",
                HttpMethod.PUT
        ) // Update geo location

        // Ward endpoints
        routeRegistry.register(
                "/api/v1/profile/location/wards",
                HttpMethod.GET,
                isPublic = true
        ) // Get all wards
        routeRegistry.register("/api/v1/profile/location/wards", HttpMethod.POST) // Create ward
        routeRegistry.register(
                "/api/v1/profile/location/wards/[a-zA-Z0-9-]+",
                HttpMethod.GET,
                isPublic = true
        ) // Get ward by ID
        routeRegistry.register(
                "/api/v1/profile/location/wards/[a-zA-Z0-9-]+",
                HttpMethod.PUT
        ) // Update ward
        routeRegistry.register(
                "/api/v1/profile/location/wards/[a-zA-Z0-9-]+",
                HttpMethod.DELETE
        ) // Delete ward
        routeRegistry.register(
                "/api/v1/profile/location/wards/number/[0-9]+",
                HttpMethod.GET,
                isPublic = true
        ) // Get ward by number

        // Settlement endpoints
        routeRegistry.register(
                "/api/v1/profile/location/settlements",
                HttpMethod.GET,
                isPublic = true
        ) // Get all settlements
        routeRegistry.register(
                "/api/v1/profile/location/settlements",
                HttpMethod.POST
        ) // Create settlement
        routeRegistry.register(
                "/api/v1/profile/location/settlements/[a-zA-Z0-9-]+",
                HttpMethod.GET,
                isPublic = true
        ) // Get settlement by ID
        routeRegistry.register(
                "/api/v1/profile/location/settlements/[a-zA-Z0-9-]+",
                HttpMethod.PUT
        ) // Update settlement
        routeRegistry.register(
                "/api/v1/profile/location/settlements/[a-zA-Z0-9-]+",
                HttpMethod.DELETE
        ) // Delete settlement
        routeRegistry.register(
                "/api/v1/profile/location/settlements/ward/[a-zA-Z0-9-]+",
                HttpMethod.GET,
                isPublic = true
        ) // Get settlements by ward
        routeRegistry.register(
                "/api/v1/profile/location/settlements/search",
                HttpMethod.GET,
                isPublic = true
        ) // Search settlements by name
    }
}
