package np.sthaniya.dpis.statistics.domain.event.core

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

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
