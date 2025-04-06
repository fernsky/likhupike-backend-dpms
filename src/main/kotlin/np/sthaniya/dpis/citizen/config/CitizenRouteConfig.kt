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
        // Administrative citizen management routes - Basic CRUD operations
        routeRegistry.register("/api/v1/admin/citizens", HttpMethod.POST)               // Create citizen
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.GET)  // Get citizen by ID
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.PUT)  // Update citizen
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.DELETE) // Delete citizen
        
        // Citizen approval
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/approve", HttpMethod.POST) // Approve citizen
        
        // Document upload endpoints
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/photo", HttpMethod.POST)  // Upload photo
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/citizenship/front", HttpMethod.POST) // Upload citizenship front
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/citizenship/back", HttpMethod.POST)  // Upload citizenship back
    }
}