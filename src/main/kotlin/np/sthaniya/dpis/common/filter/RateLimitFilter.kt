package np.sthaniya.dpis.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.common.config.RateLimitConfig
import np.sthaniya.dpis.common.service.RateLimitService
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!rateLimitConfig.enabled) {
            logger.debug("Rate limiting disabled, skipping filter")
            filterChain.doFilter(request, response)
            return
        }

        val path = request.requestURI
        val method = request.method
        
        // Get client identifier (IP address or session ID)
        val clientId = getClientId(request)
        logger.debug("Processing rate limit for client: $clientId, path: $path, method: $method")
        
        // Get endpoint identifier
        val endpoint = getEndpointIdentifier(request)
        
        // Check if rate limit is exceeded
        if (rateLimitService.tryConsume(clientId, endpoint)) {
            logger.debug("Rate limit not exceeded for client: $clientId, endpoint: $endpoint")
            filterChain.doFilter(request, response)
        } else {
            logger.warn("Rate limit exceeded for client: $clientId, endpoint: $endpoint, path: $path")
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
            path.contains("/api/v1/auth/login") -> "login"
            path.contains("/api/v1/auth/reset-password") -> "reset-password"
            path.contains("/api/v1/auth/register") -> "register"
            else -> "default"
        }.also {
            logger.debug("Mapped path: $path to endpoint identifier: $it")
        }
    }
}
