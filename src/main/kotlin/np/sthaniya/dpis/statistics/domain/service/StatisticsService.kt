package np.sthaniya.dpis.statistics.domain.service

import np.sthaniya.dpis.statistics.domain.model.BaseStatistics
import java.time.LocalDateTime
import java.util.UUID

/**
 * Base interface for all statistics services.
 * Provides common operations for calculating, validating, and managing statistics.
 */
interface StatisticsService<T : BaseStatistics> {

    /**
     * Calculate statistics based on input data
     *
     * @param inputData Map containing the input data for calculation
     * @param referenceDate The reference date for the statistics
     * @param calculatedBy ID of the user performing the calculation
     * @return The calculated statistics entity
     */
    fun calculate(
        inputData: Map<String, Any>,
        referenceDate: LocalDateTime = LocalDateTime.now(),
        calculatedBy: UUID? = null
    ): T
    
    /**
     * Validate a statistics entity for data quality and consistency
     *
     * @param entity The statistics entity to validate
     * @return List of validation issues, empty if valid
     */
    fun validate(entity: T): List<ValidationIssue>
    
    /**
     * Rebuild a statistics entity from historical events up to a specific point in time
     *
     * @param entityId The ID of the entity to rebuild
     * @param asOfDate The point in time up to which events should be applied
     * @return The rebuilt statistics entity, null if not found
     */
    fun rebuildFromEvents(entityId: UUID, asOfDate: LocalDateTime = LocalDateTime.now()): T?
    
    /**
     * Perform quality checks on statistics entities
     *
     * @param entity The statistics entity to check
     * @return Quality assessment results
     */
    fun assessQuality(entity: T): QualityAssessment
    
    /**
     * Aggregate statistics across multiple entities
     *
     * @param entityIds IDs of the entities to aggregate
     * @param aggregationParams Parameters controlling the aggregation
     * @return The aggregated results
     */
    fun aggregate(entityIds: List<UUID>, aggregationParams: Map<String, Any> = emptyMap()): Map<String, Any>
    
    /**
     * Compare two statistics entities
     *
     * @param entity1Id ID of the first entity
     * @param entity2Id ID of the second entity
     * @return Comparison results
     */
    fun compare(entity1Id: UUID, entity2Id: UUID): Map<String, Any>
}

/**
 * Represents a validation issue found during statistics validation.
 */
data class ValidationIssue(
    val type: IssueType,
    val field: String,
    val message: String,
    val severity: IssueSeverity
) {
    enum class IssueType {
        MISSING_DATA,
        INCONSISTENT_DATA,
        INVALID_VALUE,
        STATISTICAL_ANOMALY,
        FORMAT_ERROR,
        LOGICAL_ERROR
    }
    
    enum class IssueSeverity {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
}

/**
 * Represents the quality assessment of a statistics entity.
 */
data class QualityAssessment(
    val score: Int, // 0-100
    val confidenceLevel: String, // LOW, MEDIUM, HIGH
    val completeness: Double, // 0.0-1.0
    val accuracy: Double, // 0.0-1.0
    val consistency: Double, // 0.0-1.0
    val issues: List<ValidationIssue>
)
