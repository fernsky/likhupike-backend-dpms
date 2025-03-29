package np.sthaniya.dpis.auth.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RateLimitFilter : OncePerRequestFilter(), Ordered {
    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("Error in rate limit filter", e)
            throw e
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RateLimitFilter::class.java)
    }
}