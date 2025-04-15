package np.sthaniya.dpis.statistics.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entity for storing domain events in the database.
 * Supports event-sourcing capabilities of the statistics module.
 */
@Entity
@Table(name = "statistics_events", indexes = [
    Index(name = "idx_entity_id", columnList = "entityId"),
    Index(name = "idx_entity_id_seq", columnList = "entityId,sequenceNumber"),
    Index(name = "idx_event_type", columnList = "eventType"),
    Index(name = "idx_created_at", columnList = "createdAt")
])
data class StatisticsEventEntity(
    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID(),
    
    @Column(name = "entity_id", nullable = false)
    var entityId: UUID? = null,
    
    @Column(name = "entity_type", nullable = false)
    var entityType: String = "",
    
    @Column(name = "event_type", nullable = false)
    var eventType: String = "",
    
    @Column(name = "sequence_number", nullable = false)
    var sequenceNumber: Long = 0,
    
    @Column(name = "event_data", columnDefinition = "text", nullable = false)
    var eventData: String = "{}",
    
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "created_by")
    var createdBy: UUID? = null,
    
    @Column(name = "metadata", columnDefinition = "text")
    var metadata: String = "{}",
    
    @Column(name = "is_processed", nullable = false)
    var isProcessed: Boolean = false,
    
    @Column(name = "processed_at")
    var processedAt: LocalDateTime? = null
)
