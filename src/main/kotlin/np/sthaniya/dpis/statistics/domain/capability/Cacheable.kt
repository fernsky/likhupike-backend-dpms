package np.sthaniya.dpis.statistics.domain.capability

import java.time.Duration
import java.time.LocalDateTime

/**
 * Interface for entities that can be cached to improve performance.
 */
interface Cacheable {
    /**
     * Time when the entity was last cached
     */
    var lastCachedAt: LocalDateTime?
    
    /**
     * Cache expiration policy (e.g., TIME_BASED, VERSION_BASED, NEVER)
     */
    var cacheExpirationPolicy: String
    
    /**
     * Time-to-live in seconds for cached data
     */
    var cacheTTLSeconds: Long
    
    /**
     * Get a unique key for caching this entity
     */
    fun getCacheKey(): String
    
    /**
     * Get the TTL (time-to-live) duration for this entity in cache
     */
    fun getCacheTTL(): Duration {
        return Duration.ofSeconds(cacheTTLSeconds)
    }
    
    /**
     * Determine if this entity is currently cacheable
     */
    fun isCacheable(): Boolean {
        return true
    }
    
    /**
     * Get the cache region/namespace for this entity type
     */
    fun getCacheRegion(): String {
        return this.javaClass.simpleName
    }
    
    /**
     * Get a list of cache dependencies that should be invalidated together
     */
    fun getCacheDependencies(): List<CacheDependency> {
        return emptyList()
    }
    
    /**
     * Generate content to be stored in cache
     */
    fun generateCacheContent(): Map<String, Any?> {
        // Default implementation returns an empty map
        // Concrete entities should override this
        return emptyMap()
    }
    
    /**
     * Restore entity state from cached content
     */
    fun restoreFromCacheContent(content: Map<String, Any?>) {
        // Default empty implementation
        // Concrete entities should override this
    }
    
    /**
     * Invalidate this entity in the cache
     */
    fun invalidateCache() {
        // Default empty implementation
        // To be overridden with actual cache invalidation logic
    }
}

/**
 * Represents a cache dependency for a cached entity
 */
data class CacheDependency(
    val entityType: String,
    val entityId: String
)
