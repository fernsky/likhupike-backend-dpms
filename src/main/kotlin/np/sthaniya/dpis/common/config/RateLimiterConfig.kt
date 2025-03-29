package np.sthaniya.dpis.common.config

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import np.sthaniya.dpis.common.annotation.RateLimitType
import np.sthaniya.dpis.common.annotation.RateLimited
import np.sthaniya.dpis.common.exception.RateLimitExceededException
import np.sthaniya.dpis.common.service.I18nMessageService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.concurrent.ConcurrentHashMap

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(
    value = ["resilience4j.ratelimiter.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class RateLimiterConfig {
    @Bean
    fun rateLimiterAspect(
        rateLimiterRegistry: RateLimiterRegistry,
        i18nMessageService: I18nMessageService
    ): RateLimiterAspect {
        return RateLimiterAspect(rateLimiterRegistry, i18nMessageService)
    }
}

@Aspect
@Component
@ConditionalOnProperty(
    value = ["resilience4j.ratelimiter.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class RateLimiterAspect(
    private val rateLimiterRegistry: RateLimiterRegistry,
    private val i18nMessageService: I18nMessageService
) {
    private val clientRateLimiters = ConcurrentHashMap<String, RateLimiter>()

    @Around("@annotation(np.sthaniya.dpis.common.annotation.RateLimited)")
    fun rateLimiterHandler(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val rateLimited = method.getAnnotation(RateLimited::class.java)
        
        val rateLimiter = if (rateLimited.useClientLimit) {
            getClientRateLimiter(rateLimited.type)
        } else {
            getGlobalRateLimiter(rateLimited.type)
        }

        return try {
            RateLimiter.decorateSupplier(rateLimiter) { joinPoint.proceed() }.get()
        } catch (e: Exception) {
            val timeoutDuration = rateLimiter.rateLimiterConfig.limitRefreshPeriod
            val remainingSeconds = timeoutDuration.seconds
            
            throw RateLimitExceededException(
                message = i18nMessageService.getMessage("auth.error.RATE_001"),
                remainingSeconds = remainingSeconds
            )
        }
    }

    private fun getClientRateLimiter(type: RateLimitType): RateLimiter {
        val clientIp = getClientIp()
        val key = "${type.name.lowercase()}-client-$clientIp"
        
        return clientRateLimiters.computeIfAbsent(key) {
            rateLimiterRegistry.rateLimiter(
                key,
                "${type.name.lowercase()}-client"
            )
        }
    }

    private fun getGlobalRateLimiter(type: RateLimitType): RateLimiter {
        return rateLimiterRegistry.rateLimiter(
            "${type.name.lowercase()}-global"
        )
    }

    private fun getClientIp(): String {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        
        return request.getHeader("X-Forwarded-For")?.split(",")?.get(0)
            ?: request.getHeader("X-Real-IP")
            ?: request.remoteAddr
    }
}