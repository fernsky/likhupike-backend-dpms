package np.sthaniya.dpis.ui.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers UI endpoints in RouteRegistry for security filtering.
 * 
 * Implementation details:
 * - Public endpoints skip JWT validation
 * - All Thymeleaf templates are served through these routes
 * - Base path for public showcase is the root path "/"
 */
@Configuration
class UiRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Root path for showcase homepage
        routeRegistry.register("/", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/access-denied", HttpMethod.GET, isPublic = true)

        // Static resources
        routeRegistry.register("/webjars/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/css/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/js/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/images/**", HttpMethod.GET, isPublic = true)
        
        // Protected dashboard routes (if needed in the future)
        routeRegistry.register("/dashboard", HttpMethod.GET)
    }
}
