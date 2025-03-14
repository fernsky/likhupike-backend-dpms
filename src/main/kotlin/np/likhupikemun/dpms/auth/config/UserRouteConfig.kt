package np.likhupikemun.dpms.auth.config

import np.likhupikemun.dpms.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

@Configuration
class UserRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // All user routes are protected
        routeRegistry.register("/api/v1/users", HttpMethod.POST)
        routeRegistry.register("/api/v1/users/search", HttpMethod.GET)
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.GET)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.PUT)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+", HttpMethod.DELETE) // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/approve", HttpMethod.POST)       // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/permissions", HttpMethod.PUT)    // Changed {id} to [^/]+
        routeRegistry.register("/api/v1/users/[^/]+/reset-password", HttpMethod.POST) // Changed {id} to [^/]+
    }
}
