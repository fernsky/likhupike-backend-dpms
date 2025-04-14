package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when absentee population statistics are updated
 */
data class AbsenteeStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val totalAbsenteePopulation: Int,
    val maleAbsenteePopulation: Int,
    val femaleAbsenteePopulation: Int,
    val otherAbsenteePopulation: Int,
    val foreignAbsenteePercentage: Double,
    val mainAbsenceReason: String?,
    val mainDestinationCountry: String?
) : StatisticsEvent
