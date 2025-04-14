package np.sthaniya.dpis.statistics.domain.capability

import java.util.UUID

/**
 * Interface for entities supporting event sourcing pattern.
 * 
 * Enables entities to be rebuilt from their history of events.
 */
interface EventSourcingCapable {
    /**
     * Get the complete event stream for this entity
     */
    fun getEventStream(): List<DomainEvent>
    
    /**
     * Apply an event to update the entity's state
     */
    fun applyEvent(event: DomainEvent)
    
    /**
     * Get the current state of the entity as a map
     */
    fun getCurrentState(): Map<String, Any?>
    
    /**
     * Rebuild the entity state from a list of events
     */
    fun rebuildFromEvents(events: List<DomainEvent>)
    
    /**
     * Get the sequence number of the last event applied to this entity
     */
    fun getLastEventSequence(): Long
    
    /**
     * Get events that should be produced when this entity is updated
     */
    fun getProducedEvents(): List<Any>
}

/**
 * Base domain event interface
 */
interface DomainEvent {
    val eventId: UUID
    val entityId: UUID
    val eventType: String
    val timestamp: Long
    val sequenceNumber: Long
    val userId: UUID?
    val metadata: Map<String, Any?>
}
