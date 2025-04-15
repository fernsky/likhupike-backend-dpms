package np.sthaniya.dpis.statistics.infrastructure.service

import np.sthaniya.dpis.statistics.domain.capability.Cacheable
import np.sthaniya.dpis.statistics.domain.repository.CacheManager
import np.sthaniya.dpis.statistics.domain.repository.CacheManager.CacheLevel
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager as SpringCacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Service implementation for caching operations in the statistics module.
 * 
 * Implements a two-tiered caching strategy:
 * 1. Local in-memory cache for frequently accessed data
 * 2. Distributed Redis cache for sharing across application instances
 */
@Service
class CacheService(
    private val localCacheManager: SpringCacheManager,
    private val redisCacheManager: SpringCacheManager,
    private val redisTemplate: RedisTemplate<String, Any>
) : CacheManager {
    
    private val logger = LoggerFactory.getLogger(CacheService::class.java)
    
    // Simple in-memory cache for local caching
    private val localCache = ConcurrentHashMap<String, CacheEntry<Any>>()
    
    // List of cache event listeners
    private val eventListeners = mutableListOf<CacheManager.CacheEventListener>()
    
    // Cache statistics
    private val cacheStats = ConcurrentHashMap<String, Long>().apply {
        put("hits", 0L)
        put("misses", 0L)
        put("evictions", 0L)
        put("puts", 0L)
        put("localHits", 0L)
        put("distributedHits", 0L)
    }
    
    /**
     * Put an item in the cache
     */
    override fun <T> put(
        key: String,
        value: T,
        ttlSeconds: Long,
        level: CacheLevel
    ) {
        try {
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                // Store in local cache
                localCache[key] = CacheEntry(value as Any, ttlSeconds)
                
                // Also store in Spring's local cache manager if appropriate
                val cacheName = determineCacheRegion(key)
                localCacheManager.getCache(cacheName)?.put(key, value)
                
                logger.debug("Stored item in LOCAL cache with key: {}, TTL: {} seconds", key, ttlSeconds)
            }
            
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Store in Redis distributed cache
                redisTemplate.opsForValue().set(key, value as Any, ttlSeconds, TimeUnit.SECONDS)
                
                // Also store in Spring's Redis cache manager if appropriate
                val cacheName = determineCacheRegion(key)
                redisCacheManager.getCache(cacheName)?.put(key, value)
                
                logger.debug("Stored item in DISTRIBUTED cache with key: {}, TTL: {} seconds", key, ttlSeconds)
            }
            
            // Update statistics
            incrementStat("puts")
            
            // Notify listeners
            notifyListeners { it.onUpdate(key) }
        } catch (e: Exception) {
            logger.error("Error caching item with key: {}", key, e)
        }
    }
    
    /**
     * Put a cacheable entity in the cache using its own settings
     */
    override fun put(entity: Cacheable, level: CacheLevel) {
        if (!entity.isCacheable()) {
            logger.debug("Entity is not cacheable: {}", entity.javaClass.simpleName)
            return
        }
        
        val key = entity.getCacheKey()
        val ttl = entity.cacheTTLSeconds
        val region = entity.getCacheRegion()
        
        logger.debug("Caching entity {} in region {} with TTL {} seconds at level {}", 
                    entity.javaClass.simpleName, region, ttl, level)
        
        // Update the last cached timestamp on the entity
        entity.updateCacheTimestamp()
        
        // Generate cache content and store it
        val cacheContent = entity.generateCacheContent()
        put(key, cacheContent, ttl, level)
        
        // Store the actual entity if needed
        put("entity:$key", entity, ttl, level)
    }
    
    /**
     * Get an item from the cache
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> get(
        key: String,
        type: Class<T>,
        level: CacheLevel
    ): T? {
        try {
            // Try local cache first for better performance
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                // Check our direct local cache
                val localEntry = localCache[key]
                if (localEntry != null && !localEntry.isExpired()) {
                    incrementStat("hits")
                    incrementStat("localHits")
                    logger.debug("Cache HIT (LOCAL): {}", key)
                    return localEntry.value as? T
                }
                
                // If not found, check Spring's local cache manager
                val cacheName = determineCacheRegion(key)
                val cacheValue = localCacheManager.getCache(cacheName)?.get(key)?.get()
                
                if (cacheValue != null && type.isInstance(cacheValue)) {
                    // Update our local cache too for consistency
                    localCache[key] = CacheEntry(cacheValue, 3600) // 1 hour default
                    
                    incrementStat("hits")
                    incrementStat("localHits")
                    logger.debug("Cache HIT (SPRING LOCAL): {}", key)
                    return cacheValue as T
                }
                
                // If we're only checking LOCAL and didn't find it, record a miss
                if (level == CacheLevel.LOCAL) {
                    incrementStat("misses")
                    logger.debug("Cache MISS (LOCAL): {}", key)
                    return null
                }
            }
            
            // Then try distributed cache if requested
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Try Spring's Redis cache manager first
                val cacheName = determineCacheRegion(key)
                val redisCacheValue = redisCacheManager.getCache(cacheName)?.get(key)?.get()
                
                if (redisCacheValue != null && type.isInstance(redisCacheValue)) {
                    // Refresh in local cache if using both levels
                    if (level == CacheLevel.ALL) {
                        localCache[key] = CacheEntry(redisCacheValue, 3600) // 1 hour default for local
                    }
                    
                    incrementStat("hits")
                    incrementStat("distributedHits")
                    logger.debug("Cache HIT (SPRING REDIS): {}", key)
                    return redisCacheValue as T
                }
                
                // If not found, try direct Redis access
                val distributedValue = redisTemplate.opsForValue().get(key)
                if (distributedValue != null && type.isInstance(distributedValue)) {
                    // Refresh in local cache if using both levels
                    if (level == CacheLevel.ALL) {
                        val ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS)
                        if (ttl > 0) {
                            localCache[key] = CacheEntry(distributedValue, ttl)
                        }
                    }
                    
                    incrementStat("hits")
                    incrementStat("distributedHits")
                    logger.debug("Cache HIT (REDIS): {}", key)
                    return distributedValue as T
                }
            }
            
            // If we get here, it's a cache miss
            incrementStat("misses")
            logger.debug("Cache MISS for key: {} at level {}", key, level)
            return null
            
        } catch (e: Exception) {
            logger.error("Error retrieving cached item with key: {}", key, e)
            incrementStat("misses")
            return null
        }
    }
    
    /**
     * Check if an item exists in the cache
     */
    override fun exists(key: String, level: CacheLevel): Boolean {
        try {
            // Check local cache first
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                val localEntry = localCache[key]
                if (localEntry != null && !localEntry.isExpired()) {
                    return true
                }
                
                // Check Spring's local cache
                val cacheName = determineCacheRegion(key)
                if (localCacheManager.getCache(cacheName)?.get(key) != null) {
                    return true
                }
                
                // If only checking local, return false here
                if (level == CacheLevel.LOCAL) {
                    return false
                }
            }
            
            // Then check distributed cache
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Check Spring's Redis cache
                val cacheName = determineCacheRegion(key)
                if (redisCacheManager.getCache(cacheName)?.get(key) != null) {
                    return true
                }
                
                // Check direct Redis access
                return redisTemplate.hasKey(key) == true
            }
            
            return false
        } catch (e: Exception) {
            logger.error("Error checking cache for key: {}", key, e)
            return false
        }
    }
    
    /**
     * Remove an item from the cache
     */
    override fun evict(key: String, level: CacheLevel) {
        try {
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                // Remove from local cache
                localCache.remove(key)?.let {
                    incrementStat("evictions")
                    logger.debug("Evicted from LOCAL cache: {}", key)
                }
                
                // Remove from Spring's local cache
                val cacheName = determineCacheRegion(key)
                localCacheManager.getCache(cacheName)?.evict(key)
            }
            
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Remove from Spring's Redis cache
                val cacheName = determineCacheRegion(key)
                redisCacheManager.getCache(cacheName)?.evict(key)
                
                // Remove from direct Redis access
                redisTemplate.delete(key)
                logger.debug("Evicted from DISTRIBUTED cache: {}", key)
            }
            
            // Notify listeners
            notifyListeners { it.onEviction(key, "Explicit eviction") }
        } catch (e: Exception) {
            logger.error("Error evicting cache item with key: {}", key, e)
        }
    }
    
    /**
     * Remove all items with a prefix from the cache
     */
    override fun evictByPrefix(keyPrefix: String, level: CacheLevel) {
        try {
            // For local cache, iterate and remove matching keys
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                val keysToRemove = localCache.keys.filter { it.startsWith(keyPrefix) }
                keysToRemove.forEach { localCache.remove(it) }
                
                // Update statistics
                incrementStat("evictions", keysToRemove.size.toLong())
                
                logger.debug("Evicted {} items with prefix {} from LOCAL cache", keysToRemove.size, keyPrefix)
                
                // For Spring's local cache, we need to clear entire regions
                // This is inefficient but necessary due to Spring Cache API limitations
                val cacheName = determineCacheRegion("$keyPrefix:dummy")
                localCacheManager.getCache(cacheName)?.clear()
            }
            
            // For Redis, use scan and delete pattern
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                val redisKeyPattern = "$keyPrefix*"
                val keys = redisTemplate.keys(redisKeyPattern)
                if (!keys.isNullOrEmpty()) {
                    redisTemplate.delete(keys)
                    logger.debug("Evicted {} items with prefix {} from DISTRIBUTED cache", keys.size, keyPrefix)
                }
                
                // For Spring's Redis cache, we need to clear entire regions
                val cacheName = determineCacheRegion("$keyPrefix:dummy")
                redisCacheManager.getCache(cacheName)?.clear()
            }
        } catch (e: Exception) {
            logger.error("Error evicting cache items with prefix: {}", keyPrefix, e)
        }
    }
    
    /**
     * Invalidate all cached data for an entity
     */
    override fun invalidateEntity(entityId: UUID, entityType: String, level: CacheLevel) {
        try {
            // Form the common key pattern for this entity
            val keyPrefix = "$entityType:$entityId"
            
            // Evict by prefix
            evictByPrefix(keyPrefix, level)
            
            logger.debug("Invalidated cache for entity type: {}, id: {} at level {}", entityType, entityId, level)
        } catch (e: Exception) {
            logger.error("Error invalidating cache for entity: {}", entityId, e)
        }
    }
    
    /**
     * Invalidate all cached data for a specific entity type
     */
    override fun invalidateEntityType(entityType: String, level: CacheLevel) {
        try {
            // Form the common key pattern for this entity type
            val keyPrefix = "$entityType:"
            
            // Evict by prefix
            evictByPrefix(keyPrefix, level)
            
            logger.debug("Invalidated cache for entity type: {} at level {}", entityType, level)
        } catch (e: Exception) {
            logger.error("Error invalidating cache for entity type: {}", entityType, e)
        }
    }
    
    /**
     * Invalidate all cached ward statistics
     */
    override fun invalidateWardStatistics(wardId: UUID, level: CacheLevel) {
        try {
            // Form the key pattern for ward statistics
            val keyPrefix = "ward_statistics:$wardId"
            
            // Evict by prefix
            evictByPrefix(keyPrefix, level)
            
            logger.debug("Invalidated cache for ward: {} at level {}", wardId, level)
        } catch (e: Exception) {
            logger.error("Error invalidating cache for ward: {}", wardId, e)
        }
    }
    
    /**
     * Clear all caches
     */
    override fun clearAll(level: CacheLevel) {
        try {
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                // Clear local cache
                val count = localCache.size
                localCache.clear()
                
                // Update statistics
                incrementStat("evictions", count.toLong())
                
                // Clear Spring's local cache
                localCacheManager.cacheNames.forEach { cacheName ->
                    localCacheManager.getCache(cacheName)?.clear()
                }
                
                logger.info("Cleared LOCAL cache - {} entries removed", count)
            }
            
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Clear Spring's Redis cache
                redisCacheManager.cacheNames.forEach { cacheName ->
                    redisCacheManager.getCache(cacheName)?.clear()
                }
                
                // Clear Redis cache - careful with this, only clear application-specific keys
                val appPrefix = "dpis:statistics:"
                val keys = redisTemplate.keys("$appPrefix*")
                if (!keys.isNullOrEmpty()) {
                    redisTemplate.delete(keys)
                    logger.info("Cleared DISTRIBUTED cache - {} entries removed", keys.size)
                }
            }
        } catch (e: Exception) {
            logger.error("Error clearing all caches", e)
        }
    }
    
    /**
     * Clean up expired entries from the cache
     * This is particularly important for local cache that doesn't have auto-expiry
     */
    fun cleanupExpiredEntries() {
        try {
            // For local cache, we need to manually check expiration
            val now = LocalDateTime.now()
            val expiredKeys = localCache.entries
                .filter { it.value.isExpired(now) }
                .map { it.key }
                
            if (expiredKeys.isNotEmpty()) {
                expiredKeys.forEach { localCache.remove(it) }
                logger.debug("Removed {} expired entries from local cache", expiredKeys.size)
            }
            
            // Redis handles expiration automatically
        } catch (e: Exception) {
            logger.error("Error cleaning up expired cache entries", e)
        }
    }
    
    /**
     * Get cache statistics
     */
    override fun getStatistics(): Map<String, Any> {
        val stats = HashMap<String, Any>(cacheStats)
        
        // Add additional statistics
        stats["localCacheSize"] = localCache.size
        stats["hitRatio"] = calculateHitRatio()
        stats["localHitRatio"] = calculateLocalHitRatio()
        
        // Add Spring cache manager statistics if available
        stats["springLocalCaches"] = localCacheManager.cacheNames.toList()
        stats["springRedisCaches"] = redisCacheManager.cacheNames.toList()
        
        // Add cache level distribution
        val localHits = cacheStats["localHits"] ?: 0
        val distributedHits = cacheStats["distributedHits"] ?: 0
        val total = localHits + distributedHits
        
        if (total > 0) {
            stats["localHitPercentage"] = (localHits.toDouble() / total.toDouble()) * 100
            stats["distributedHitPercentage"] = (distributedHits.toDouble() / total.toDouble()) * 100
        } else {
            stats["localHitPercentage"] = 0.0
            stats["distributedHitPercentage"] = 0.0
        }
        
        return stats
    }
    
    /**
     * Refresh an item in the cache by re-fetching it from the source
     */
    override fun <T> refresh(
        key: String,
        fetcher: () -> T,
        ttlSeconds: Long,
        level: CacheLevel
    ): T {
        try {
            // Fetch fresh data
            val freshData = fetcher()
            
            // Update cache
            put(key, freshData, ttlSeconds, level)
            
            logger.debug("Refreshed cache for key: {} at level {}", key, level)
            return freshData
        } catch (e: Exception) {
            logger.error("Error refreshing cache for key: {}", key, e)
            
            // If refresh fails, try to return existing data
            @Suppress("UNCHECKED_CAST")
            val existingData = get(key, Any::class.java, level)
            if (existingData != null) {
                logger.debug("Refresh failed, returning existing cached data for key: {}", key)
                return existingData as T
            }
            
            throw e
        }
    }
    
    /**
     * Pre-warm the cache with data
     */
    override fun <T> prewarm(
        items: Map<String, T>,
        ttlSeconds: Long,
        level: CacheLevel
    ) {
        try {
            items.forEach { (key, value) ->
                put(key, value, ttlSeconds, level)
            }
            
            logger.info("Pre-warmed cache with {} items at level {}", items.size, level)
        } catch (e: Exception) {
            logger.error("Error pre-warming cache", e)
        }
    }
    
    /**
     * Register a listener for cache events
     */
    override fun registerCacheEventListener(listener: CacheManager.CacheEventListener) {
        eventListeners.add(listener)
        logger.debug("Registered new cache event listener: {}", listener.javaClass.simpleName)
    }
    
    /**
     * Utility method to increment a statistical counter
     */
    private fun incrementStat(stat: String, increment: Long = 1L) {
        cacheStats.compute(stat) { _, value -> (value ?: 0) + increment }
    }
    
    /**
     * Calculate cache hit ratio
     */
    private fun calculateHitRatio(): Double {
        val hits = cacheStats["hits"] ?: 0
        val misses = cacheStats["misses"] ?: 0
        val total = hits + misses
        
        return if (total > 0) {
            hits.toDouble() / total.toDouble()
        } else 0.0
    }
    
    /**
     * Calculate local cache hit ratio
     */
    private fun calculateLocalHitRatio(): Double {
        val localHits = cacheStats["localHits"] ?: 0
        val hits = cacheStats["hits"] ?: 0
        
        return if (hits > 0) {
            localHits.toDouble() / hits.toDouble()
        } else 0.0
    }
    
    /**
     * Determine the appropriate cache region/name for a key
     */
    private fun determineCacheRegion(key: String): String {
        return when {
            key.startsWith("demographic_") -> "demographic_statistics"
            key.startsWith("ward_") -> "ward_statistics"
            key.startsWith("education_") -> "education_statistics"
            key.startsWith("economic_") -> "economic_statistics"
            key.contains("frequently_accessed") -> "frequently_accessed"
            key.contains("rarely_updated") -> "rarely_updated"
            else -> "ward_statistics" // Default cache
        }
    }
    
    /**
     * Notify all registered listeners of cache events
     */
    private fun notifyListeners(action: (CacheManager.CacheEventListener) -> Unit) {
        eventListeners.forEach { listener ->
            try {
                action(listener)
            } catch (e: Exception) {
                logger.error("Error notifying cache event listener", e)
            }
        }
    }
    
    /**
     * Helper class to store cache entries with TTL in local cache
     */
    private inner class CacheEntry<T>(
        val value: T,
        ttlSeconds: Long
    ) {
        private val creationTime: LocalDateTime = LocalDateTime.now()
        private val expirationTime: LocalDateTime = creationTime.plusSeconds(ttlSeconds)
        
        fun isExpired(now: LocalDateTime = LocalDateTime.now()): Boolean {
            return now.isAfter(expirationTime)
        }
        
        fun timeToLive(now: LocalDateTime = LocalDateTime.now()): Duration {
            return if (isExpired(now)) {
                Duration.ZERO
            } else {
                Duration.between(now, expirationTime)
            }
        }
    }
}
