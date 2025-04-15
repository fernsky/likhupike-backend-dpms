package np.sthaniya.dpis.statistics.infrastructure.service.demographics

import np.sthaniya.dpis.statistics.domain.model.demographics.WardWiseAbsenteeStatistics
import np.sthaniya.dpis.statistics.domain.service.QualityAssessment
import np.sthaniya.dpis.statistics.domain.service.ValidationIssue
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * Service for validating absentee statistics data and assessing its quality.
 */
@Service
class AbsenteeStatisticsValidationService {
    private val logger = LoggerFactory.getLogger(AbsenteeStatisticsValidationService::class.java)

    /**
     * Validate absentee statistics for consistency, completeness, and plausibility
     */
    fun validateAbsenteeStatistics(statistics: WardWiseAbsenteeStatistics): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()
        
        // Check for required fields
        if (statistics.wardId == null) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.MISSING_DATA,
                    field = "wardId",
                    message = "Ward ID is required",
                    severity = ValidationIssue.IssueSeverity.ERROR
                )
            )
        }
        
        if (statistics.wardNumber == null) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.MISSING_DATA,
                    field = "wardNumber",
                    message = "Ward number is required",
                    severity = ValidationIssue.IssueSeverity.ERROR
                )
            )
        }
        
        // Check for applicablePopulation
        if (statistics.applicablePopulation == null || statistics.applicablePopulation!! <= 0) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.INVALID_VALUE,
                    field = "applicablePopulation",
                    message = "Applicable population must be a positive number",
                    severity = ValidationIssue.IssueSeverity.ERROR
                )
            )
        }
        
        // Check total absentee population against applicable population
        if (statistics.applicablePopulation != null && 
            statistics.applicablePopulation!! > 0 && 
            statistics.absenteeData.totalAbsenteePopulation > statistics.applicablePopulation!!) {
            
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.LOGICAL_ERROR,
                    field = "totalAbsenteePopulation",
                    message = "Total absentee population cannot exceed total ward population",
                    severity = ValidationIssue.IssueSeverity.CRITICAL
                )
            )
        }
        
        // Check gender distribution sum
        val genderDistribution = statistics.absenteeData.getGenderDistribution()
        val totalByGender = genderDistribution.values.sum()
        
        if (totalByGender > 0 && totalByGender != statistics.absenteeData.totalAbsenteePopulation) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.INCONSISTENT_DATA,
                    field = "genderDistribution",
                    message = "Sum of gender distribution ($totalByGender) does not match total absentee population (${statistics.absenteeData.totalAbsenteePopulation})",
                    severity = ValidationIssue.IssueSeverity.WARNING
                )
            )
        }
        
        // Check reason distribution sum
        val totalByReason = statistics.absenteeData.reasonDistribution.values.sum()
        
        if (totalByReason > 0 && totalByReason != statistics.absenteeData.totalAbsenteePopulation) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.INCONSISTENT_DATA,
                    field = "reasonDistribution",
                    message = "Sum of reason distribution ($totalByReason) does not match total absentee population (${statistics.absenteeData.totalAbsenteePopulation})",
                    severity = ValidationIssue.IssueSeverity.WARNING
                )
            )
        }
        
        // Check location distribution sum
        val totalByLocation = statistics.absenteeData.locationDistribution.values.sum()
        
        if (totalByLocation > 0 && totalByLocation != statistics.absenteeData.totalAbsenteePopulation) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.INCONSISTENT_DATA,
                    field = "locationDistribution",
                    message = "Sum of location distribution ($totalByLocation) does not match total absentee population (${statistics.absenteeData.totalAbsenteePopulation})",
                    severity = ValidationIssue.IssueSeverity.WARNING
                )
            )
        }
        
        // Check for unusually high absentee percentage (e.g., > 75%)
        if (statistics.applicablePopulation != null && 
            statistics.applicablePopulation!! > 0 && 
            statistics.absenteePercentage > BigDecimal(75)) {
            
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.STATISTICAL_ANOMALY,
                    field = "absenteePercentage",
                    message = "Unusually high absentee percentage (${statistics.absenteePercentage}%)",
                    severity = ValidationIssue.IssueSeverity.WARNING
                )
            )
        }
        
        // Check for extreme gender imbalance (e.g., >95% male)
        val maleCount = genderDistribution[Gender.MALE] ?: 0
        val femaleCount = genderDistribution[Gender.FEMALE] ?: 0
        
        if (totalByGender > 0) {
            val malePercentage = (maleCount.toDouble() / totalByGender) * 100
            val femalePercentage = (femaleCount.toDouble() / totalByGender) * 100
            
            if (malePercentage > 95) {
                issues.add(
                    ValidationIssue(
                        type = ValidationIssue.IssueType.STATISTICAL_ANOMALY,
                        field = "genderDistribution",
                        message = "Extreme gender imbalance: $malePercentage% male",
                        severity = ValidationIssue.IssueSeverity.WARNING
                    )
                )
            } else if (femalePercentage > 95) {
                issues.add(
                    ValidationIssue(
                        type = ValidationIssue.IssueType.STATISTICAL_ANOMALY,
                        field = "genderDistribution",
                        message = "Extreme gender imbalance: $femalePercentage% female",
                        severity = ValidationIssue.IssueSeverity.WARNING
                    )
                )
            }
        }
        
        // Check if unknown reason is dominant
        val reasonPercentages = statistics.absenteeData.getReasonPercentages()
        val unknownPercentage = reasonPercentages[AbsenceReason.UNKNOWN]?.toDouble() ?: 0.0
        
        if (unknownPercentage > 50) {
            issues.add(
                ValidationIssue(
                    type = ValidationIssue.IssueType.DATA_QUALITY,
                    field = "reasonDistribution",
                    message = "High percentage of unknown reasons ($unknownPercentage%)",
                    severity = ValidationIssue.IssueSeverity.WARNING
                )
            )
        }
        
        return issues
    }

    /**
     * Assess quality of absentee statistics
     */
    fun assessQuality(statistics: WardWiseAbsenteeStatistics): QualityAssessment {
        val issues = validateAbsenteeStatistics(statistics)
        
        // Calculate completeness score
        val completeness = calculateCompletenessScore(statistics)
        
        // Calculate accuracy score
        val accuracy = calculateAccuracyScore(statistics, issues)
        
        // Calculate consistency score
        val consistency = calculateConsistencyScore(statistics, issues)
        
        // Calculate overall score (weighted average)
        val overallScore = (completeness * 0.4 + accuracy * 0.4 + consistency * 0.2) * 100
        val score = overallScore.toInt().coerceIn(0, 100)
        
        // Determine confidence level
        val confidenceLevel = when {
            score >= 80 -> "HIGH"
            score >= 60 -> "MEDIUM"
            else -> "LOW"
        }
        
        return QualityAssessment(
            score = score,
            confidenceLevel = confidenceLevel,
            completeness = completeness,
            accuracy = accuracy,
            consistency = consistency,
            issues = issues
        )
    }

    /**
     * Calculate completeness score (0.0-1.0)
     */
    private fun calculateCompletenessScore(statistics: WardWiseAbsenteeStatistics): Double {
        var score = 1.0
        val checks = mutableListOf<Boolean>()
        
        // Check for basic required fields
        checks.add(statistics.wardId != null)
        checks.add(statistics.wardNumber != null)
        checks.add(statistics.applicablePopulation != null && statistics.applicablePopulation!! > 0)
        checks.add(statistics.absenteeData.totalAbsenteePopulation > 0)
        
        // Check for distributions
        checks.add(statistics.absenteeData.getGenderDistribution().isNotEmpty())
        checks.add(statistics.absenteeData.reasonDistribution.isNotEmpty())
        checks.add(statistics.absenteeData.locationDistribution.isNotEmpty())
        
        // Special case: check for foreign absentees data
        if (statistics.absenteeData.locationDistribution.containsKey(AbsenteeLocationType.ANOTHER_COUNTRY) &&
            statistics.absenteeData.locationDistribution[AbsenteeLocationType.ANOTHER_COUNTRY]!! > 0) {
            checks.add(statistics.absenteeData.destinationCountryDistribution.isNotEmpty())
        }
        
        // Calculate score: proportion of passed checks
        return checks.count { it }.toDouble() / checks.size
    }

    /**
     * Calculate accuracy score (0.0-1.0)
     */
    private fun calculateAccuracyScore(
        statistics: WardWiseAbsenteeStatistics, 
        issues: List<ValidationIssue>
    ): Double {
        // Start with perfect score and subtract for issues
        var score = 1.0
        
        // Count critical and error issues
        val criticalIssues = issues.count { it.severity == ValidationIssue.IssueSeverity.CRITICAL }
        val errorIssues = issues.count { it.severity == ValidationIssue.IssueSeverity.ERROR }
        val warningIssues = issues.count { it.severity == ValidationIssue.IssueSeverity.WARNING }
        
        // Deduct points for issues
        score -= criticalIssues * 0.4  // 40% penalty per critical issue
        score -= errorIssues * 0.2     // 20% penalty per error
        score -= warningIssues * 0.05  // 5% penalty per warning
        
        // Ensure value is between 0 and 1
        return score.coerceIn(0.0, 1.0)
    }

    /**
     * Calculate consistency score (0.0-1.0)
     */
    private fun calculateConsistencyScore(
        statistics: WardWiseAbsenteeStatistics,
        issues: List<ValidationIssue>
    ): Double {
        var score = 1.0
        
        // Check for inconsistencies in distribution sums
        val inconsistencyIssues = issues.filter { it.type == ValidationIssue.IssueType.INCONSISTENT_DATA }
        
        if (inconsistencyIssues.isNotEmpty()) {
            score -= 0.3 * inconsistencyIssues.size  // 30% penalty per inconsistency
        }
        
        // Check for logical errors
        val logicalErrors = issues.filter { it.type == ValidationIssue.IssueType.LOGICAL_ERROR }
        
        if (logicalErrors.isNotEmpty()) {
            score -= 0.5  // 50% penalty if any logical errors exist
        }
        
        return score.coerceIn(0.0, 1.0)
    }
}
