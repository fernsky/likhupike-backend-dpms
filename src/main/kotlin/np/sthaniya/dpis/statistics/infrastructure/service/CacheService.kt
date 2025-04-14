package np.sthaniya.dpis.statistics.infrastructure.service

import np.sthaniya.dpis.statistics.domain.capability.Cacheable
import np.sthaniya.dpis.statistics.domain.repository.CacheManager
import np.sthaniya.dpis.statistics.domain.repository.CacheManager.CacheLevel
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager as SpringCacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
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
            }
            
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                // Store in Redis distributed cache
                redisTemplate.opsForValue().set(key, value as Any, ttlSeconds, TimeUnit.SECONDS)
                
                // Also store in Spring's Redis cache manager if appropriate
                val cacheName = determineCacheRegion(key)
                redisCacheManager.getCache(cacheName)?.put(key, value)
            }
            
            // Update statistics
            incrementStat("puts")
            
            // Notify listeners
            notifyListeners { it.onUpdate(key) }
            
            logger.debug("Cached item with key: {}, TTL: {} seconds", key, ttlSeconds)
        } catch (e: Exception) {
            logger.error("Error caching item with key: {}", key, e)
        }
    }
    
    /**
     * Put a cacheable entity in the cache using its own settings
     */
    override fun put(entity: Cacheable, level: CacheLevel) {
        if (!entity.isCacheable()) {
            return
        }
        
        val key = entity.getCacheKey()
        val ttl = entity.cacheTTLSeconds
        
        // Update the last cached timestamp on the entity
        entity.updateCacheTimestamp()
        
        // Cache the entity
        put(key, entity, ttl, level)
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
                    return localEntry.value as? T
                }
                
                // If not found, check Spring's local cache manager
                val cacheName = determineCacheRegion(key)
                val cacheValue = localCacheManager.getCache(cacheName)?.get(key)?.get()
                
                if (cacheValue != null && type.isInstance(cacheValue)) {
                    incrementStat("hits")
                    return cacheValue as T
                }
            }
            
            // Then try distributed cache
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
                    return distributedValue as T
                }
            }
            
            incrementStat("misses")
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
            }
            
            // Notify listeners
            notifyListeners { it.onEviction(key, "Explicit eviction") }
            
            logger.debug("Evicted cache item with key: {}", key)
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
                
                // No easy way to evict by prefix from Spring's local cache
                // We'd need to implement this manually if needed
            }
            
            // For Redis, use scan and delete pattern
            if (level == CacheLevel.DISTRIBUTED || level == CacheLevel.ALL) {
                val redisKeyPattern = "$keyPrefix*"
                val keys = redisTemplate.keys(redisKeyPattern)
                if (!keys.isNullOrEmpty()) {
                    redisTemplate.delete(keys)
                }
                
                // Spring Redis cache doesn't support easy prefix-based eviction
                // We'd need to implement this manually if needed
            }
            
            logger.debug("Evicted cache items with prefix: {}", keyPrefix)
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
            
            logger.debug("Invalidated cache for entity type: {}, id: {}", entityType, entityId)
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
            
            logger.debug("Invalidated cache for entity type: {}", entityType)
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
            
            logger.debug("Invalidated cache for ward: {}", wardId)
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
                }
            }
            
            logger.info("Cleared all caches at level: {}", level)
        } catch (e: Exception) {
            logger.error("Error clearing all caches", e)
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
        
        // Add Spring cache manager statistics if available
        stats["springLocalCaches"] = localCacheManager.cacheNames.toList()
        stats["springRedisCaches"] = redisCacheManager.cacheNames.toList()
        
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
            
            return freshData
        } catch (e: Exception) {
            logger.error("Error refreshing cache for key: {}", key, e)
            
            // If refresh fails, try to return existing data
            @Suppress("UNCHECKED_CAST")
            if (level == CacheLevel.LOCAL || level == CacheLevel.ALL) {
                val localEntry = localCache[key]
                if (localEntry != null && !localEntry.isExpired()) {
                    return localEntry.value as T
                }
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
            
            logger.info("Pre-warmed cache with {} items", items.size)
        } catch (e: Exception) {
            logger.error("Error pre-warming cache", e)
        }
    }
    
    /**
     * Register a listener for cache events
     */
    override fun registerCacheEventListener(listener: CacheManager.CacheEventListener) {
        eventListeners.add(listener)
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
        
        fun isExpired(): Boolean {
            return LocalDateTime.now().isAfter(expirationTime)
        }
    }
}
