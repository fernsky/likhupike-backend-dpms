package np.sthaniya.dpis.common.config

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

/**
 * Registry for managing and validating API routes in the application.
 *
 * This component maintains a collection of registered routes and provides functionality to:
 * - Register new routes with their HTTP methods and access levels
 * - Validate incoming request paths and methods
 * - Check route accessibility (public/private)
 * - Pattern matching for URL paths with wildcard support
 */
@Component
class RouteRegistry {
    private val routes = mutableSetOf<Route>()

    /**
     * Registers a new route with the specified pattern, HTTP method, and accessibility.
     *
     * @param pattern The URL pattern to match, supporting wildcards (/* and /**)
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
     * Checks if the given path and method combination matches any registered route.
     *
     * @param path The request path to validate
     * @param method The HTTP method to validate
     * @return `true` if a matching route exists, `false` otherwise
     */
    fun isValidRoute(
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
     * Checks if any registered route pattern matches the given path.
     *
     * @param path The request path to check
     * @return `true` if any route pattern matches, regardless of method
     */
    fun hasMatchingPattern(path: String): Boolean =
        routes.any { route ->
            val regex =
                route.pattern
                    .replace("/**", "(/.*)?")
                    .replace("/*", "/[^/]*")
                    .toRegex()
            regex.matches(path)
        }

    /**
     * Retrieves all valid HTTP methods for a given path.
     *
     * @param path The request path to check
     * @return Set of [HttpMethod]s that are valid for the path
     */
    fun getValidMethodsForPath(path: String): Set<HttpMethod> =
        routes
            .filter { route ->
                val regex =
                    route.pattern
                        .replace("/**", "(/.*)?")
                        .replace("/*", "/[^/]*")
                        .toRegex()
                regex.matches(path)
            }.map { it.method }
            .toSet()

    /**
     * Data class representing a registered route with its matching logic.
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
            val regex =
                pattern
                    .replace("/**", "(/.*)?")
                    .replace("/*", "/[^/]*")
                    .toRegex()
            return this.method == method && regex.matches(path)
        }
    }
}
