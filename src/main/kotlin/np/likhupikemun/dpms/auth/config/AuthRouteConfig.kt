package np.likhupikemun.dpms.auth.config

import np.likhupikemun.dpms.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

@Configuration
class AuthRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Public routes
        routeRegistry.register("/api/v1/auth/login", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/register", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/refresh", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/password-reset/request", HttpMethod.POST, isPublic = true)
        routeRegistry.register("/api/v1/auth/password-reset/reset", HttpMethod.POST, isPublic = true)

        // Protected routes
        routeRegistry.register("/api/v1/auth/logout", HttpMethod.POST)
    }
}
