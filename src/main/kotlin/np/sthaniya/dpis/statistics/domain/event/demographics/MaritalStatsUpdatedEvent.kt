package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when marital status statistics are updated
 */
data class MaritalStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val marriedPercentage: Double,
    val earlyMarriageRate: Double
) : StatisticsEvent
