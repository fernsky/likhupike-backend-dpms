package np.sthaniya.dpis.common.config

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import np.sthaniya.dpis.common.enum.RateLimitType
import np.sthaniya.dpis.common.exception.RateLimitExceededException
import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory
import io.github.resilience4j.ratelimiter.annotation.RateLimiter as Resilience4jRateLimiter

@Configuration
@ConditionalOnProperty(
    value = ["resilience4j.ratelimiter.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class RateLimiterConfig {
    @Component
    class ClientRateLimiterHelper(
        private val rateLimiterRegistry: RateLimiterRegistry,
        private val i18nMessageService: I18nMessageService
    ) {
        private val logger = LoggerFactory.getLogger(javaClass)
        private val clientRateLimiters = ConcurrentHashMap<String, RateLimiter>()

        init {
            // Log whenever rate limiter is created
            rateLimiterRegistry.eventPublisher.onEntryAdded { event ->
                logger.info("Rate limiter created: {}", event.addedEntry.name)
            }
        }

        fun getRateLimiter(type: RateLimitType, useClientLimit: Boolean): String {
            val key = if (useClientLimit) {
                getClientRateLimiterName(type)
            } else {
                getGlobalRateLimiterName(type)
            }
            
            val rateLimiter = rateLimiterRegistry.rateLimiter(key)
            logRateLimiterState(key, rateLimiter)
            
            return key
        }

        private fun logRateLimiterState(key: String, rateLimiter: RateLimiter) {
            val metrics = rateLimiter.metrics
            logger.debug("""
                Rate Limiter State for '$key':
                - Available permissions: ${metrics.availablePermissions}
                - Number of waiting threads: ${metrics.numberOfWaitingThreads}
                - Successful calls: ${rateLimiter.eventPublisher.onSuccess { it.eventType.name }}
                - Failed calls: ${rateLimiter.eventPublisher.onFailure { it.eventType.name }}
            """.trimIndent())
        }

        private fun getClientRateLimiterName(type: RateLimitType): String {
            val clientIp = getClientIp()
            val key = "${type.name.lowercase()}-client-$clientIp"
            
            clientRateLimiters.computeIfAbsent(key) {
                rateLimiterRegistry.rateLimiter(
                    key,
                    "${type.name.lowercase()}-client"
                )
            }
            
            return key
        }

        private fun getGlobalRateLimiterName(type: RateLimitType): String {
            return "${type.name.lowercase()}-global"
        }

        private fun getClientIp(): String {
            val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
            
            return request.getHeader("X-Forwarded-For")?.split(",")?.get(0)
                ?: request.getHeader("X-Real-IP")
                ?: request.remoteAddr
        }
    }
}