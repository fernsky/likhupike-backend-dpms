package np.sthaniya.dpis.config

import np.sthaniya.dpis.common.config.RouteRegistry
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
            // In test environment, consider all routes as public
            register("/**", HttpMethod.GET, true)
            register("/**", HttpMethod.POST, true)
            register("/**", HttpMethod.PUT, true)
            register("/**", HttpMethod.DELETE, true)
            register("/**", HttpMethod.PATCH, true)
        }
    }
}
