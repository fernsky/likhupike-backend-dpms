package np.sthaniya.dpis.auth.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import np.sthaniya.dpis.auth.domain.entity.User
import java.util.UUID
/**
 * Resolver for injecting the current user's ID into controller methods.
 *
 * This resolver implements Spring's [HandlerMethodArgumentResolver] to provide
 * automatic resolution of the currently authenticated user's ID when the
 * [CurrentUserId] annotation is used on controller method parameters.
 *
 * Implementation Details:
 * - Extracts User from Spring Security context
 * - Safely handles authentication state
 * - Provides null-safety
 *
 * Integration Points:
 * - Works with Spring Security
 * - Integrates with Spring MVC
 * - Uses custom User entity
 *
 * Usage Example:
 * ```kotlin
 * // In WebMvcConfigurer
 * override fun addArgumentResolvers(resolvers: List<HandlerMethodArgumentResolver>) {
 *     resolvers.add(CurrentUserIdResolver())
 * }
 *
 * // In Controller
 * @GetMapping("/profile")
 * fun getProfile(@CurrentUserId userId: UUID)
 * ```
 */
class CurrentUserIdResolver : HandlerMethodArgumentResolver {
    
    /**
     * Determines if this resolver supports the given method parameter.
     *
     * Checks if:
     * 1. Parameter is annotated with @CurrentUserId
     * 2. Parameter type is compatible with UUID
     *
     * @param parameter The method parameter to check
     * @return true if this resolver can handle the parameter
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUserId::class.java)
    }

    /**
     * Resolves the current user's ID from the security context.
     *
     * Resolution Process:
     * 1. Gets authentication from security context
     * 2. Extracts principal (User entity)
     * 3. Returns user's ID or null
     *
     * Security Handling:
     * - Returns null for unauthenticated requests
     * - Safely handles missing principal
     * - Type-safe casting of user entity
     *
     * @param parameter The method parameter to resolve
     * @param mavContainer The model and view container
     * @param webRequest The current web request
     * @param binderFactory The data binder factory
     * @return UUID of the current user or null if not authenticated
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UUID? {
        val authentication = SecurityContextHolder.getContext().authentication
        return (authentication?.principal as? User)?.id
    }
}
