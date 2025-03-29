package np.sthaniya.dpis.common.config

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class RouteRegistry {
    private val routes = mutableSetOf<Route>()

    fun register(
        pattern: String,
        method: HttpMethod,
        isPublic: Boolean = false,
    ) {
        routes.add(Route(pattern, method, isPublic))
    }

    fun isValidRoute(
        path: String,
        method: HttpMethod,
    ): Boolean = routes.any { it.matches(path, method) }

    fun isPublicRoute(
        path: String,
        method: HttpMethod,
    ): Boolean = routes.find { it.matches(path, method) }?.isPublic ?: false

    fun hasMatchingPattern(path: String): Boolean =
        routes.any { route ->
            val regex =
                route.pattern
                    .replace("/**", "(/.*)?")
                    .replace("/*", "/[^/]*")
                    .toRegex()
            regex.matches(path)
        }

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

    data class Route(
        val pattern: String,
        val method: HttpMethod,
        val isPublic: Boolean,
    ) {
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
