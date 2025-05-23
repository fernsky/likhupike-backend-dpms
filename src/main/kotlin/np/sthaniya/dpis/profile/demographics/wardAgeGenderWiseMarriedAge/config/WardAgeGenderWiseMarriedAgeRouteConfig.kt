package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward-age-gender-wise married age endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/ward-age-gender-wise-married-age
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class WardAgeGenderWiseMarriedAgeRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-age-gender-wise married age endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.DELETE)

        // Filters
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-age-group/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-gender/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-age-group-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-ward", HttpMethod.GET, true)
    }
}
