package np.sthaniya.dpis.profile.demographics.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers demographics endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/demographics
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class DemographicsRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise religion population endpoints
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/ward/[^/]+", HttpMethod.GET, true)

        // Religion filter endpoint
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/religion/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/summary/by-religion", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/demographics/ward-wise-religion-population/summary/by-ward", HttpMethod.GET, true)
    }
}
