package np.sthaniya.dpis.statistics.infrastructure.persistence.repository

import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.StatisticsEventEntity
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
    
    /**
     * Find the last sequence number for an entity
     */
    @Query("SELECT MAX(e.sequenceNumber) FROM StatisticsEventEntity e WHERE e.entityId = :entityId")
    fun findLastSequenceNumber(entityId: UUID): Long?
    
    /**
     * Find events for an entity with sequence number greater than or equal to the given value
     */
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.entityId = :entityId AND e.sequenceNumber >= :fromSequence ORDER BY e.sequenceNumber ASC")
    fun findByEntityIdAndSequenceNumberGreaterThanEqualOrderBySequenceNumberAsc(entityId: UUID, fromSequence: Long): List<StatisticsEventEntity>
    
    /**
     * Find events for an entity with sequence number between the given range
     */
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.entityId = :entityId AND e.sequenceNumber >= :fromSequence AND e.sequenceNumber <= :toSequence ORDER BY e.sequenceNumber ASC")
    fun findByEntityIdAndSequenceNumberBetweenOrderBySequenceNumberAsc(entityId: UUID, fromSequence: Long, toSequence: Long): List<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE e.createdBy = :userId ORDER BY e.createdAt DESC")
    fun findByCreatedBy(userId: UUID, pageable: Pageable): Page<StatisticsEventEntity>
    
    @Query("SELECT e FROM StatisticsEventEntity e WHERE lower(e.eventData) LIKE lower(concat('%', :searchQuery, '%')) ORDER BY e.createdAt DESC")
    fun searchByEventData(searchQuery: String, pageable: Pageable): Page<StatisticsEventEntity>
}
