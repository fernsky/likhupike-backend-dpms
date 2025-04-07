package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjectionImpl
import np.sthaniya.dpis.citizen.repository.CitizenRepositoryCustom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.Optional
import java.util.UUID

/**
 * Implementation of custom repository operations for Citizen entities.
 *
 * This class delegates to specialized repository implementations for different
 * types of operations to maintain a clean separation of concerns and keep
 * the code maintainable.
 *
 * Features:
 * - Delegates to specialized search repository for name and address searches
 * - Delegates to specialized state repository for state-related queries
 * - Maintains clear separation of concerns
 *
 * @property entityManager JPA EntityManager for query execution
 */
class CitizenRepositoryImpl(
    private val entityManager: EntityManager
) : CitizenRepositoryCustom {

    // Specialized repository implementations for different types of operations
    private val searchRepository = CitizenSearchRepositoryImpl(entityManager)
    private val stateRepository = CitizenStateRepositoryImpl(entityManager)
    private val projectionRepository = CitizenProjectionRepositoryImpl(entityManager)

    /**
     * Finds a citizen by ID and eagerly loads related document storage keys.
     *
     * @param id The UUID of the citizen to search for
     * @return An Optional containing the citizen with document information if found
     */
    override fun findByIdWithDocuments(id: UUID): Optional<Citizen> {
        return searchRepository.findByIdWithDocuments(id)
    }
    
    /**
     * Searches for citizens by name with partial matching.
     *
     * @param namePart Part of the citizen name to search for
     * @return List of citizens matching the name pattern
     */
    override fun searchByNameContaining(namePart: String): List<Citizen> {
        return searchRepository.searchByNameContaining(namePart)
    }
    
    /**
     * Finds citizens by address with partial matching for both
     * permanent and temporary addresses.
     *
     * @param addressPart Part of the address to search for
     * @return List of citizens matching the address pattern
     */
    override fun findByAddressContaining(addressPart: String): List<Citizen> {
        return searchRepository.findByAddressContaining(addressPart)
    }
    
    /**
     * Finds citizens by their current state in the verification workflow.
     *
     * @param state The state to filter by
     * @param pageable Pagination information
     * @return Page of citizens in the specified state
     */
    override fun findByState(state: CitizenState, pageable: Pageable): Page<Citizen> {
        return stateRepository.findByState(state, pageable)
    }
    
    /**
     * Finds citizens who need action from administrators.
     * This includes those in PENDING_REGISTRATION and ACTION_REQUIRED states.
     *
     * @param pageable Pagination information
     * @return Page of citizens requiring administrator action
     */
    override fun findCitizensRequiringAction(pageable: Pageable): Page<Citizen> {
        return stateRepository.findCitizensRequiringAction(pageable)
    }
    
    /**
     * Finds citizens with missing or rejected documents.
     *
     * @param documentState The document state to filter by (e.g., NOT_UPLOADED, REJECTED_BLURRY)
     * @param pageable Pagination information
     * @return Page of citizens with documents in the specified state
     */
    override fun findByDocumentState(documentState: DocumentState, pageable: Pageable): Page<Citizen> {
        return stateRepository.findByDocumentState(documentState, pageable)
    }
    
    /**
     * Finds citizens with specific issues in their documents, using a note keyword search.
     *
     * @param noteKeyword Keyword to search for in document state notes
     * @param pageable Pagination information
     * @return Page of citizens with matching document state notes
     */
    override fun findByDocumentStateNote(noteKeyword: String, pageable: Pageable): Page<Citizen> {
        return stateRepository.findByDocumentStateNote(noteKeyword, pageable)
    }
    
    /**
     * Finds all citizens that match the given specification and returns them as projections
     * with only the requested fields included.
     *
     * @param spec Specification to filter citizens
     * @param pageable Pagination information
     * @param columns Set of column names to include in the projection
     * @return Page of citizen projections with only the requested fields
     */
    override fun findAllWithProjection(
        spec: Specification<Citizen>,
        pageable: Pageable,
        columns: Set<String>
    ): Page<CitizenProjection> {
        return projectionRepository.findAllWithProjection(spec, pageable, columns)
    }
    
    /**
     * Counts the number of citizens that match the given specification.
     * Uses distinct count when necessary (e.g., when joining related entities).
     *
     * @param spec Specification to filter citizens
     * @return Count of matching citizens
     */
    override fun countDistinct(spec: Specification<Citizen>): Long {
        return projectionRepository.countDistinct(spec)
    }
}
