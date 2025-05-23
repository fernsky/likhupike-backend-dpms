package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward-wise house head gender endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/ward-wise-househead-gender
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class WardWiseHouseHeadGenderRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise house head gender endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/by-ward/[^/]+", HttpMethod.GET, true)

        // Gender filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/by-gender/[^/]+", HttpMethod.GET, true)

        // Summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-ward", HttpMethod.GET, true)
    }
}
