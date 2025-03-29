package np.sthaniya.dpis.common.config

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import np.sthaniya.dpis.common.exception.RateLimitExceededException
import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Configuration
@EnableAspectJAutoProxy
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
class RateLimiterAspect(
    private val rateLimiterRegistry: RateLimiterRegistry,
    private val i18nMessageService: I18nMessageService
) {
    @Around("@annotation(np.sthaniya.dpis.common.annotation.RateLimited)")
    fun rateLimiterHandler(joinPoint: ProceedingJoinPoint): Any {
        val rateLimiter = rateLimiterRegistry.rateLimiter("auth")
        
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
}