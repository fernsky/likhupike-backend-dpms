package np.sthaniya.dpis.statistics.domain.capability

import java.time.Duration
import java.time.LocalDateTime

/**
 * Interface for entities that can be cached.
 * 
 * Provides metadata and behavior needed for intelligent caching strategies,
 * including time-based and version-based invalidation.
 */
interface Cacheable {
    
    /**
     * Time when the entity was last cached
     */
    var lastCachedAt: LocalDateTime?
    
    /**
     * Cache expiration policy
     * Possible values:
     * - TIME_BASED: Cache expires after a fixed TTL
     * - VERSION_BASED: Cache expires when entity version changes
     * - EVENT_BASED: Cache expires when specific events occur
     * - HYBRID: Combination of time and version/event strategies
     */
    var cacheExpirationPolicy: String
    
    /**
     * Time-to-live in seconds for time-based caching
     */
    var cacheTTLSeconds: Long
    
    /**
     * Version that invalidates the cache when changed
     * Used with VERSION_BASED policy
     */
    fun getCacheVersion(): String {
        // Default implementation returns an empty string
        // Override in implementing classes
        return ""
    }
    
    /**
     * Get a unique key for caching this entity
     * 
     * @return A string that uniquely identifies this entity for caching
     */
    fun getCacheKey(): String
    
    /**
     * Check if the cached entity is still valid
     * 
     * @param now The current time for TTL calculation
     * @return True if the cache entry is still valid, false otherwise
     */
    fun isCacheValid(now: LocalDateTime = LocalDateTime.now()): Boolean {
        if (lastCachedAt == null) return false
        
        return when (cacheExpirationPolicy) {
            "TIME_BASED" -> {
                val expirationTime = lastCachedAt!!.plusSeconds(cacheTTLSeconds)
                now.isBefore(expirationTime)
            }
            "VERSION_BASED", "EVENT_BASED", "HYBRID" -> {
                // Implementation depends on concrete class
                false
            }
            else -> false
        }
    }
    
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
    
    /**
     * Get metadata about the cache entry
     * 
     * @return Map of cache metadata
     */
    fun getCacheMetadata(): Map<String, Any?> {
        return mapOf(
            "cacheKey" to getCacheKey(),
            "lastCachedAt" to lastCachedAt,
            "expirationPolicy" to cacheExpirationPolicy,
            "ttlSeconds" to cacheTTLSeconds,
            "cacheVersion" to getCacheVersion()
        )
    }
    
    /**
     * Update the last cached time
     * 
     * @param timestamp The time when the entity was cached
     */
    fun updateCacheTimestamp(timestamp: LocalDateTime = LocalDateTime.now()) {
        lastCachedAt = timestamp
    }
    
    /**
     * Calculate cache priority (used for cache eviction strategies)
     * Higher values indicate higher priority for keeping in cache
     * 
     * @return Priority score from 0 to 100
     */
    fun getCachePriority(): Int {
        // Default implementation - can be overridden in implementing classes
        return 50
    }
}

/**
 * Represents a cache dependency for a cached entity
 */
data class CacheDependency(
    val entityType: String,
    val entityId: String
)
