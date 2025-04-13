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
 * - Base path for UI starts with /ui/
 */
@Configuration
class UiRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Register public UI routes with multiple HTTP methods
        // Login routes
        routeRegistry.register("/ui/login", 
            listOf(HttpMethod.GET, HttpMethod.POST), 
            isPublic = true)
            
        // Register routes - Both GET and POST
        routeRegistry.register("/ui/register", 
            listOf(HttpMethod.GET, HttpMethod.POST), 
            isPublic = true)
            
        // Password reset routes
        routeRegistry.register("/ui/password-reset", 
            listOf(HttpMethod.GET, HttpMethod.POST), 
            isPublic = true)
        routeRegistry.register("/ui/password-reset/confirm", 
            listOf(HttpMethod.GET, HttpMethod.POST), 
            isPublic = true)
            
        // Logout route
        routeRegistry.register("/ui/logout", HttpMethod.POST, isPublic = true)
        
        // Home routes
        routeRegistry.register("/ui", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/ui/", HttpMethod.GET, isPublic = true)

        // Static resources
        routeRegistry.register("/webjars/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/css/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/js/**", HttpMethod.GET, isPublic = true)
        routeRegistry.register("/images/**", HttpMethod.GET, isPublic = true)
        
        // Protected UI routes
        routeRegistry.register("/ui/dashboard", HttpMethod.GET)
        routeRegistry.register("/ui/profile", HttpMethod.GET)
        routeRegistry.register("/ui/settings", HttpMethod.GET)
        routeRegistry.register("/ui/users/**", HttpMethod.GET)
    }
}
