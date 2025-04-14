package np.sthaniya.dpis.statistics.domain.repository

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import java.time.LocalDateTime
import java.util.UUID

/**
 * Repository interface for ward-level statistics entities.
 * 
 * Extends the base statistics repository with ward-specific functionality,
 * allowing for more targeted statistical data access and manipulation.
 */
interface WardStatisticsRepository<T : WardStatistics> : StatisticsRepository<T> {
    
    /**
     * Find statistics for a specific ward by ward ID
     *
     * @param wardId The unique identifier of the ward
     * @return The statistics entity for the ward if found, null otherwise
     */
    fun findByWardId(wardId: UUID): T?
    
    /**
     * Find statistics for a specific ward as they existed at a point in time
     *
     * @param wardId The unique identifier of the ward
     * @param asOfDate The historical point in time
     * @return The statistics entity as it existed at the specified time, null if not found
     */
    fun findByWardIdAsOf(wardId: UUID, asOfDate: LocalDateTime): T?
    
    /**
     * Find statistics by ward number
     *
     * @param wardNumber The ward number (user-friendly identifier)
     * @return The statistics entity for the ward if found, null otherwise
     */
    fun findByWardNumber(wardNumber: Int): T?
    
    /**
     * Find all ward statistics for a given municipality
     *
     * @param municipalityId The unique identifier of the municipality
     * @return List of ward statistics belonging to the municipality
     */
    fun findAllByMunicipality(municipalityId: UUID): List<T>
    
    /**
     * Find ward statistics that have been updated since a given date
     *
     * @param since The date threshold
     * @return List of recently updated ward statistics
     */
    fun findUpdatedSince(since: LocalDateTime): List<T>
    
    /**
     * Find the top N wards based on the comparison value of their statistics
     *
     * @param n Number of top wards to return
     * @param ascending Whether to sort in ascending order (default: false = descending)
     * @return List of ward statistics sorted by comparison value
     */
    fun findTopNByComparisonValue(n: Int, ascending: Boolean = false): List<T>
    
    /**
     * Get statistics for wards with population greater than the specified threshold
     *
     * @param populationThreshold The minimum population
     * @return List of ward statistics for wards exceeding the population threshold
     */
    fun findByPopulationGreaterThan(populationThreshold: Int): List<T>
    
    /**
     * Find statistics for wards that match a geographic criteria
     *
     * @param geoCriteria Map of geographic attributes to filter by
     * @return List of ward statistics matching the geographic criteria
     */
    fun findByGeographicCriteria(geoCriteria: Map<String, Any>): List<T>
    
    /**
     * Calculate aggregated statistics across multiple wards
     *
     * @param wardIds The IDs of the wards to include in the aggregation
     * @return Aggregated statistics result
     */
    fun calculateAggregateForWards(wardIds: List<UUID>): Map<String, Any>
    
    /**
     * Compare statistics between two wards
     *
     * @param wardId1 First ward ID
     * @param wardId2 Second ward ID
     * @return Comparison result with key metrics
     */
    fun compareWards(wardId1: UUID, wardId2: UUID): Map<String, Any>
}
