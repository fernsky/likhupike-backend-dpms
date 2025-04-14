package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when age-gender statistics are updated
 */
data class AgeGenderStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val totalPopulation: Int,
    val malePopulation: Int,
    val femalePopulation: Int,
    val otherPopulation: Int,
    val dependencyRatio: Double,
    val sexRatio: Double,
    val medianAge: Double?
) : StatisticsEvent
