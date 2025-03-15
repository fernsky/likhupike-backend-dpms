package np.likhupikemun.dpms.config

import np.likhupikemun.dpms.common.config.RouteRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod

@Configuration
class TestRouteConfig {
    
    @Bean
    @Primary
    fun routeRegistry(): RouteRegistry {
        return RouteRegistry().apply {
            // Auth endpoints
            register("/api/v1/auth/register", HttpMethod.POST, true)
            register("/api/v1/auth/login", HttpMethod.POST, true)
            register("/api/v1/auth/refresh", HttpMethod.POST, true)
            register("/api/v1/auth/logout", HttpMethod.POST, false)
            register("/api/v1/auth/password-reset/request", HttpMethod.POST, true)
            register("/api/v1/auth/password-reset/reset", HttpMethod.POST, true)
            
            // User endpoints
            register("/api/v1/users", HttpMethod.POST, false)
            register("/api/v1/users/search", HttpMethod.GET, false)
            register("/api/v1/users/[^/]+", HttpMethod.GET, false)    // Changed {id} to [^/]+
            register("/api/v1/users/[^/]+", HttpMethod.DELETE, false) // Changed {id} to [^/]+
            register("/api/v1/users/[^/]+/approve", HttpMethod.POST, false)       // Changed {id} to [^/]+
            register("/api/v1/users/[^/]+/permissions", HttpMethod.PUT, false)    // Changed {id} to [^/]+
            register("/api/v1/users/[^/]+/reset-password", HttpMethod.POST, false) // Changed {id} to [^/]+
        }
    }
}
