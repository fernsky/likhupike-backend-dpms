package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward age-wise marital status endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/ward-age-wise-marital-status
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class WardAgeWiseMaritalStatusRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward age-wise marital status endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-ward/[^/]+", HttpMethod.GET, true)

        // Age group filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-age-group/[^/]+", HttpMethod.GET, true)

        // Marital status filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-marital-status/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-marital-status", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-age-group", HttpMethod.GET, true)
    }
}
