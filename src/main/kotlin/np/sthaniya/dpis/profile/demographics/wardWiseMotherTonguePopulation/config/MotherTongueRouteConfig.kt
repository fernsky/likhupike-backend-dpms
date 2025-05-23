package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers mother tongue demographics endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/demographics
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class MotherTongueRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise mother tongue population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-ward/[^/]+", HttpMethod.GET, true)

        // Language filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-language/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-language", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-ward", HttpMethod.GET, true)
    }
}
