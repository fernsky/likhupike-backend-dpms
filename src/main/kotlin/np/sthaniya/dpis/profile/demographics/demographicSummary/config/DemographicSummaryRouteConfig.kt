package np.sthaniya.dpis.profile.demographics.demographicSummary.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers demographic summary endpoints to RouteRegistry for security filtering.
 * GET endpoints are publicly accessible, while update endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/profile/demographics/summary
 */
@Configuration
class DemographicSummaryRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Get endpoint is public
        routeRegistry.register("/api/v1/profile/demographics/summary", HttpMethod.GET, true)

        // Update endpoints require authentication
        routeRegistry.register("/api/v1/profile/demographics/summary", HttpMethod.PUT)

        // Update single field endpoint
        routeRegistry.register("/api/v1/profile/demographics/summary/field", HttpMethod.PUT)
    }
}
