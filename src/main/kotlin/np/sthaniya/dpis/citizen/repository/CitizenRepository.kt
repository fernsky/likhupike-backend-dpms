package np.sthaniya.dpis.citizen.repository

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * Repository for managing Citizen entities.
 * 
 * Provides standard JPA operations and extends with custom query methods
 * specific to citizen data.
 */
@Repository
interface CitizenRepository : 
    JpaRepository<Citizen, UUID>, 
    JpaSpecificationExecutor<Citizen>,
    CitizenRepositoryCustom {
    
    /**
     * Finds a citizen by email address.
     *
     * @param email The email address to search for
     * @return Optional containing the citizen if found
     */
    fun findByEmail(email: String): Optional<Citizen>
    
    /**
     * Finds a citizen by citizenship number.
     *
     * @param citizenshipNumber The citizenship number to search for
     * @return Optional containing the citizen if found
     */
    fun findByCitizenshipNumber(citizenshipNumber: String): Optional<Citizen>
    
    /**
     * Checks if a citizenship number already exists in the system.
     *
     * @param citizenshipNumber The citizenship number to check
     * @return true if the citizenship number exists, false otherwise
     */
    fun existsByCitizenshipNumber(citizenshipNumber: String): Boolean
    
    /**
     * Checks if an email address already exists in the system.
     *
     * @param email The email address to check
     * @return true if the email exists, false otherwise
     */
    fun existsByEmail(email: String): Boolean
    
    /**
     * Finds citizens by their verification state.
     *
     * @param state The state to filter by
     * @param pageable Pagination information
     * @return Page of citizens in the specified state
     */
    fun findByStateAndIsDeletedFalse(state: CitizenState, pageable: Pageable): Page<Citizen>
    
    /**
     * Counts citizens by their verification state.
     *
     * @param state The state to count
     * @return Number of citizens in the specified state
     */
    fun countByStateAndIsDeletedFalse(state: CitizenState): Long
    
    /**
     * Finds citizens that are not yet approved.
     *
     * @param pageable Pagination information
     * @return Page of citizens that haven't been approved yet
     */
    fun findByIsApprovedFalseAndIsDeletedFalse(pageable: Pageable): Page<Citizen>
    
    /**
     * Finds citizens requiring action (pending registration or action required).
     *
     * @param states List of states to include
     * @param pageable Pagination information
     * @return Page of citizens requiring administrator action
     */
    fun findByStateInAndIsDeletedFalse(states: List<CitizenState>, pageable: Pageable): Page<Citizen>
    
    /**
     * Finds citizens by approval status with optional filtering by state.
     *
     * @param isApproved Whether to find approved or unapproved citizens
     * @param state Optional state to filter by
     * @param pageable Pagination information
     * @return Page of citizens matching the criteria
     */
    @Query("SELECT c FROM Citizen c WHERE c.isApproved = :isApproved AND (:state IS NULL OR c.state = :state) AND c.isDeleted = false")
    fun findByApprovalStatusAndOptionalState(
        @Param("isApproved") isApproved: Boolean, 
        @Param("state") state: CitizenState?, 
        pageable: Pageable
    ): Page<Citizen>
}
