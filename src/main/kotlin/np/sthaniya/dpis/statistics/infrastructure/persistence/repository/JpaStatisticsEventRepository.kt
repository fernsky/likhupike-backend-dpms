package np.sthaniya.dpis.statistics.infrastructure.persistence.repository

import np.sthaniya.dpis.statistics.infrastructure.persistence.event.StatisticsEventEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA repository for statistics events.
 */
@Repository
interface JpaStatisticsEventRepository : JpaRepository<StatisticsEventEntity, UUID> {
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.entityId = :entityId ORDER BY e.sequenceNumber ASC")
    fun findByEntityIdOrderBySequenceNumberAsc(entityId: UUID): List<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.entityId = :entityId AND e.createdAt <= :asOfDate ORDER BY e.sequenceNumber ASC")
    fun findByEntityIdAndCreatedAtBeforeOrderBySequenceNumberAsc(entityId: UUID, asOfDate: LocalDateTime): List<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.eventType = :eventType ORDER BY e.createdAt DESC")
    fun findByEventType(eventType: String, pageable: Pageable): Page<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.entityType = :entityType ORDER BY e.createdAt DESC")
    fun findByEntityType(entityType: String, pageable: Pageable): Page<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.createdAt BETWEEN :startTime AND :endTime ORDER BY e.createdAt DESC")
    fun findByCreatedAtBetween(startTime: LocalDateTime, endTime: LocalDateTime, pageable: Pageable): Page<StatisticsEventEntity>
    
    @Query("SELECT COUNT(e) FROM StatisticsEventEntity e WHERE e.entityId = :entityId")
    fun countByEntityId(entityId: UUID): Long
    
    @Query("SELECT MAX(e.sequenceNumber) FROM StatisticsEventEntity e WHERE e.entityId = :entityId")
    fun findMaxSequenceNumberForEntity(entityId: UUID): Long?
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.createdBy = :userId ORDER BY e.createdAt DESC")
    fun findByCreatedBy(userId: UUID, pageable: Pageable): Page<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE lower(e.eventData) LIKE lower(concat('%', :searchQuery, '%')) ORDER BY e.createdAt DESC")
    fun searchByEventData(searchQuery: String, pageable: Pageable): Page<StatisticsEventEntity>
}
