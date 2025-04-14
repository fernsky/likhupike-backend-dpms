package np.sthaniya.dpis.statistics.infrastructure.persistence.repository

import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.WardWiseAbsenteeStatisticsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA repository for ward-level absentee statistics.
 */
@Repository
interface JpaWardWiseAbsenteeStatisticsRepository : JpaRepository<WardWiseAbsenteeStatisticsEntity, UUID> {
    
    fun findByWardId(wardId: UUID): List<WardWiseAbsenteeStatisticsEntity>
    
    fun findByWardNumber(wardNumber: Int): List<WardWiseAbsenteeStatisticsEntity>
    
    fun findByMunicipalityId(municipalityId: UUID): List<WardWiseAbsenteeStatisticsEntity>
    
    fun findByWardIdAndIsValidTrue(wardId: UUID): List<WardWiseAbsenteeStatisticsEntity>
    
    fun findByWardIdAndReferenceDate(wardId: UUID, referenceDate: LocalDateTime): WardWiseAbsenteeStatisticsEntity?
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.absenteePercentage >= :threshold AND e.isValid = true")
    fun findByAbsenteePercentageGreaterThanEqual(threshold: BigDecimal): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.primaryAbsenceReason = :reason AND e.isValid = true")
    fun findByPrimaryReason(reason: AbsenceReason): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.foreignAbsenteePercentage >= :threshold AND e.isValid = true")
    fun findByForeignAbsenteePercentageGreaterThanEqual(threshold: BigDecimal): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.topDestinationCountry = :country AND e.isValid = true")
    fun findByTopDestinationCountry(country: String): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.absenteeSexRatio >= :ratio AND e.isValid = true")
    fun findByAbsenteeSexRatioGreaterThanEqual(ratio: BigDecimal): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.calculationDate >= :since AND e.isValid = true")
    fun findUpdatedSince(since: LocalDateTime): List<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.isValid = true ORDER BY e.absenteePercentage DESC")
    fun findAllOrderByAbsenteePercentageDesc(pageable: Pageable): Page<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.isValid = true ORDER BY e.absenteePercentage ASC")
    fun findAllOrderByAbsenteePercentageAsc(pageable: Pageable): Page<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT e FROM WardWiseAbsenteeStatisticsEntity e WHERE e.wardId = :wardId AND e.calculationDate <= :asOfDate AND e.isValid = true ORDER BY e.calculationDate DESC")
    fun findByWardIdAsOf(wardId: UUID, asOfDate: LocalDateTime, pageable: Pageable): Page<WardWiseAbsenteeStatisticsEntity>
    
    @Query("SELECT COUNT(e) FROM WardWiseAbsenteeStatisticsEntity e WHERE e.wardId = :wardId AND e.isValid = true")
    fun countByWardId(wardId: UUID): Long
}
