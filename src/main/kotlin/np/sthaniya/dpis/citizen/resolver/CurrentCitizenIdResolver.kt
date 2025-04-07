package np.sthaniya.dpis.citizen.resolver

import io.jsonwebtoken.Claims
import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.security.CitizenJwtService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

/**
 * Annotation to mark a parameter in controller methods that should be resolved
 * to the ID of the currently authenticated citizen from the JWT token.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentCitizenId

/**
 * Resolver that injects the citizen ID from the JWT token into controller methods.
 *
 * This resolver uses the [CurrentCitizenId] annotation to identify parameters that should
 * be populated with the ID of the authenticated citizen.
 */
@Component
class CurrentCitizenIdResolver(
    private val citizenJwtService: CitizenJwtService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentCitizenId::class.java) &&
                parameter.parameterType == UUID::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authHeader = webRequest.getHeader("Authorization") 
            ?: throw CitizenAuthException.UnauthenticatedException()
        
        if (!authHeader.startsWith("Bearer ")) {
            throw CitizenAuthException.UnauthenticatedException()
        }
        
        val token = authHeader.substring(7)
        if (!citizenJwtService.validateToken(token)) {
            throw CitizenAuthException.InvalidTokenException()
        }
        
        try {
            // Extract claims to get citizenId
            val claims = citizenJwtService.extractAllClaims(token)
            val citizenId = claims?.get("citizenId", String::class.java)
                ?: throw CitizenAuthException.JwtAuthenticationException("Token does not contain citizenId")
            
            return UUID.fromString(citizenId)
        } catch (e: Exception) {
            throw CitizenAuthException.JwtAuthenticationException(
                "Failed to extract citizen ID from token", 
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }
}
