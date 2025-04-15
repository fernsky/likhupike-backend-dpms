package np.sthaniya.dpis.statistics.infrastructure.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import np.sthaniya.dpis.statistics.domain.capability.EventSourcingCapable
import np.sthaniya.dpis.statistics.domain.capability.DomainEvent
import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import np.sthaniya.dpis.statistics.domain.model.BaseStatistics
import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.StatisticsEventEntity
import np.sthaniya.dpis.statistics.infrastructure.persistence.repository.JpaStatisticsEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

/**
 * Service responsible for implementing event sourcing functionality for statistics.
 * Handles storing, retrieving, and rebuilding entities from events.
 */
@Service
class EventSourcingService(
    private val eventRepository: JpaStatisticsEventRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(EventSourcingService::class.java)
    
    /**
     * Save domain events to the event store
     *
     * @param entity The entity that produced the events
     * @param events The events to save
     * @param userId The ID of the user who initiated the action
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveEvents(entity: StatisticsEventEntity, events: List<Any>, userId: UUID?) {
        if (events.isEmpty()) return
        
        val lastSequenceNumber = eventRepository.findLastSequenceNumber(entity.id) ?: 0L
        
        events.forEachIndexed { index, event ->
            if (event !is StatisticsEvent) {
                logger.warn("Skipping non-statistics event: ${event::class.simpleName}")
                return@forEachIndexed
            }
            
            try {
                val eventEntity = StatisticsEventEntity(
                    id = UUID.randomUUID(),
                    entityId = entity.id,
                    entityType = entity::class.simpleName ?: "Unknown",
                    eventType = event::class.simpleName ?: "Unknown",
                    eventData = objectMapper.writeValueAsString(event),
                    sequenceNumber = lastSequenceNumber + index + 1,
                    createdAt = LocalDateTime.now(),
                    createdBy = userId
                )
                
                eventRepository.save(eventEntity)
                logger.debug("Saved event ${eventEntity.eventType} with sequence number ${eventEntity.sequenceNumber}")
            } catch (e: Exception) {
                logger.error("Failed to save event ${event::class.simpleName}", e)
                throw e
            }
        }
    }
    
    /**
     * Retrieve events for an entity
     *
     * @param entityId The entity ID
     * @param fromSequence The starting sequence number (inclusive)
     * @param toSequence The ending sequence number (inclusive), null for all subsequent events
     * @return List of domain events
     */
    fun getEvents(entityId: UUID, fromSequence: Long = 1, toSequence: Long? = null): List<DomainEvent> {
        val eventEntities = if (toSequence == null) {
            eventRepository.findByEntityIdAndSequenceNumberGreaterThanEqualOrderBySequenceNumberAsc(entityId, fromSequence)
        } else {
            eventRepository.findByEntityIdAndSequenceNumberBetweenOrderBySequenceNumberAsc(entityId, fromSequence, toSequence)
        }
        
        return eventEntities.mapNotNull { eventEntity ->
            try {
                deserializeEvent(eventEntity)
            } catch (e: Exception) {
                logger.error("Failed to deserialize event: ${eventEntity.id}", e)
                null
            }
        }
    }
    
    /**
     * Retrieve events for an entity up to a specific point in time
     *
     * @param entityId The entity ID
     * @param asOfDate The cutoff date
     * @return List of domain events
     */
    fun getEventsAsOf(entityId: UUID, asOfDate: LocalDateTime): List<DomainEvent> {
        val eventEntities = eventRepository.findByEntityIdAndCreatedAtBeforeOrderBySequenceNumberAsc(entityId, asOfDate)
        
        return eventEntities.mapNotNull { eventEntity ->
            try {
                deserializeEvent(eventEntity)
            } catch (e: Exception) {
                logger.error("Failed to deserialize event: ${eventEntity.id}", e)
                null
            }
        }
    }
    
    /**
     * Rebuild an entity from events up to a specific point in time
     *
     * @param entity Empty entity instance to rebuild
     * @param asOfDate The cutoff date
     * @return The rebuilt entity
     */
    fun <T : BaseStatistics> rebuildEntity(entity: T, asOfDate: LocalDateTime): T {
        val events = getEventsAsOf(entity.id, asOfDate)
        if (events.isEmpty()) {
            logger.warn("No events found for entity: ${entity.id}")
            return entity
        }
        
        entity.rebuildFromEvents(events)
        logger.debug("Rebuilt entity ${entity.id} from ${events.size} events up to $asOfDate")
        
        return entity
    }
    
    /**
     * Deserialize an event entity to a domain event
     */
    private fun deserializeEvent(eventEntity: StatisticsEventEntity): DomainEvent? {
        return try {
            // To properly deserialize, we need to know the exact type
            val eventType = Class.forName("np.sthaniya.dpis.statistics.domain.event.${eventEntity.eventType}")
            val event = objectMapper.readValue(eventEntity.eventData, eventType)
            
            if (event is DomainEvent) {
                event
            } else {
                logger.warn("Deserialized event is not a DomainEvent: ${eventEntity.eventType}")
                null
            }
        } catch (e: ClassNotFoundException) {
            logger.error("Failed to find event class: ${eventEntity.eventType}", e)
            null
        }
    }
}
