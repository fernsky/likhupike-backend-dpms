package np.sthaniya.dpis.statistics.domain.model

import np.sthaniya.dpis.statistics.domain.entity.BaseEntity
import np.sthaniya.dpis.statistics.domain.entity.SoftDeletableEntity
import np.sthaniya.dpis.statistics.domain.capability.EventSourcingCapable
import np.sthaniya.dpis.statistics.domain.capability.Cacheable
import np.sthaniya.dpis.statistics.domain.capability.AnalyticsTrackable
import np.sthaniya.dpis.statistics.domain.capability.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Base class for all statistical entities in the system.
 * 
 * Provides common attributes and behaviors for statistics including:
 * - Event sourcing for audit trail and replay capability
 * - Caching for performance optimization
 * - Analytics tracking for usage patterns
 */
abstract class BaseStatistics : 
    SoftDeletableEntity(),
    EventSourcingCapable,
    Cacheable,
    AnalyticsTrackable {

    /**
     * Statistical group this statistic belongs to (e.g., "demographics", "economics", "education")
     */
    var statisticalGroup: String = ""
    
    /**
     * Specific statistical category within the group
     */
    var statisticalCategory: String = ""
    
    /**
     * Reference date or period for the statistics
     */
    var referenceDate: LocalDateTime? = null
    
    /**
     * Date when the statistics were last calculated or updated
     */
    var calculationDate: LocalDateTime = LocalDateTime.now()
    
    /**
     * Entity that initiated the calculation or update
     */
    var calculatedBy: UUID? = null
    
    /**
     * Version of the calculation methodology used
     */
    var methodologyVersion: String = "1.0"
    
    /**
     * Whether the data is an estimate or final
     */
    var isEstimate: Boolean = false
    
    /**
     * Confidence score for statistical validity (0-100)
     */
    var confidenceScore: Int? = null
    
    /**
     * Source of the data used for calculation
     */
    var dataSource: String? = null
    
    /**
     * Time when the statistic was last cached
     */
    override var lastCachedAt: LocalDateTime? = null
    
    /**
     * Cache expiration policy
     */
    override var cacheExpirationPolicy: String = "TIME_BASED"
    
    /**
     * Time-to-live in seconds for cached data
     */
    override var cacheTTLSeconds: Long = 3600 // Default 1 hour

    /**
     * Determines if the statistics are valid and can be publicly displayed
     */
    var isValid: Boolean = true
    
    /**
     * Validation notes explaining any data quality issues
     */
    var validationNotes: String? = null

    // Event sourcing fields
    private var lastEventSequence: Long = 0
    private var events: MutableList<DomainEvent> = mutableListOf()
    
    /**
     * Get the events that should be produced when this statistic is updated
     */
    override fun getProducedEvents(): List<Any> {
        // Implement in concrete subclasses
        return emptyList()
    }

    /**
     * Get a unique key for caching this statistical entity
     */
    override fun getCacheKey(): String {
        return "${this.javaClass.simpleName}:${this.id}:${this.calculationDate}"
    }
    
    /**
     * Track analytics event when this statistical entity is accessed
     */
    override fun trackAnalytics(action: String, metadata: Map<String, Any>) {
        // Will be implemented by the analytics service
    }
    
    /**
     * Implementation of EventSourcingCapable interface
     */
    override fun getEventStream(): List<DomainEvent> {
        return events.toList()
    }
    
    override fun applyEvent(event: DomainEvent) {
        // Apply event based on type - implement in concrete subclasses
        lastEventSequence = event.sequenceNumber
        // Add event to local list
        events.add(event)
    }
    
    override fun getCurrentState(): Map<String, Any?> {
        val state = mutableMapOf<String, Any?>()
        state["id"] = this.id
        state["statisticalGroup"] = this.statisticalGroup
        state["statisticalCategory"] = this.statisticalCategory
        state["referenceDate"] = this.referenceDate?.toString()
        state["calculationDate"] = this.calculationDate.toString()
        state["calculatedBy"] = this.calculatedBy
        state["methodologyVersion"] = this.methodologyVersion
        state["isEstimate"] = this.isEstimate
        state["confidenceScore"] = this.confidenceScore
        state["dataSource"] = this.dataSource
        state["isValid"] = this.isValid
        state["validationNotes"] = this.validationNotes
        state["lastEventSequence"] = this.lastEventSequence
        
        return state
    }
    
    override fun rebuildFromEvents(events: List<DomainEvent>) {
        // Reset state
        this.statisticalGroup = ""
        this.statisticalCategory = ""
        this.referenceDate = null
        this.calculationDate = LocalDateTime.now()
        this.calculatedBy = null
        this.methodologyVersion = "1.0"
        this.isEstimate = false
        this.confidenceScore = null
        this.dataSource = null
        this.isValid = true
        this.validationNotes = null
        this.lastEventSequence = 0
        
        // Apply all events in sequence
        for (event in events.sortedBy { it.sequenceNumber }) {
            applyEvent(event)
        }
    }
    
    override fun getLastEventSequence(): Long {
        return this.lastEventSequence
    }
    
    /**
     * Include statistical attributes in hashable fields
     */
    override fun getHashableFields(): List<Any?> {
        return super.getHashableFields() + listOf(
            statisticalGroup,
            statisticalCategory,
            referenceDate,
            calculationDate,
            methodologyVersion,
            isEstimate,
            confidenceScore,
            isValid
        )
    }
}

