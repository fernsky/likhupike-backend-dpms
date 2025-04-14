package np.sthaniya.dpis.statistics.domain.event.economics

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when economic statistics are updated
 */
data class EconomicStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val economicallyActivePopulation: Int,
    val employmentRate: Double,
    val averageIncome: Double,
    val householdsWithLoans: Int
) : StatisticsEvent
