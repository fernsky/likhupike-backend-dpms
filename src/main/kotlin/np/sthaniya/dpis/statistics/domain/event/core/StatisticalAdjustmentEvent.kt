package np.sthaniya.dpis.statistics.domain.event.core

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

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
