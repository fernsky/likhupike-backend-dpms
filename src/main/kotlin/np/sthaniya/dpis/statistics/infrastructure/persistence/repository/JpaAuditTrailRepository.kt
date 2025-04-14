package np.sthaniya.dpis.statistics.infrastructure.persistence.repository

import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.AuditTrailEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA repository for audit trail records.
 */
@Repository
interface JpaAuditTrailRepository : JpaRepository<AuditTrailEntity, UUID> {
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.entityId = :entityId ORDER BY a.timestamp DESC")
    fun findByEntityIdOrderByTimestampDesc(entityId: UUID): List<AuditTrailEntity>
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.entityType = :entityType ORDER BY a.timestamp DESC")
    fun findByEntityType(entityType: String, pageable: Pageable): Page<AuditTrailEntity>
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.userId = :userId ORDER BY a.timestamp DESC")
    fun findByUserId(userId: UUID, pageable: Pageable): Page<AuditTrailEntity>
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.action = :action ORDER BY a.timestamp DESC")
    fun findByAction(action: String, pageable: Pageable): Page<AuditTrailEntity>
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.timestamp BETWEEN :startTime AND :endTime ORDER BY a.timestamp DESC")
    fun findByTimestampBetween(startTime: LocalDateTime, endTime: LocalDateTime, pageable: Pageable): Page<AuditTrailEntity>
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE lower(a.details) LIKE lower(concat('%', :searchQuery, '%')) OR lower(a.oldState) LIKE lower(concat('%', :searchQuery, '%')) OR lower(a.newState) LIKE lower(concat('%', :searchQuery, '%')) ORDER BY a.timestamp DESC")
    fun search(searchQuery: String, pageable: Pageable): Page<AuditTrailEntity>
    
    @Query("SELECT COUNT(a) FROM AuditTrailEntity a WHERE a.entityId = :entityId")
    fun countByEntityId(entityId: UUID): Long
    
    @Query("SELECT a FROM AuditTrailEntity a WHERE a.isSensitive = true ORDER BY a.timestamp DESC")
    fun findSensitiveOperations(pageable: Pageable): Page<AuditTrailEntity>
}
