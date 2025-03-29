package np.sthaniya.dpis.common.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class Bucket4jConfig {

    @Bean
    fun caffeineCacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager("rateLimit")
        cacheManager.setCaffeine(caffeineCacheBuilder())
        return cacheManager
    }
    
    fun caffeineCacheBuilder(): Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .recordStats()
    }
}
