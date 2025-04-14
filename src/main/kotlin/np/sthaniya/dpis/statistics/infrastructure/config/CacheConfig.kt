package np.sthaniya.dpis.statistics.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

/**
 * Configuration for caching infrastructure in the statistics module.
 * 
 * Sets up both local in-memory caching and distributed Redis caching.
 */
@Configuration
@EnableCaching
class CacheConfig(private val objectMapper: ObjectMapper) {
    
    /**
     * Default cache configuration prefix to namespace our application's cache entries
     */
    private val cachePrefix = "dpis:statistics:"
    
    /**
     * Local cache manager for in-memory caching
     */
    @Bean
    fun localCacheManager(): CacheManager {
        return ConcurrentMapCacheManager().apply {
            setCacheNames(listOf(
                "ward_statistics",
                "demographic_statistics",
                "education_statistics", 
                "economic_statistics"
            ))
        }
    }
    
    /**
     * Redis template for direct cache access
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory
        
        // Use String serializer for keys
        template.keySerializer = StringRedisSerializer()
        
        // Use JSON serializer for values
        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.valueSerializer = jsonSerializer
        
        // Also serialize hash keys and values using the same serializers
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = jsonSerializer
        
        return template
    }
    
    /**
     * Redis cache manager for distributed caching
     */
    @Bean
    @Primary
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        // Create JSON serializer for redis cache values
        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        
        // Configure the default cache settings
        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60)) // Default TTL
            .disableCachingNullValues()
            .prefixCacheNameWith(cachePrefix)
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
        
        // Create specific configurations for different cache regions with different TTLs
        val configMap = mapOf(
            "ward_statistics" to defaultConfig.entryTtl(Duration.ofHours(24)),
            "demographic_statistics" to defaultConfig.entryTtl(Duration.ofHours(24)),
            "education_statistics" to defaultConfig.entryTtl(Duration.ofHours(24)),
            "economic_statistics" to defaultConfig.entryTtl(Duration.ofHours(24)),
            "frequently_accessed" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "rarely_updated" to defaultConfig.entryTtl(Duration.ofDays(7))
        )
        
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(configMap)
            .build()
    }
}
