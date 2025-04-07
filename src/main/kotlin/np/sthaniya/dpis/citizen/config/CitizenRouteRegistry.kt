package np.sthaniya.dpis.citizen.config

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct

/**
 * Registry for managing and validating citizen API routes in the application.
 *
 * This component maintains a collection of registered citizen routes and provides functionality to:
 * - Register citizen routes with their HTTP methods and access levels
 * - Validate incoming citizen request paths and methods
 * - Check route accessibility (public/private)
 * - Pattern matching for URL paths with wildcard support
 * 
 * Similar to the main RouteRegistry but focused on citizen-specific endpoints.
 */
@Component
class CitizenRouteRegistry {
    /**
     * Data class representing a registered citizen route with its matching logic.
     *
     * @property pattern The URL pattern with optional wildcards
     * @property method The HTTP method for this route
     * @property isPublic Whether the route is publicly accessible
     */
    data class Route(
        val pattern: String,
        val method: HttpMethod,
        val isPublic: Boolean,
    ) {
        /**
         * Checks if this route matches the given path and method.
         *
         * @param path The request path to match
         * @param method The HTTP method to match
         * @return `true` if both path and method match, `false` otherwise
         */
        fun matches(
            path: String,
            method: HttpMethod,
        ): Boolean {
            val regex = pattern
                .replace("/**", "(/.*)?")
                .replace("/*", "/[^/]*")
                .toRegex()
            return this.method == method && regex.matches(path)
        }
        
        /**
         * Converts this Route to a Spring Security pattern matcher string.
         *
         * @return A pattern string for use with Spring Security
         */
        fun toSecurityPattern(): String = pattern
    }
    
    private val routes = mutableSetOf<Route>()
    
    /**
     * Registers a new citizen route with the specified pattern, HTTP method, and accessibility.
     *
     * @param pattern The URL pattern to match, supporting wildcards (/<all> and /<all>)
     * @param method The HTTP method for this route
     * @param isPublic Whether the route is publicly accessible without authentication
     */
    fun register(
        pattern: String,
        method: HttpMethod,
        isPublic: Boolean = false,
    ) {
        routes.add(Route(pattern, method, isPublic))
    }
    
    /**
     * Initializes the registry with default citizen routes.
     */
    @PostConstruct
    fun init() {
        // Authentication endpoints
        register("/api/v1/citizen-auth/login", HttpMethod.POST, true)
        register("/api/v1/citizen-auth/refresh", HttpMethod.POST, true)
        register("/api/v1/citizen-auth/password-reset/request", HttpMethod.POST, true)
        register("/api/v1/citizen-auth/password-reset/reset", HttpMethod.POST, true)
        
        // Profile endpoints that don't require authentication
        register("/api/v1/citizen-profile/register", HttpMethod.POST, true)
        
        // Authenticated citizen endpoints
        register("/api/v1/citizen-auth/logout", HttpMethod.POST, false)
        register("/api/v1/citizen-auth/change-password", HttpMethod.POST, false)
        
        // Profile management endpoints
        register("/api/v1/citizen-profile/me", HttpMethod.GET, false)
        register("/api/v1/citizen-profile/me", HttpMethod.PUT, false)
        register("/api/v1/citizen-profile/upload/photo", HttpMethod.POST, false)
        register("/api/v1/citizen-profile/upload/citizenship-front", HttpMethod.POST, false)
        register("/api/v1/citizen-profile/upload/citizenship-back", HttpMethod.POST, false)
        
        // Global options for CORS
        register("/**", HttpMethod.OPTIONS, true)
    }

    /**
     * Checks if the given path and method combination is a valid citizen route.
     *
     * @param path The request path to validate
     * @param method The HTTP method to validate
     * @return `true` if a matching citizen route exists, `false` otherwise
     */
    fun isCitizenRoute(
        path: String,
        method: HttpMethod,
    ): Boolean = routes.any { it.matches(path, method) }

    /**
     * Determines if the given path and method combination is publicly accessible.
     *
     * @param path The request path to check
     * @param method The HTTP method to check
     * @return `true` if the route is public, `false` otherwise
     */
    fun isPublicRoute(
        path: String,
        method: HttpMethod,
    ): Boolean = routes.find { it.matches(path, method) }?.isPublic ?: false
    
    /**
     * Gets all registered routes.
     *
     * @return A set of all routes in the registry
     */
    fun getAllRoutes(): Set<Route> = routes.toSet()
    
    /**
     * Gets all public routes.
     *
     * @return A set of all public routes in the registry
     */
    fun getPublicRoutes(): Set<Route> = routes.filter { it.isPublic }.toSet()
}
