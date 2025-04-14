package np.sthaniya.dpis.statistics.domain.event.institutions

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when institutional statistics are updated
 */
data class InstitutionalStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val educationalInstitutions: Int,
    val healthInstitutions: Int,
    val financialInstitutions: Int,
    val governmentOffices: Int
) : StatisticsEvent
