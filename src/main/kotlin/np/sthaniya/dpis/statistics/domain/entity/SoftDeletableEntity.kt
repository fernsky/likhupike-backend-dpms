package np.sthaniya.dpis.statistics.domain.entity

import java.time.Instant
import java.util.UUID

/**
 * Abstract class extending BaseEntity with soft deletion capabilities.
 */
abstract class SoftDeletableEntity : BaseEntity() {
    var isDeleted: Boolean = false
    
    var deletedAt: Instant? = null
    
    var deletedBy: UUID? = null
    
    var deletionReason: String? = null
    
    var restoredAt: Instant? = null
    
    var restoredBy: UUID? = null
    
    /**
     * Soft delete this entity, marking it as deleted without removing from database
     */
    fun softDelete(userId: UUID, reason: String) {
        this.isDeleted = true
        this.deletedAt = Instant.now()
        this.deletedBy = userId
        this.deletionReason = reason
        this.markUpdated(userId)
    }
    
    /**
     * Restore a previously deleted entity
     */
    fun restore(userId: UUID, reason: String?) {
        this.isDeleted = false
        this.restoredAt = Instant.now()
        this.restoredBy = userId
        this.markUpdated(userId)
    }
    
    /**
     * Include deletion status in hashable fields
     */
    override fun getHashableFields(): List<Any?> {
        return super.getHashableFields() + listOf(isDeleted, deletedAt, deletionReason)
    }
}
