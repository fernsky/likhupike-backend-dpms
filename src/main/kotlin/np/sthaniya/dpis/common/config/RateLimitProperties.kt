package np.sthaniya.dpis.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@ConfigurationProperties(prefix = "resilience4j.ratelimiter")
class RateLimitProperties {
    var enabled: Boolean = false
    var instances: Map<String, RateLimitInstanceProperties> = emptyMap()
}

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