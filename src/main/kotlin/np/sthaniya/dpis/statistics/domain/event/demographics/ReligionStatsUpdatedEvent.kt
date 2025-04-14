package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when religion statistics are updated
 */
data class ReligionStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val dominantReligion: String?,
    val diversityIndex: Double,
    val isReligiouslyDiverse: Boolean
) : StatisticsEvent
