package np.sthaniya.dpis.statistics.domain.event.education

import np.sthaniya.dpis.statistics.domain.event.StatisticsEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * Event emitted when education statistics are updated
 */
data class EducationStatsUpdatedEvent(
    override val statisticsId: UUID,
    override val calculationDate: LocalDateTime,
    val wardId: UUID,
    val wardNumber: Int,
    val totalStudents: Int,
    val literacyRate: Double,
    val primaryEnrollment: Int,
    val secondaryEnrollment: Int,
    val higherEducationEnrollment: Int
) : StatisticsEvent
