package np.sthaniya.dpis.statistics.domain.repository

import np.sthaniya.dpis.statistics.domain.capability.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Interface for storing and retrieving domain events.
 * 
 * Provides event sourcing capabilities, allowing for the complete 
 * reconstruction of entity state from the sequence of events that affected it.
 */
interface EventStore {
    
    /**
     * Store a new domain event
     *
     * @param event The domain event to store
     * @return The stored event with any generated metadata (e.g., sequence number)
     */
    fun <T : DomainEvent> store(event: T): T
    
    /**
     * Store multiple domain events in a transaction
     *
     * @param events List of domain events to store
     * @return List of stored events with generated metadata
     */
    fun <T : DomainEvent> storeAll(events: List<T>): List<T>
    
    /**
     * Get all events for a specific entity
     *
     * @param entityId The unique identifier of the entity
     * @return List of events ordered by sequence number
     */
    fun getEventsForEntity(entityId: UUID): List<DomainEvent>
    
    /**
     * Get events for an entity as they existed at a specific point in time
     *
     * @param entityId The unique identifier of the entity
     * @param asOfDate The historical point in time
     * @return List of events ordered by sequence number up to the specified time
     */
    fun getEventsForEntityAsOf(entityId: UUID, asOfDate: LocalDateTime): List<DomainEvent>
    
    /**
     * Get events by type
     *
     * @param eventType The class name of the event type
     * @param limit Maximum number of events to return
     * @param offset Offset for pagination
     * @return List of events of the specified type
     */
    fun getEventsByType(eventType: String, limit: Int = 100, offset: Int = 0): List<DomainEvent>
    
    /**
     * Get events by entity type
     *
     * @param entityType The class name of the entity type
     * @param limit Maximum number of events to return
     * @param offset Offset for pagination
     * @return List of events for entities of the specified type
     */
    fun getEventsByEntityType(entityType: String, limit: Int = 100, offset: Int = 0): List<DomainEvent>
    
    /**
     * Get events within a time range
     *
     * @param startTime Start of the time range
     * @param endTime End of the time range
     * @param limit Maximum number of events to return
     * @param offset Offset for pagination
     * @return List of events within the specified time range
     */
    fun getEventsBetweenDates(
        startTime: LocalDateTime, 
        endTime: LocalDateTime, 
        limit: Int = 100, 
        offset: Int = 0
    ): List<DomainEvent>
    
    /**
     * Count events for a specific entity
     *
     * @param entityId The unique identifier of the entity
     * @return Count of events for the entity
     */
    fun countEventsForEntity(entityId: UUID): Long
    
    /**
     * Get latest event for an entity
     *
     * @param entityId The unique identifier of the entity
     * @return The most recent event for the entity, or null if none found
     */
    fun getLatestEventForEntity(entityId: UUID): DomainEvent?
    
    /**
     * Get events by user ID (who triggered the event)
     *
     * @param userId The unique identifier of the user
     * @param limit Maximum number of events to return
     * @param offset Offset for pagination
     * @return List of events triggered by the specified user
     */
    fun getEventsByUserId(userId: UUID, limit: Int = 100, offset: Int = 0): List<DomainEvent>
    
    /**
     * Search events by payload content
     *
     * @param searchQuery The search query for the event payload
     * @param limit Maximum number of events to return
     * @param offset Offset for pagination
     * @return List of events matching the search query
     */
    fun searchEvents(searchQuery: String, limit: Int = 100, offset: Int = 0): List<DomainEvent>
}
