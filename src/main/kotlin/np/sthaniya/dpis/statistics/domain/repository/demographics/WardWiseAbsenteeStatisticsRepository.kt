package np.sthaniya.dpis.statistics.domain.repository.demographics

import np.sthaniya.dpis.statistics.domain.model.demographics.WardWiseAbsenteeStatistics
import np.sthaniya.dpis.statistics.domain.repository.WardStatisticsRepository
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Repository interface for ward-level absentee population statistics.
 */
interface WardWiseAbsenteeStatisticsRepository : WardStatisticsRepository<WardWiseAbsenteeStatistics> {
    
    /**
     * Find wards with absentee percentage above a threshold
     *
     * @param thresholdPercentage The minimum absentee percentage
     * @return List of ward statistics exceeding the threshold
     */
    fun findByAbsenteePercentageAbove(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards with a specific primary reason for absence
     *
     * @param reason The primary absence reason
     * @return List of ward statistics with the specified primary reason
     */
    fun findByPrimaryAbsenceReason(reason: AbsenceReason): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards with foreign absentee percentage above a threshold
     *
     * @param thresholdPercentage The minimum foreign absentee percentage
     * @return List of ward statistics exceeding the threshold
     */
    fun findByForeignAbsenteePercentageAbove(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards with a specific top destination country
     *
     * @param country The destination country
     * @return List of ward statistics with the specified top destination
     */
    fun findByTopDestinationCountry(country: String): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards with absentee gender imbalance above a threshold
     * (e.g., where male absentees greatly outnumber female absentees)
     *
     * @param ratio The minimum sex ratio to consider as imbalanced
     * @return List of ward statistics with the specified gender imbalance
     */
    fun findByAbsenteeGenderImbalance(ratio: BigDecimal): List<WardWiseAbsenteeStatistics>
    
    /**
     * Get aggregated absentee statistics by absence reason across all wards
     *
     * @return Map of absence reason to count
     */
    fun getAggregatedStatsByReason(): Map<AbsenceReason, Int>
    
    /**
     * Get aggregated absentee statistics by location type across all wards
     *
     * @return Map of location type to count
     */
    fun getAggregatedStatsByLocation(): Map<AbsenteeLocationType, Int>
    
    /**
     * Get aggregated absentee statistics by destination country across all wards
     *
     * @return Map of country to count
     */
    fun getAggregatedStatsByDestinationCountry(): Map<String, Int>
    
    /**
     * Get absentee trend data over time for a specific ward
     *
     * @param wardId The ID of the ward
     * @param startDate Start of the time range
     * @param endDate End of the time range
     * @return List of trend data points
     */
    fun getAbsenteeTrendData(
        wardId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Map<String, Any>>
    
    /**
     * Get comparison of absentee data by gender
     *
     * @return Map of gender to absentee count and percentage
     */
    fun getAbsenteeGenderComparison(): Map<Gender, Map<String, Any>>
    
    /**
     * Find wards where absentee population has increased beyond a threshold
     * compared to a previous reference period
     *
     * @param referenceDate Reference date for comparison
     * @param thresholdPercentage Minimum increase percentage to consider significant
     * @return List of ward statistics with significant increases
     */
    fun findWardsWithSignificantAbsenteeIncrease(
        referenceDate: LocalDateTime,
        thresholdPercentage: BigDecimal
    ): List<WardWiseAbsenteeStatistics>
    
    /**
     * Get cross-tabulation of absentee statistics by multiple dimensions
     *
     * @param dimensions List of dimensions to include (e.g., "ageGroup", "gender", "reason")
     * @return Cross-tabulated statistics
     */
    fun getCrossTabulation(dimensions: List<String>): Map<String, Any>
}
