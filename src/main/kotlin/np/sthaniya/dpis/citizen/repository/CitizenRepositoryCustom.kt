package np.sthaniya.dpis.citizen.repository

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.Optional
import java.util.UUID

/**
 * Custom repository interface for Citizen entities.
 *
 * This interface defines custom repository operations that cannot be easily
 * expressed using Spring Data JPA query methods.
 */
interface CitizenRepositoryCustom {

    /**
     * Finds a citizen by ID and eagerly loads related document storage keys.
     *
     * @param id The UUID of the citizen to search for
     * @return An Optional containing the citizen with document information if found
     */
    fun findByIdWithDocuments(id: UUID): Optional<Citizen>

    /**
     * Searches for citizens by name with partial matching.
     *
     * @param namePart Part of the citizen name to search for
     * @return List of citizens matching the name pattern
     */
    fun searchByNameContaining(namePart: String): List<Citizen>

    /**
     * Finds citizens by address with partial matching for both
     * permanent and temporary addresses.
     *
     * @param addressPart Part of the address to search for
     * @return List of citizens matching the address pattern
     */
    fun findByAddressContaining(addressPart: String): List<Citizen>

    /**
     * Finds citizens by their current state in the verification workflow.
     *
     * @param state The state to filter by
     * @param pageable Pagination information
     * @return Page of citizens in the specified state
     */
    fun findByState(state: CitizenState, pageable: Pageable): Page<Citizen>

    /**
     * Finds citizens who need action from administrators.
     * This includes those in PENDING_REGISTRATION and ACTION_REQUIRED states.
     *
     * @param pageable Pagination information
     * @return Page of citizens requiring administrator action
     */
    fun findCitizensRequiringAction(pageable: Pageable): Page<Citizen>

    /**
     * Finds citizens with missing or rejected documents.
     *
     * @param documentState The document state to filter by (e.g., NOT_UPLOADED, REJECTED_BLURRY)
     * @param pageable Pagination information
     * @return Page of citizens with documents in the specified state
     */
    fun findByDocumentState(documentState: DocumentState, pageable: Pageable): Page<Citizen>

    /**
     * Finds citizens with specific issues in their documents, using a note keyword search.
     *
     * @param noteKeyword Keyword to search for in document state notes
     * @param pageable Pagination information
     * @return Page of citizens with matching document state notes
     */
    fun findByDocumentStateNote(noteKeyword: String, pageable: Pageable): Page<Citizen>

    /**
     * Finds all citizens that match the given specification and returns them as projections
     * with only the requested fields included.
     *
     * @param spec Specification to filter citizens
     * @param pageable Pagination information
     * @param columns Set of column names to include in the projection
     * @return Page of citizen projections with only the requested fields
     */
    fun findAllWithProjection(
        spec: Specification<Citizen>,
        pageable: Pageable,
        columns: Set<String>
    ): Page<CitizenProjection>

    /**
     * Counts the number of citizens that match the given specification.
     * Uses distinct count when necessary (e.g., when joining related entities).
     *
     * @param spec Specification to filter citizens
     * @return Count of matching citizens
     */
    fun countDistinct(spec: Specification<Citizen>): Long
}
