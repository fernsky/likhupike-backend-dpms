package np.sthaniya.dpis.config

import np.sthaniya.dpis.auth.resolver.CurrentUserIdResolver
import np.sthaniya.dpis.citizen.resolver.CurrentCitizenIdResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configuration class for Spring MVC customizations.
 *
 * This configuration provides custom argument resolvers for:
 * - Current user ID resolution in controller methods
 * - Current citizen ID resolution in controller methods
 * - Integration with Spring MVC's method parameter resolution
 */
@Configuration
class WebMvcConfig(
        private val currentUserIdResolver: CurrentUserIdResolver,
        private val currentCitizenIdResolver: CurrentCitizenIdResolver
) : WebMvcConfigurer {

    /**
     * Adds custom argument resolvers to Spring MVC's parameter resolution chain.
     *
     * Registers:
     * - [CurrentUserIdResolver] for resolving current user IDs from security context
     * - [CurrentCitizenIdResolver] for resolving current citizen IDs from JWT tokens
     *
     * @param resolvers The list of argument resolvers to extend
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentUserIdResolver)
        resolvers.add(currentCitizenIdResolver)
    }
}
