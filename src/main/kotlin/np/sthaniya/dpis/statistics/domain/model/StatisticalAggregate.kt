package np.sthaniya.dpis.statistics.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Interface for statistical aggregates that can be calculated from raw data
 */
interface StatisticalAggregate {
    
    /**
     * Unique identifier for this statistical aggregate
     */
    val id: UUID?
    
    /**
     * When this aggregate was last calculated
     */
    val calculationDate: LocalDateTime
    
    /**
     * Statistical group this aggregate belongs to
     */
    val statisticalGroup: String
    
    /**
     * Calculate or recalculate this aggregate from raw data
     * 
     * @return true if calculation was successful
     */
    fun calculate(): Boolean
    
    /**
     * Invalidate this aggregate, triggering recalculation
     */
    fun invalidate()
    
    /**
     * Check if this aggregate needs recalculation
     * 
     * @return true if recalculation is needed
     */
    fun needsRecalculation(): Boolean
    
    /**
     * Get the cache key for this aggregate
     */
    fun getCacheKey(): String
}

/**
 * Interface for time-series statistical aggregates
 */
interface TimeSeriesAggregate : StatisticalAggregate {

    /**
     * The time period this aggregate covers
     */
    val period: Any
    
    /**
     * Calculate aggregate for a specific time period
     */
    fun calculateForPeriod(period: Any): Boolean
    
    /**
     * Get historical values as a map of period to value
     */
    fun getTimeSeries(): Map<Any, Any>
    
    /**
     * Get the trend direction for this aggregate
     */
    fun getTrend(): TrendDirection
}

/**
 * Possible trend directions for time series data
 */
enum class TrendDirection {
    INCREASING,
    DECREASING,
    STABLE,
    FLUCTUATING,
    INSUFFICIENT_DATA
}

/**
 * Interface for spatially distributed statistics
 */
interface SpatialAggregate : StatisticalAggregate {
    
    /**
     * Spatial area this aggregate covers
     */
    val spatialArea: Any
    
    /**
     * Calculate aggregate for a specific spatial area
     */
    fun calculateForArea(area: Any): Boolean
    
    /**
     * Get distribution of values across different areas
     */
    fun getSpatialDistribution(): Map<Any, Any>
}

/**
 * Interface for demographic segment analysis
 */
interface DemographicSegmentAggregate : StatisticalAggregate {
    
    /**
     * Demographic segments this aggregate covers
     */
    val segments: List<Any>
    
    /**
     * Calculate aggregate for a specific demographic segment
     */
    fun calculateForSegment(segment: Any): Boolean
    
    /**
     * Get distribution of values across different segments
     */
    fun getSegmentDistribution(): Map<Any, Any>
    
    /**
     * Find segments with significant statistical differences
     */
    fun findSignificantDifferences(): List<Pair<Any, Any>>
}
