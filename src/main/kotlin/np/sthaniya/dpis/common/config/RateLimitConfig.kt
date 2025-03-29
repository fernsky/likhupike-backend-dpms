package np.sthaniya.dpis.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@ConfigurationProperties(prefix = "app.rate-limiting")
class RateLimitConfig {
    var enabled: Boolean = true
    var global: RateLimitProperties = RateLimitProperties()
    var endpoints: Map<String, RateLimitProperties> = HashMap()

    class RateLimitProperties {
        var capacity: Long = 100
        var refillTokens: Long = 100
        var refillDuration: Long = 1
        var refillTimeUnit: TimeUnit = TimeUnit.MINUTES
    }
}
