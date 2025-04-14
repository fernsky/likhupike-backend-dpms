package np.sthaniya.dpis.statistics.domain.event

import java.time.LocalDateTime
import java.util.UUID

/**
 * Base interface for all statistics-related events
 */
interface StatisticsEvent {
    val statisticsId: UUID
    val calculationDate: LocalDateTime
}
