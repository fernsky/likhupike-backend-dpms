package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

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
