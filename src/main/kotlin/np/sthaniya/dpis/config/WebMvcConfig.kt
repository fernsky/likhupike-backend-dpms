package np.sthaniya.dpis.config

import np.sthaniya.dpis.auth.resolver.CurrentUserIdResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configuration class for Spring MVC customizations.
 *
 * This configuration provides custom argument resolvers for:
 * - Current user ID resolution in controller methods
 * - Integration with Spring MVC's method parameter resolution
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    /**
     * Adds custom argument resolvers to Spring MVC's parameter resolution chain.
     *
     * Registers:
     * - [CurrentUserIdResolver] for resolving current user IDs from security context
     *
     * @param resolvers The list of argument resolvers to extend
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserIdResolver())
    }
}
