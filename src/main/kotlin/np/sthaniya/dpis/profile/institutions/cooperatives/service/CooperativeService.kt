package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

/**
 * Service for managing cooperative operations.
 */
interface CooperativeService {

    /**
     * Creates a new cooperative with initial translation.
     *
     * @param createDto DTO containing cooperative data and initial translation
     * @return The created cooperative response
     */
    fun createCooperative(createDto: CreateCooperativeDto): CooperativeResponse

    /**
     * Updates an existing cooperative.
     *
     * @param id Cooperative ID
     * @param updateDto DTO containing updated cooperative data
     * @return The updated cooperative response
     */
    fun updateCooperative(id: UUID, updateDto: UpdateCooperativeDto): CooperativeResponse

    /**
     * Retrieves a cooperative by ID.
     *
     * @param id Cooperative ID
     * @return The cooperative response
     */
    fun getCooperativeById(id: UUID): CooperativeResponse

    /**
     * Retrieves a cooperative by its code.
     *
     * @param code Unique cooperative code
     * @return The cooperative response
     */
    fun getCooperativeByCode(code: String): CooperativeResponse

    /**
     * Deletes a cooperative.
     *
     * @param id Cooperative ID
     */
    fun deleteCooperative(id: UUID)

    /**
     * Retrieves all cooperatives with pagination.
     *
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun getAllCooperatives(pageable: Pageable): Page<CooperativeResponse>

    /**
     * Retrieves all cooperatives by status with pagination.
     *
     * @param status Cooperative status
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun getCooperativesByStatus(status: CooperativeStatus, pageable: Pageable): Page<CooperativeResponse>

    /**
     * Retrieves all cooperatives by type with pagination.
     *
     * @param type Cooperative type
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun getCooperativesByType(type: CooperativeType, pageable: Pageable): Page<CooperativeResponse>

    /**
     * Retrieves all cooperatives in a specific ward with pagination.
     *
     * @param ward Ward number
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun getCooperativesByWard(ward: Int, pageable: Pageable): Page<CooperativeResponse>

    /**
     * Searches cooperatives by name in any available translation.
     *
     * @param nameQuery The name search query
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun searchCooperativesByName(nameQuery: String, pageable: Pageable): Page<CooperativeResponse>

    /**
     * Changes the status of a cooperative.
     *
     * @param id Cooperative ID
     * @param status New status
     * @return The updated cooperative response
     */
    fun changeCooperativeStatus(id: UUID, status: CooperativeStatus): CooperativeResponse

    /**
     * Finds cooperatives within a specified distance of a geographic point.
     *
     * @param longitude The longitude coordinate
     * @param latitude The latitude coordinate
     * @param distanceInMeters Maximum distance in meters
     * @param pageable Pagination information
     * @return Page of cooperative responses
     */
    fun findCooperativesNear(
        longitude: Double, 
        latitude: Double, 
        distanceInMeters: Double = 5000.0,
        pageable: Pageable
    ): Page<CooperativeResponse>

    /**
     * Gets cooperative statistics by type.
     *
     * @return Map of cooperative type to count
     */
    fun getCooperativeStatisticsByType(): Map<CooperativeType, Long>

    /**
     * Gets cooperative statistics by ward.
     *
     * @return Map of ward number to count
     */
    fun getCooperativeStatisticsByWard(): Map<Int, Long>
}
