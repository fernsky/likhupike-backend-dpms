package np.sthaniya.dpis.statistics.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entity for storing audit trail records in the database.
 */
@Entity
@Table(name = "statistics_audit_trail", indexes = [
    Index(name = "idx_entity_id", columnList = "entityId"),
    Index(name = "idx_entity_type", columnList = "entityType"),
    Index(name = "idx_user_id", columnList = "userId"),
    Index(name = "idx_action", columnList = "action"),
    Index(name = "idx_timestamp", columnList = "timestamp")
])
class AuditTrailEntity {
    
    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID()
    
    @Column(name = "entity_id", nullable = false)
    var entityId: UUID? = null
    
    @Column(name = "entity_type", nullable = false)
    var entityType: String = ""
    
    @Column(name = "action", nullable = false)
    var action: String = ""
    
    @Column(name = "user_id")
    var userId: UUID? = null
    
    @Column(name = "timestamp", nullable = false)
    var timestamp: LocalDateTime = LocalDateTime.now()
    
    @Column(name = "details")
    var details: String? = null
    
    @Column(name = "old_state", columnDefinition = "jsonb")
    var oldState: String? = null
    
    @Column(name = "new_state", columnDefinition = "jsonb")
    var newState: String? = null
    
    @Column(name = "ip_address")
    var ipAddress: String? = null
    
    @Column(name = "user_agent")
    var userAgent: String? = null
    
    @Column(name = "is_sensitive", nullable = false)
    var isSensitive: Boolean = false
}
