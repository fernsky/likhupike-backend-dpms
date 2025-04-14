package np.sthaniya.dpis.statistics.domain.repository

import np.sthaniya.dpis.statistics.domain.model.BaseStatistics
import java.time.LocalDateTime
import java.util.UUID

/**
 * Base repository interface for statistics aggregates.
 * 
 * Provides common operations for accessing and persisting statistical data
 * with built-in support for versioning, auditing, and data consistency.
 */
interface StatisticsRepository<T : BaseStatistics> {
    
    /**
     * Find a statistics entity by its unique identifier
     *
     * @param id The unique identifier of the statistics
     * @return The statistics entity if found, null otherwise
     */
    fun findById(id: UUID): T?
    
    /**
     * Find the latest version of statistics by its reference properties
     *
     * @param criteria Map of property name to value for filtering
     * @return The latest statistics entity matching the criteria, null if none found
     */
    fun findLatestByCriteria(criteria: Map<String, Any>): T?
    
    /**
     * Find statistics as they existed at a specific point in time
     *
     * @param id The unique identifier of the statistics
     * @param asOfDate The historical point in time
     * @return The statistics entity as it existed at the specified time, null if not found
     */
    fun findByIdAsOf(id: UUID, asOfDate: LocalDateTime): T?
    
    /**
     * Find all statistics entities matching the given criteria
     *
     * @param criteria Map of property name to value for filtering
     * @return List of statistics entities matching the criteria
     */
    fun findAllByCriteria(criteria: Map<String, Any>): List<T>
    
    /**
     * Find valid statistics (not marked as invalid) matching the criteria
     *
     * @param criteria Map of property name to value for filtering
     * @return List of valid statistics entities matching the criteria
     */
    fun findValidByCriteria(criteria: Map<String, Any>): List<T>
    
    /**
     * Save a statistics entity
     *
     * @param entity The entity to save
     * @return The saved entity with any generated metadata (e.g., IDs, timestamps)
     */
    fun save(entity: T): T
    
    /**
     * Save a statistics entity while explicitly recording the user who performed the action
     *
     * @param entity The entity to save
     * @param userId The ID of the user performing the operation
     * @return The saved entity with updated metadata
     */
    fun saveWithAudit(entity: T, userId: UUID): T
    
    /**
     * Mark a statistics entity as invalid
     *
     * @param id The unique identifier of the statistics
     * @param reason The reason for invalidation
     * @param userId The ID of the user performing the invalidation
     * @return True if successful, false otherwise
     */
    fun invalidate(id: UUID, reason: String, userId: UUID): Boolean
    
    /**
     * Retrieve the version history of a statistics entity
     *
     * @param id The unique identifier of the statistics
     * @return List of version metadata (timestamps, users, etc.)
     */
    fun getVersionHistory(id: UUID): List<Map<String, Any>>
    
    /**
     * Get statistics entities for specific wards
     *
     * @param wardIds List of ward identifiers
     * @return Map of ward ID to statistics entity
     */
    fun findByWardIds(wardIds: List<UUID>): Map<UUID, T>
    
    /**
     * Get count of statistics entities matching criteria
     *
     * @param criteria Map of property name to value for filtering
     * @return Count of matching entities
     */
    fun countByCriteria(criteria: Map<String, Any>): Long
    
    /**
     * Get statistics with pagination
     *
     * @param criteria Map of property name to value for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Property to sort by
     * @param sortDirection Direction of sort (ASC/DESC)
     * @return List of statistics entities for the requested page
     */
    fun findWithPagination(
        criteria: Map<String, Any>, 
        page: Int, 
        size: Int, 
        sortBy: String = "calculationDate", 
        sortDirection: String = "DESC"
    ): List<T>
}
