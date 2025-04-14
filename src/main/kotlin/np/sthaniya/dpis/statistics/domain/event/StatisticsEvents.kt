package np.sthaniya.dpis.statistics.domain.event

import java.time.LocalDateTime
import java.util.UUID

/**
 * Base interface for all statistics-related events
 */
interface StatisticsEvent {
    val statisticsId: UUID
    val calculationDate: LocalDateTime
}

/**
 * Event emitted when statistics are calculated
 */
data class StatisticsCalculatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val statisticalGroup: String,
    val statisticalCategory: String,
    val calculatedBy: UUID? = null,
    val isValid: Boolean = true,
    val validationNotes: String? = null
) : StatisticsEvent

/**
 * Event emitted when statistics are invalidated
 */
data class StatisticsInvalidatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val reason: String,
    val invalidatedBy: UUID,
    val recalculationRequired: Boolean = true
) : StatisticsEvent

/**
 * Event emitted when demographic statistics are updated
 */
data class DemographicStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val totalPopulation: Int,
    val malePopulation: Int,
    val femalePopulation: Int,
    val otherPopulation: Int,
    val totalHouseholds: Int
) : StatisticsEvent

/**
 * Event emitted when economic statistics are updated
 */
data class EconomicStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val economicallyActivePopulation: Int,
    val employmentRate: Double,
    val averageIncome: Double,
    val householdsWithLoans: Int
) : StatisticsEvent

/**
 * Event emitted when education statistics are updated
 */
data class EducationStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val totalStudents: Int,
    val literacyRate: Double,
    val primaryEnrollment: Int,
    val secondaryEnrollment: Int,
    val higherEducationEnrollment: Int
) : StatisticsEvent

/**
 * Event emitted when agricultural statistics are updated
 */
data class AgriculturalStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val farmingHouseholds: Int,
    val irrigatedLandArea: Double,
    val majorCrops: List<String>,
    val livestockCount: Int
) : StatisticsEvent

/**
 * Event emitted when institutional statistics are updated
 */
data class InstitutionalStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val educationalInstitutions: Int,
    val healthInstitutions: Int,
    val financialInstitutions: Int,
    val governmentOffices: Int
) : StatisticsEvent

/**
 * Event emitted when a manual statistical adjustment is made
 */
data class StatisticalAdjustmentEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val adjustedBy: UUID,
    val oldValue: Any,
    val newValue: Any,
    val justification: String,
    val approvedBy: UUID? = null,
    val isApproved: Boolean = false
) : StatisticsEvent

/**
 * Event emitted when statistics are accessed
 */
data class StatisticsAccessedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val accessedBy: UUID? = null,
    val accessChannel: String,
    val accessPurpose: String? = null
) : StatisticsEvent