/**
 * Usage Example:
 * 
 * The BaseStatistics class serves as the foundation for all statistical aggregates in the system.
 * Here's a real-world example of how it's utilized to track demographic statistics for a ward:
 * 
 * ```
 * // Create a ward demographic statistic entity
 * val wardDemographics = WardDemographicStatistics().apply {
 *     // Core statistical metadata
 *     statisticalGroup = "demographics"
 *     statisticalCategory = "ward_population"
 *     methodologyVersion = "2.0"
 *     referenceDate = LocalDateTime.of(2023, 1, 1, 0, 0)
 *     calculatedBy = currentUserId
 *     
 *     // Ward-specific attributes
 *     wardId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
 *     wardNumber = 5
 *     
 *     // Actual statistical data
 *     totalPopulation = 2750
 *     populationByGender = PopulationBreakdown(
 *         male = 1325,
 *         female = 1410,
 *         other = 15
 *     )
 *     totalHouseholds = 575
 *     averageHouseholdSize = BigDecimal("4.8")
 *     populationDensity = BigDecimal("856.2")
 *     
 *     // Calculated metrics
 *     calculateSexRatio()
 *     isValid = true
 * }
 * 
 * // The entity can then be persisted and automatically benefits from:
 * // 1. Event sourcing - each change is recorded as an event
 * // 2. Caching - statistics are cached for fast retrieval
 * // 3. Analytics tracking - usage patterns are tracked
 * 
 * // When a user accesses these statistics:
 * wardDemographics.trackAnalytics("VIEW", mapOf(
 *     "userId" to userId.toString(),
 *     "accessChannel" to "WEB_PORTAL",
 *     "accessTime" to LocalDateTime.now().toString()
 * ))
 * 
 * // For a significant update:
 * wardDemographics.apply {
 *     totalPopulation = 2850  // Population increased
 *     populationByGender = PopulationBreakdown(
 *         male = 1375,
 *         female = 1460,
 *         other = 15
 *     )
 *     calculationDate = LocalDateTime.now()
 *     calculatedBy = currentUserId
 *     isEstimate = false
 *     dataSource = "Annual Census"
 *     
 *     // Recalculate derived values
 *     calculateSexRatio()
 * }
 * 
 * // This update triggers events that can be consumed by other bounded contexts
 * val producedEvents = wardDemographics.getProducedEvents()
 * eventPublisher.publishAll(producedEvents)
 * 
 * // The statistics can be invalidated if the source data is found to be incorrect
 * if (dataQualityIssueDetected) {
 *     wardDemographics.apply {
 *         isValid = false
 *         validationNotes = "Data quality issues identified in source data"
 *     }
 * }
 * 
 * // Auditing: We can see who created and modified the statistics when
 * val auditInfo = mapOf(
 *     "created" to wardDemographics.createdAt.toString(),
 *     "createdBy" to wardDemographics.createdBy.toString(),
 *     "lastUpdated" to wardDemographics.updatedAt.toString(),
 *     "lastUpdatedBy" to wardDemographics.updatedBy.toString(),
 *     "validationStatus" to wardDemographics.isValid.toString(),
 *     "lastCalculation" to wardDemographics.calculationDate.toString()
 * )
 * 
 * // Event sourcing allows for rebuilding the state at any point in time
 * val historicalEvents = eventStore.getEventsForEntity(wardDemographics.id, asOf = someHistoricalDate)
 * val historicalState = WardDemographicStatistics().apply {
 *     rebuildFromEvents(historicalEvents)
 * }
 * ```
 * 
 * This example demonstrates how the BaseStatistics class enables tracking demographic data
 * for a ward while providing comprehensive auditing, validation, and event production capabilities.
 */
