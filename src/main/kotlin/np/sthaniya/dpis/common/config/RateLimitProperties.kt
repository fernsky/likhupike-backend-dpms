package np.sthaniya.dpis.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "resilience4j.ratelimiter")
data class RateLimitProperties(
    val enabled: Boolean = false,
    val instances: Map<String, RateLimitInstanceProperties> = emptyMap()
)

data class RateLimitInstanceProperties(
    val limitRefreshPeriod: String = "1m",
    val limitForPeriod: Int = 10,
    val timeoutDuration: String = "0",
    val baseConfig: String? = null
) {
    fun getRefreshPeriodInSeconds(): Long {
        return try {
            Duration.parse("PT${limitRefreshPeriod.uppercase()}").seconds
        } catch (e: Exception) {
            60L // default to 1 minute if parsing fails
        }
    }
}