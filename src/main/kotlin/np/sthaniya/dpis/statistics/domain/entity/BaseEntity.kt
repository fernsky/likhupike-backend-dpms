package np.sthaniya.dpis.statistics.domain.entity

import java.time.Instant
import java.util.UUID

/**
 * Base entity class providing common attributes for all entities in the statistics domain.
 */
abstract class BaseEntity {
    val id: UUID = UUID.randomUUID()
    
    val createdAt: Instant = Instant.now()
    
    var createdBy: UUID? = null
    
    var updatedAt: Instant? = null
    
    var updatedBy: UUID? = null
    
    var version: Long = 0
    
    var entityHash: String? = null
    
    var origin: String? = null
    
    var externalId: String? = null
    
    var tenant: UUID? = null
    
    /**
     * Calculate a hash of the entity for integrity verification
     */
    fun calculateHash(): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashableFields = this.getHashableFields()
        return digest.digest(hashableFields.toString().toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Get fields that should be included in the entity hash
     */
    protected open fun getHashableFields(): List<Any?> {
        return listOf(id, version)
    }
    
    /**
     * Update entity metadata when the entity is modified
     */
    fun markUpdated(userId: UUID?) {
        this.updatedAt = Instant.now()
        this.updatedBy = userId
        this.version++
        this.entityHash = calculateHash()
    }
}
