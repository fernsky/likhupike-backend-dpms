package np.sthaniya.dpis.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.common.config.RateLimitConfig
import np.sthaniya.dpis.common.service.RateLimitService
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(1)
class RateLimitFilter(
    private val rateLimitService: RateLimitService,
    private val rateLimitConfig: RateLimitConfig
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!rateLimitConfig.enabled) {
            filterChain.doFilter(request, response)
            return
        }

        // Get client identifier (IP address or session ID)
        val clientId = getClientId(request)
        
        // Get endpoint identifier
        val endpoint = getEndpointIdentifier(request)
        
        // Check if rate limit is exceeded
        if (rateLimitService.tryConsume(clientId, endpoint)) {
            filterChain.doFilter(request, response)
        } else {
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.contentType = "application/json"
            response.writer.write("{\"error\": \"Rate limit exceeded. Please try again later.\"}")
        }
    }
    
    private fun getClientId(request: HttpServletRequest): String {
        // You can use X-Forwarded-For header if you're behind a proxy
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        return if (xForwardedFor != null && xForwardedFor.isNotEmpty()) {
            xForwardedFor.split(",")[0].trim()
        } else {
            request.remoteAddr
        }
    }
    
    private fun getEndpointIdentifier(request: HttpServletRequest): String {
        val path = request.requestURI
        
        // Map path to endpoint identifier
        return when {
            path.contains("/api/auth/login") -> "login"
            path.contains("/api/auth/reset-password") -> "reset-password"
            path.contains("/api/auth/register") -> "register"
            else -> "default"
        }
    }
}
