package np.sthaniya.dpis.statistics.domain.repository

import java.time.LocalDateTime
import java.util.UUID

/**
 * Repository interface for managing audit trail records.
 * 
 * Tracks all user-initiated actions on statistical data,
 * supporting compliance requirements and providing visibility into changes.
 */
interface AuditTrailRepository {
    
    /**
     * Record an audit entry for an action on statistical data
     *
     * @param entityId The ID of the affected entity
     * @param entityType The type of the affected entity
     * @param action The action performed (e.g., CREATE, UPDATE, DELETE, INVALIDATE)
     * @param userId The ID of the user who performed the action
     * @param timestamp The time when the action occurred
     * @param details Additional details about the action
     * @param oldState The state before the action (optional)
     * @param newState The state after the action (optional)
     * @return The ID of the created audit entry
     */
    fun recordAudit(
        entityId: UUID,
        entityType: String,
        action: String,
        userId: UUID,
        timestamp: LocalDateTime = LocalDateTime.now(),
        details: String? = null,
        oldState: Map<String, Any?>? = null,
        newState: Map<String, Any?>? = null
    ): UUID
    
    /**
     * Get audit entries for a specific entity
     *
     * @param entityId The ID of the entity
     * @return List of audit entries for the entity ordered by timestamp
     */
    fun getAuditTrailForEntity(entityId: UUID): List<Map<String, Any?>>
    
    /**
     * Get audit entries for a specific entity type
     *
     * @param entityType The type of entity
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of audit entries for the entity type
     */
    fun getAuditTrailForEntityType(
        entityType: String,
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
    
    /**
     * Get audit entries for a specific user
     *
     * @param userId The ID of the user
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of audit entries for the user
     */
    fun getAuditTrailForUser(
        userId: UUID,
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
    
    /**
     * Get audit entries for a specific action type
     *
     * @param action The action type
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of audit entries for the action type
     */
    fun getAuditTrailForAction(
        action: String,
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
    
    /**
     * Get audit entries within a time range
     *
     * @param startTime Start of the time range
     * @param endTime End of the time range
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of audit entries within the time range
     */
    fun getAuditTrailBetweenDates(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
    
    /**
     * Search audit entries by details or state content
     *
     * @param searchQuery The search query
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of audit entries matching the search query
     */
    fun searchAuditTrail(
        searchQuery: String,
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
    
    /**
     * Count audit entries for a specific entity
     *
     * @param entityId The ID of the entity
     * @return Count of audit entries for the entity
     */
    fun countAuditEntriesForEntity(entityId: UUID): Long
    
    /**
     * Get audit entries that represent sensitive operations
     * (e.g., data invalidation, mass updates)
     *
     * @param limit Maximum number of entries to return
     * @param offset Offset for pagination
     * @return List of sensitive audit entries
     */
    fun getSensitiveOperations(
        limit: Int = 100,
        offset: Int = 0
    ): List<Map<String, Any?>>
}
