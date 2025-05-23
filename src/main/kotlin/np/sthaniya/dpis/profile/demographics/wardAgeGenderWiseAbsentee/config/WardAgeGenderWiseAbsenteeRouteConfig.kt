package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward-age-gender-wise absentee endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/ward-age-gender-wise-absentee
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class WardAgeGenderWiseAbsenteeRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-age-gender-wise absentee endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-ward/[^/]+", HttpMethod.GET, true)

        // Age group filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-age-group/[^/]+", HttpMethod.GET, true)

        // Gender filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-gender/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-age-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-ward", HttpMethod.GET, true)
    }
}
