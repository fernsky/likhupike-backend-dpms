package np.sthaniya.dpis.common.service

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import np.sthaniya.dpis.common.config.RateLimitConfig
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Service
class RateLimitService(private val rateLimitConfig: RateLimitConfig) {
    
    private val globalBucket: Bucket
    
    // Client-specific cache using Caffeine
    private val clientBuckets = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .maximumSize(10000)
        .build<String, Bucket>()
    
    // Endpoint-specific cache using Caffeine
    private val endpointBuckets = ConcurrentHashMap<String, com.github.benmanes.caffeine.cache.Cache<String, Bucket>>()
    
    init {
        // Initialize the global bucket
        val globalLimit = rateLimitConfig.global
        val refill = Refill.intervally(globalLimit.refillTokens, 
                 Duration.ofSeconds(globalLimit.refillTimeUnit.toSeconds(globalLimit.refillDuration)))
        val bandwidth = Bandwidth.classic(globalLimit.capacity, refill)
        globalBucket = Bucket4j.builder().addLimit(bandwidth).build()
    }
    
    fun resolveBucket(clientId: String, endpoint: String): Bucket {
        // For endpoint-specific rate limiting
        if (rateLimitConfig.endpoints.containsKey(endpoint)) {
            val cache = endpointBuckets.computeIfAbsent(endpoint) {
                Caffeine.newBuilder()
                    .expireAfterAccess(1, TimeUnit.HOURS)
                    .maximumSize(10000)
                    .build()
            }
            
            return cache.get(clientId) { createEndpointBucket(endpoint) }
        }
        
        // For client-specific rate limiting
        return clientBuckets.get(clientId) { createClientBucket() }
    }
    
    fun tryConsume(clientId: String, endpoint: String): Boolean {
        if (!rateLimitConfig.enabled) {
            return true
        }
        
        // First check global limit
        if (!globalBucket.tryConsume(1)) {
            return false
        }
        
        // Then check specific limit
        return resolveBucket(clientId, endpoint).tryConsume(1)
    }
    
    private fun createClientBucket(): Bucket {
        val globalLimit = rateLimitConfig.global
        val refill = Refill.intervally(globalLimit.refillTokens, 
                 Duration.ofSeconds(globalLimit.refillTimeUnit.toSeconds(globalLimit.refillDuration)))
        val bandwidth = Bandwidth.classic(globalLimit.capacity, refill)
        return Bucket4j.builder().addLimit(bandwidth).build()
    }
    
    private fun createEndpointBucket(endpoint: String): Bucket {
        val limitProps = rateLimitConfig.endpoints[endpoint] ?: rateLimitConfig.global
        val refill = Refill.intervally(limitProps.refillTokens, 
                 Duration.ofSeconds(limitProps.refillTimeUnit.toSeconds(limitProps.refillDuration)))
        val bandwidth = Bandwidth.classic(limitProps.capacity, refill)
        return Bucket4j.builder().addLimit(bandwidth).build()
    }
}
