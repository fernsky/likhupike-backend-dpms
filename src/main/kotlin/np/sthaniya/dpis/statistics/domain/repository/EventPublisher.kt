package np.sthaniya.dpis.statistics.domain.repository

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * Interface for publishing domain events.
 * 
 * Enables loose coupling between event producers and consumers,
 * supporting asynchronous processing and event-driven architecture.
 */
interface EventPublisher {
    
    /**
     * Publish a single event
     *
     * @param event The event to publish
     * @return CompletableFuture that completes when the event is published
     */
    fun publish(event: StatisticsEvent): CompletableFuture<Void>
    
    /**
     * Publish multiple events
     *
     * @param events The events to publish
     * @return CompletableFuture that completes when all events are published
     */
    fun publishAll(events: List<StatisticsEvent>): CompletableFuture<Void>
    
    /**
     * Publish an event with delivery guarantee
     * This ensures the event is delivered at least once to all consumers
     *
     * @param event The event to publish
     * @return The ID of the published event
     */
    fun publishWithGuarantee(event: StatisticsEvent): UUID
    
    /**
     * Publish an event to a specific topic
     *
     * @param event The event to publish
     * @param topic The destination topic
     * @return CompletableFuture that completes when the event is published
     */
    fun publishToTopic(event: StatisticsEvent, topic: String): CompletableFuture<Void>
    
    /**
     * Check if a specific event was successfully published
     *
     * @param eventId The ID of the event
     * @return True if the event was successfully published, false otherwise
     */
    fun isEventPublished(eventId: UUID): Boolean
    
    /**
     * Get information about a published event
     *
     * @param eventId The ID of the event
     * @return Map containing information about the event publication
     */
    fun getPublishedEventInfo(eventId: UUID): Map<String, Any>?
    
    /**
     * Republish a failed event
     *
     * @param eventId The ID of the failed event
     * @return CompletableFuture that completes when the event is republished
     */
    fun republishEvent(eventId: UUID): CompletableFuture<Void>
    
    /**
     * Get statistics about published events
     *
     * @return Map of publishing statistics
     */
    fun getPublishingStatistics(): Map<String, Any>
    
    /**
     * Register an event publication listener
     *
     * @param listener The listener to register
     */
    fun registerPublicationListener(listener: EventPublicationListener)
    
    /**
     * Interface for event publication listeners
     */
    interface EventPublicationListener {
        /**
         * Called when an event is successfully published
         *
         * @param eventId The ID of the published event
         * @param event The published event
         */
        fun onSuccess(eventId: UUID, event: StatisticsEvent)
        
        /**
         * Called when an event publication fails
         *
         * @param eventId The ID of the failed event
         * @param event The event that failed to publish
         * @param exception The exception that caused the failure
         */
        fun onFailure(eventId: UUID, event: StatisticsEvent, exception: Throwable)
    }
}
