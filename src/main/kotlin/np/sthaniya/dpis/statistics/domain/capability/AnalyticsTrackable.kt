package np.sthaniya.dpis.statistics.domain.capability

/**
 * Interface for entities that collect and provide analytics data.
 */
interface AnalyticsTrackable {
    /**
     * Track an analytics event about this entity
     * 
     * @param action The type of action being tracked
     * @param metadata Additional data about the action
     */
    fun trackAnalytics(action: String, metadata: Map<String, Any>)
    
    /**
     * Get the event history for this entity
     * 
     * @param filter Filter criteria for events
     * @return List of analytics events matching the filter
     */
    fun getEventHistory(filter: AnalyticsFilter): List<AnalyticsEvent> {
        // Default implementation returns empty list
        // To be implemented by concrete classes or services
        return emptyList()
    }
    
    /**
     * Get metrics for this entity
     * 
     * @param metricType Type of metrics to retrieve
     * @param timeframe Time period for the metrics
     * @return Map of metric names to values
     */
    fun getMetrics(metricType: String, timeframe: String): Map<String, Any> {
        // Default implementation returns empty map
        // To be implemented by concrete classes or services
        return emptyMap()
    }
    
    /**
     * Get performance indicators for this entity
     * 
     * @return List of performance indicators
     */
    fun getPerformanceIndicators(): List<PerformanceIndicator> {
        // Default implementation returns empty list
        // To be implemented by concrete classes or services
        return emptyList()
    }
    
    /**
     * Get usage statistics for this entity
     * 
     * @param timeframe Time period for the statistics
     * @return Usage statistics data
     */
    fun getUsageStatistics(timeframe: String): Map<String, Any> {
        // Default implementation returns empty map
        // To be implemented by concrete classes or services
        return emptyMap()
    }
    
    /**
     * Get trend data for this entity
     * 
     * @param metricType Type of metric to trend
     * @param timeframe Time period for the trend
     * @return List of trend points
     */
    fun getTrends(metricType: String, timeframe: String): List<TrendPoint> {
        // Default implementation returns empty list
        // To be implemented by concrete classes or services
        return emptyList()
    }
}

/**
 * Filter for analytics events
 */
data class AnalyticsFilter(
    val timeRange: TimeRange? = null,
    val eventTypes: Set<String> = emptySet(),
    val metadataFilters: Map<String, String> = emptyMap()
)

/**
 * Time range for filtering
 */
data class TimeRange(
    val start: java.time.LocalDateTime? = null,
    val end: java.time.LocalDateTime? = null
)

/**
 * Analytics event data
 */
interface AnalyticsEvent {
    val eventType: String
    val timestamp: java.time.LocalDateTime
    val metadata: Map<String, Any>
}

/**
 * Performance indicator for analytics
 */
data class PerformanceIndicator(
    val name: String,
    val value: Double,
    val comparisonValue: Double? = null,
    val trend: String = "STABLE",
    val status: String = "NORMAL",
    val category: String
)

/**
 * Trend point for analytics
 */
data class TrendPoint(
    val label: String,
    val value: Double,
    val timestamp: java.time.LocalDateTime,
    val metadata: Map<String, Any> = emptyMap()
)
