package np.sthaniya.dpis.statistics.domain.service.demographics

import np.sthaniya.dpis.statistics.domain.model.demographics.WardWiseAbsenteeStatistics
import np.sthaniya.dpis.statistics.domain.service.StatisticsService
import np.sthaniya.dpis.statistics.domain.service.ValidationIssue
import np.sthaniya.dpis.statistics.domain.service.QualityAssessment
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeAgeGroup
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeData
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.vo.demographic.EducationalLevelType
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Service interface for absentee statistics calculations and operations.
 */
interface AbsenteeStatisticsService : StatisticsService<WardWiseAbsenteeStatistics> {

    /**
     * Calculate absentee statistics for a specific ward
     *
     * @param wardId ID of the ward
     * @param wardNumber Ward number
     * @param totalPopulation Total ward population
     * @param absenteeData Detailed absentee data
     * @param referenceDate Reference date for the statistics
     * @param calculatedBy User who performed the calculation
     * @return The calculated absentee statistics
     */
    fun calculateForWard(
        wardId: UUID,
        wardNumber: Int,
        totalPopulation: Int,
        absenteeData: AbsenteeData,
        referenceDate: LocalDateTime = LocalDateTime.now(),
        calculatedBy: UUID? = null
    ): WardWiseAbsenteeStatistics
    
    /**
     * Find wards with significant foreign absentee population
     *
     * @param thresholdPercentage Minimum percentage to qualify as significant
     * @return List of ward statistics with significant foreign absentee population
     */
    fun findWardsWithSignificantForeignAbsentees(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards where specified absence reason is dominant
     *
     * @param reason The absence reason to look for
     * @return List of ward statistics where the specified reason is dominant
     */
    fun findWardsWithDominantAbsenceReason(reason: AbsenceReason): List<WardWiseAbsenteeStatistics>
    
    /**
     * Find wards with significant gender imbalance in absentee population
     *
     * @param ratioThreshold The sex ratio threshold to consider as imbalanced
     * @return List of ward statistics with significant gender imbalance
     */
    fun findWardsWithGenderImbalance(ratioThreshold: BigDecimal): List<WardWiseAbsenteeStatistics>
    
    /**
     * Get breakdown of absentee destination countries across all wards
     *
     * @return Map of countries to absentee counts
     */
    fun getDestinationCountriesBreakdown(): Map<String, Int>
    
    /**
     * Get most common reasons for absence across wards
     *
     * @param limit Maximum number of reasons to return
     * @return Map of reasons to percentage of total absentees
     */
    fun getTopAbsenceReasons(limit: Int = 5): Map<AbsenceReason, Double>
    
    /**
     * Get wards with the highest education level among absentees
     * 
     * @param limit Number of wards to return
     * @return List of ward statistics with the highest education level
     */
    fun getWardsWithHighestAbsenteeEducation(limit: Int = 10): List<WardWiseAbsenteeStatistics>
    
    /**
     * Generate a comprehensive report of absentee population trends
     *
     * @param municipalityId Optional municipality ID to filter results
     * @param asOfDate Reference date for historical comparison
     * @return Map containing the report data
     */
    fun generateAbsenteeTrendsReport(
        municipalityId: UUID? = null,
        asOfDate: LocalDateTime = LocalDateTime.now()
    ): Map<String, Any>
}
