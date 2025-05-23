package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward-wise caste population endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/ward-wise-caste-population
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class WardWiseCastePopulationRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise caste population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/by-ward/[^/]+", HttpMethod.GET, true)

        // Caste filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/by-caste/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-caste", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-ward", HttpMethod.GET, true)
    }
}
