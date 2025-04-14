package np.sthaniya.dpis.statistics.domain.repository

import np.sthaniya.dpis.statistics.domain.capability.Cacheable
import java.time.LocalDateTime
import java.util.UUID

/**
 * Interface for managing cached data in the system.
 * 
 * Provides tiered caching capabilities with support for different 
 * invalidation strategies to optimize performance while ensuring data integrity.
 */
interface CacheManager {
    
    /**
     * Cache levels supported by the system
     */
    enum class CacheLevel {
        LOCAL,        // In-memory cache in current JVM
        DISTRIBUTED,  // Shared cache across multiple instances (e.g., Redis)
        ALL           // All cache levels
    }
    
    /**
     * Put an item in the cache
     *
     * @param key Cache key
     * @param value Object to cache
     * @param ttlSeconds Time-to-live in seconds
     * @param level Cache level(s) to use
     */
    fun <T> put(
        key: String,
        value: T,
        ttlSeconds: Long = 3600,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Put a cacheable entity in the cache using its own settings
     *
     * @param entity The cacheable entity
     * @param level Cache level(s) to use
     */
    fun put(
        entity: Cacheable,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Get an item from the cache
     *
     * @param key Cache key
     * @param type Class of the cached object
     * @return Cached object if found and valid, null otherwise
     */
    fun <T> get(
        key: String,
        type: Class<T>,
        level: CacheLevel = CacheLevel.ALL
    ): T?
    
    /**
     * Check if an item exists in the cache
     *
     * @param key Cache key
     * @return True if the item exists and is valid, false otherwise
     */
    fun exists(
        key: String,
        level: CacheLevel = CacheLevel.ALL
    ): Boolean
    
    /**
     * Remove an item from the cache
     *
     * @param key Cache key
     * @param level Cache level(s) to clear
     */
    fun evict(
        key: String,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Remove all items with a prefix from the cache
     *
     * @param keyPrefix Prefix for cache keys to remove
     * @param level Cache level(s) to clear
     */
    fun evictByPrefix(
        keyPrefix: String,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Invalidate all cached data for an entity
     *
     * @param entityId ID of the entity
     * @param entityType Type of the entity
     * @param level Cache level(s) to clear
     */
    fun invalidateEntity(
        entityId: UUID,
        entityType: String,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Invalidate all cached data for a specific entity type
     *
     * @param entityType Type of the entity
     * @param level Cache level(s) to clear
     */
    fun invalidateEntityType(
        entityType: String,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Invalidate all cached ward statistics
     *
     * @param wardId ID of the ward
     * @param level Cache level(s) to clear
     */
    fun invalidateWardStatistics(
        wardId: UUID,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Clear all caches
     *
     * @param level Cache level(s) to clear
     */
    fun clearAll(
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Get cache statistics
     *
     * @return Map of cache statistics
     */
    fun getStatistics(): Map<String, Any>
    
    /**
     * Refresh an item in the cache by re-fetching it from the source
     *
     * @param key Cache key
     * @param fetcher Function to re-fetch the data
     * @param ttlSeconds Time-to-live in seconds
     * @param level Cache level(s) to use
     * @return The refreshed object
     */
    fun <T> refresh(
        key: String,
        fetcher: () -> T,
        ttlSeconds: Long = 3600,
        level: CacheLevel = CacheLevel.ALL
    ): T
    
    /**
     * Pre-warm the cache with data
     *
     * @param items Map of cache keys to values
     * @param ttlSeconds Time-to-live in seconds
     * @param level Cache level(s) to use
     */
    fun <T> prewarm(
        items: Map<String, T>,
        ttlSeconds: Long = 3600,
        level: CacheLevel = CacheLevel.ALL
    )
    
    /**
     * Register a listener for cache events
     *
     * @param listener The cache event listener
     */
    fun registerCacheEventListener(listener: CacheEventListener)
    
    /**
     * Interface for cache event listeners
     */
    interface CacheEventListener {
        /**
         * Called when an item is evicted from the cache
         *
         * @param key The cache key
         * @param reason The reason for eviction
         */
        fun onEviction(key: String, reason: String)
        
        /**
         * Called when an item is updated in the cache
         *
         * @param key The cache key
         */
        fun onUpdate(key: String)
    }
}
