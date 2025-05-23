package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers ward-wise demographic summary endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication except for GET requests which are publicly available.
 */
@Configuration
class WardWiseDemographicSummaryRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise demographic summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.DELETE)

        // Ward filter endpoint
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/by-ward/[^/]+", HttpMethod.GET, true)
    }
}
