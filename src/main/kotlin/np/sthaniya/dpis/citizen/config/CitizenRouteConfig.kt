package np.sthaniya.dpis.citizen.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers citizen management endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 * 
 * Base path: /api/v1/admin/citizens
 * 
 * Implementation note: Using regex instead of {id} for Spring security pattern matching
 */
@Configuration
class CitizenRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Administrative citizen management routes
        routeRegistry.register("/api/v1/admin/citizens", HttpMethod.POST)
    }
}