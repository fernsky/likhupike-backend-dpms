package np.sthaniya.dpis.statistics.domain.event.core

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

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
