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
        // CitizenManagementController endpoints

        // CitizenSearchController endpoints
        routeRegistry.register("/api/v1/admin/citizens/search", HttpMethod.GET)  // Advanced search - Changed from POST to GET

        // Basic CRUD operations
        routeRegistry.register("/api/v1/admin/citizens", HttpMethod.POST)                // Create citizen
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.GET)   // Get citizen by ID
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.PUT)   // Update citizen
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+", HttpMethod.DELETE) // Delete citizen
        
        // Citizen approval
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/approve", HttpMethod.POST) // Approve citizen
        
        // Document upload endpoints
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/photo", HttpMethod.POST)  // Upload photo
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/citizenship/front", HttpMethod.POST) // Upload citizenship front
        routeRegistry.register("/api/v1/admin/citizens/[a-zA-Z0-9-]+/citizenship/back", HttpMethod.POST)  // Upload citizenship back
        
        // CitizenStateController endpoints
        // State management
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/[a-zA-Z0-9-]+/state", HttpMethod.PUT)  // Update citizen state
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/[a-zA-Z0-9-]+/documents/[a-zA-Z0-9_-]+/state", HttpMethod.PUT)  // Update document state
        
        // Citizen filtering by state
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/requiring-action", HttpMethod.GET)  // Get citizens requiring action
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/by-state/[a-zA-Z0-9_-]+", HttpMethod.GET)  // Get citizens by state
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/by-document-state/[a-zA-Z0-9_-]+", HttpMethod.GET)  // Get citizens by document state
        routeRegistry.register("/api/v1/admin/citizen-state/citizens/by-document-note", HttpMethod.GET)  // Get citizens by document note
    }
}