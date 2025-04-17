package np.sthaniya.dpis.profile.institutions.cooperatives.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers cooperative management endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/cooperatives
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class CooperativeRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Main cooperative endpoints
        routeRegistry.register("/api/v1/cooperatives", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives", HttpMethod.POST)
        routeRegistry.register("/api/v1/cooperatives/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/cooperatives/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/status", HttpMethod.PATCH)
        routeRegistry.register("/api/v1/cooperatives/code/[^/]+", HttpMethod.GET)

        // Cooperative search endpoints
        routeRegistry.register("/api/v1/cooperatives/search/by-name", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/by-type/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/by-status/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/by-ward/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/near", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/statistics/by-type", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/search/statistics/by-ward", HttpMethod.GET)

        // Cooperative translation endpoints
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations", HttpMethod.POST)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations/[^/]+/status", HttpMethod.PATCH)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/translations/locale/[^/]+", HttpMethod.GET)

        // Cooperative media endpoints
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media", HttpMethod.POST)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/[^/]+/set-primary", HttpMethod.POST)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/[^/]+/visibility", HttpMethod.PATCH)
        routeRegistry.register("/api/v1/cooperatives/[^/]+/media/by-type/[^/]+", HttpMethod.GET)

        // Cooperative type translation endpoints
        routeRegistry.register("/api/v1/cooperative-types/translations", HttpMethod.POST)
        routeRegistry.register("/api/v1/cooperative-types/translations/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperative-types/translations/type/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperative-types/translations/type/[^/]+/locale/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperative-types/translations/type/[^/]+/locale/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/cooperative-types/translations/locale/[^/]+", HttpMethod.GET)
        routeRegistry.register("/api/v1/cooperative-types/translations/all-types/locale/[^/]+", HttpMethod.GET)
    }
}
