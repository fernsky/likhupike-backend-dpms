package np.sthaniya.dpis.auth.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import np.sthaniya.dpis.auth.domain.entity.User
import java.util.UUID

/**
 * Annotation to mark a parameter in controller methods that should be resolved
 * to the ID of the currently authenticated user from the security context.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentUserId

/**
 * Resolver for injecting the current user's ID into controller methods.
 *
 * This resolver implements Spring's [HandlerMethodArgumentResolver] to provide
 * automatic resolution of the currently authenticated user's ID when the
 * [CurrentUserId] annotation is used on controller method parameters.
 */
@Component
class CurrentUserIdResolver : HandlerMethodArgumentResolver {
    
    /**
     * Determines if this resolver supports the given method parameter.
     *
     * @param parameter The method parameter to check
     * @return true if this resolver can handle the parameter
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUserId::class.java) &&
                parameter.parameterType == UUID::class.java
    }

    /**
     * Resolves the current user's ID from the security context.
     *
     * @param parameter The method parameter to resolve
     * @param mavContainer The model and view container
     * @param webRequest The current web request
     * @param binderFactory The data binder factory
     * @return UUID of the current user
     * @throws IllegalStateException if user is not authenticated or ID cannot be extracted
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UUID {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authentication found in security context")
            
        // Try to get ID from either a User principal or the name field
        val userId = when (val principal = authentication.principal) {
            is User -> principal.id
            else -> {
                val name = authentication.name
                    ?: throw IllegalStateException("No principal ID found in authentication")
                try {
                    UUID.fromString(name)
                } catch (e: IllegalArgumentException) {
                    throw IllegalStateException("Could not convert authentication name to UUID", e)
                }
            }
        }
        
        return userId ?: throw IllegalStateException("User ID is null")
    }
}
