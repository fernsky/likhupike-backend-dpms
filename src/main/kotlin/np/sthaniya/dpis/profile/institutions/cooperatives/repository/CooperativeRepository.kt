package np.sthaniya.dpis.profile.institutions.cooperatives.repository

import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * Repository interface for managing [Cooperative] entities.
 */
@Repository
interface CooperativeRepository : JpaRepository<Cooperative, UUID> {

    /**
     * Find a cooperative by its unique code.
     *
     * @param code The unique code of the cooperative
     * @return Optional containing the cooperative if found
     */
    fun findByCode(code: String): Optional<Cooperative>

    /**
     * Find all cooperatives by their status.
     *
     * @param status The status of cooperatives to find
     * @param pageable Pagination information
     * @return A page of cooperatives with the given status
     */
    fun findByStatus(status: CooperativeStatus, pageable: Pageable): Page<Cooperative>

    /**
     * Find all cooperatives by their type.
     *
     * @param type The type of cooperatives to find
     * @param pageable Pagination information
     * @return A page of cooperatives with the given type
     */
    fun findByType(type: CooperativeType, pageable: Pageable): Page<Cooperative>

    /**
     * Find all cooperatives located in a specific ward.
     *
     * @param ward The ward number to search in
     * @param pageable Pagination information
     * @return A page of cooperatives located in the specified ward
     */
    fun findByWard(ward: Int, pageable: Pageable): Page<Cooperative>

    /**
     * Search cooperatives by name in any available translation.
     *
     * @param nameQuery The name search query
     * @param pageable Pagination information
     * @return A page of cooperatives matching the name query
     */
    @Query("""
        SELECT DISTINCT c FROM Cooperative c 
        JOIN c.translations t 
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :nameQuery, '%'))
    """)
    fun searchByName(@Param("nameQuery") nameQuery: String, pageable: Pageable): Page<Cooperative>

    /**
     * Search cooperatives by type and name in any available translation.
     *
     * @param type The cooperative type
     * @param nameQuery The name search query
     * @param pageable Pagination information
     * @return A page of cooperatives matching the type and name query
     */
    @Query("""
        SELECT DISTINCT c FROM Cooperative c 
        JOIN c.translations t 
        WHERE c.type = :type AND LOWER(t.name) LIKE LOWER(CONCAT('%', :nameQuery, '%'))
    """)
    fun searchByTypeAndName(
        @Param("type") type: CooperativeType,
        @Param("nameQuery") nameQuery: String,
        pageable: Pageable
    ): Page<Cooperative>
    
    /**
     * Check if a cooperative with the given code already exists.
     *
     * @param code The code to check
     * @return true if a cooperative with this code exists
     */
    fun existsByCode(code: String): Boolean
    
    /**
     * Find cooperatives within a specified distance of a geographic point.
     *
     * @param longitude The longitude of the reference point
     * @param latitude The latitude of the reference point
     * @param distanceInMeters The maximum distance in meters
     * @param pageable Pagination information
     * @return A page of cooperatives within the specified distance
     */
    @Query(value = """
        SELECT c.* FROM cooperatives c
        WHERE ST_DWithin(
            c.point,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
            :distanceInMeters
        )
    """, nativeQuery = true)
    fun findWithinDistance(
        @Param("longitude") longitude: Double, 
        @Param("latitude") latitude: Double,
        @Param("distanceInMeters") distanceInMeters: Double,
        pageable: Pageable
    ): Page<Cooperative>
    
    /**
     * Get the count of cooperatives by type.
     *
     * @return A list of counts by cooperative type
     */
    @Query("""
        SELECT c.type as type, COUNT(c) as count 
        FROM Cooperative c 
        GROUP BY c.type
    """)
    fun countByType(): List<CooperativeTypeCount>
    
    /**
     * Get the count of cooperatives by ward.
     *
     * @return A list of counts by ward
     */
    @Query("""
        SELECT c.ward as ward, COUNT(c) as count 
        FROM Cooperative c 
        GROUP BY c.ward
        ORDER BY c.ward
    """)
    fun countByWard(): List<CooperativeWardCount>
}

/**
 * Interface for cooperative count by type results.
 */
interface CooperativeTypeCount {
    val type: CooperativeType
    val count: Long
}

/**
 * Interface for cooperative count by ward results.
 */
interface CooperativeWardCount {
    val ward: Int
    val count: Long
}
