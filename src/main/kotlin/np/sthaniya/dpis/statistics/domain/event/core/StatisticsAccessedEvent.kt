package np.sthaniya.dpis.statistics.domain.event.core

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

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
