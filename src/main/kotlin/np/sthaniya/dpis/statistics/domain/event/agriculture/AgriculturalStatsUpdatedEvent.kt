package np.sthaniya.dpis.statistics.domain.event.agriculture

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when agricultural statistics are updated
 */
data class AgriculturalStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val farmingHouseholds: Int,
    val irrigatedLandArea: Double,
    val majorCrops: List<String>,
    val livestockCount: Int
) : StatisticsEvent
