package np.sthaniya.dpis.statistics.domain.event.demographics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when language statistics are updated
 */
data class LanguageStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val dominantLanguage: String?,
    val diversityIndex: Double,
    val secondaryLanguagesCount: Int
) : StatisticsEvent
