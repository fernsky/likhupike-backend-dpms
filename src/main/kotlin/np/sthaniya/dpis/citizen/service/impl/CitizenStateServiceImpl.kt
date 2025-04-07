package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentType
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.mapper.CitizenMapper
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenStateService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Implementation of CitizenStateService to manage citizen state transitions.
 */
@Service
class CitizenStateServiceImpl(
    private val citizenRepository: CitizenRepository,
    private val citizenMapper: CitizenMapper
) : CitizenStateService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Updates a citizen's state in the verification workflow.
     */
    @Transactional
    override fun updateCitizenState(
        citizenId: UUID,
        newState: CitizenState,
        note: String?,
        updatedBy: UUID
    ): CitizenResponse {
        logger.info("Updating state for citizen ID: $citizenId to $newState")
        
        val citizen = citizenRepository.findById(citizenId).orElseThrow {
            throw CitizenException(
                CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND, 
                "Citizen with ID $citizenId not found"
            )
        }
        
        // Check that the state transition is valid
        // For example, an APPROVED citizen shouldn't go back to PENDING_REGISTRATION
        val currentState = citizen.state
        validateStateTransition(currentState, newState)
        
        citizen.updateState(newState, note, updatedBy)
        val savedCitizen = citizenRepository.save(citizen)
        
        logger.info("Successfully updated state for citizen ID: $citizenId from $currentState to $newState")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    /**
     * Updates the state of a citizen's document.
     */
    @Transactional
    override fun updateDocumentState(
        citizenId: UUID,
        documentType: DocumentType,
        newState: DocumentState,
        note: String?,
        updatedBy: UUID
    ): CitizenResponse {
        logger.info("Updating document state for citizen ID: $citizenId, document: $documentType to $newState")
        
        val citizen = citizenRepository.findById(citizenId).orElseThrow {
            throw CitizenException(
                CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND, 
                "Citizen with ID $citizenId not found"
            )
        }
        
        // Check that the document exists
        when (documentType) {
            DocumentType.CITIZEN_PHOTO -> {
                if (citizen.photoKey == null) {
                    throw CitizenException(
                        CitizenException.CitizenErrorCode.DOCUMENT_NOT_FOUND,
                        "Photo document not found for citizen $citizenId"
                    )
                }
            }
            DocumentType.CITIZENSHIP_FRONT -> {
                if (citizen.citizenshipFrontKey == null) {
                    throw CitizenException(
                        CitizenException.CitizenErrorCode.DOCUMENT_NOT_FOUND,
                        "Citizenship front document not found for citizen $citizenId"
                    )
                }
            }
            DocumentType.CITIZENSHIP_BACK -> {
                if (citizen.citizenshipBackKey == null) {
                    throw CitizenException(
                        CitizenException.CitizenErrorCode.DOCUMENT_NOT_FOUND,
                        "Citizenship back document not found for citizen $citizenId"
                    )
                }
            }
            else -> {
                throw CitizenException(
                    CitizenException.CitizenErrorCode.INVALID_DOCUMENT_TYPE,
                    "Unsupported document type: $documentType"
                )
            }
        }
        
        // Update document state
        citizen.updateDocumentState(documentType, newState, note)
        citizen.updatedBy = updatedBy
        
        val savedCitizen = citizenRepository.save(citizen)
        
        logger.info("Successfully updated document state for citizen ID: $citizenId, document: $documentType to $newState")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    /**
     * Gets citizens that require administrative action.
     */
    @Transactional(readOnly = true)
    override fun getCitizensRequiringAction(page: Int, size: Int): List<CitizenResponse> {
        logger.debug("Getting citizens requiring action, page: $page, size: $size")
        
        val pageable = PageRequest.of(
            page - 1, // Convert to 0-based
            size,
            Sort.by(Sort.Direction.DESC, "stateUpdatedAt")
        )
        
        val citizensPage = citizenRepository.findCitizensRequiringAction(pageable)
        
        return citizensPage.content.map { citizenMapper.toResponse(it) }
    }

    /**
     * Gets citizens in a specific state.
     */
    @Transactional(readOnly = true)
    override fun getCitizensByState(state: CitizenState, page: Int, size: Int): List<CitizenResponse> {
        logger.debug("Getting citizens in state: $state, page: $page, size: $size")
        
        val pageable = PageRequest.of(
            page - 1, // Convert to 0-based
            size,
            Sort.by(Sort.Direction.DESC, "stateUpdatedAt")
        )
        
        val citizensPage = citizenRepository.findByState(state, pageable)
        
        return citizensPage.content.map { citizenMapper.toResponse(it) }
    }

    /**
     * Gets citizens with documents in a specific state.
     */
    @Transactional(readOnly = true)
    override fun getCitizensByDocumentState(documentState: DocumentState, page: Int, size: Int): List<CitizenResponse> {
        logger.debug("Getting citizens with document state: $documentState, page: $page, size: $size")
        
        val pageable = PageRequest.of(
            page - 1, // Convert to 0-based
            size,
            Sort.by(Sort.Direction.DESC, "updatedAt")
        )
        
        val citizensPage = citizenRepository.findByDocumentState(documentState, pageable)
        
        return citizensPage.content.map { citizenMapper.toResponse(it) }
    }

    /**
     * Gets citizens with specific issues in their documents, using a note keyword search.
     */
    @Transactional(readOnly = true)
    override fun getCitizensByDocumentStateNote(noteKeyword: String, page: Int, size: Int): List<CitizenResponse> {
        logger.debug("Getting citizens with document note containing: $noteKeyword, page: $page, size: $size")
        
        val pageable = PageRequest.of(
            page - 1, // Convert to 0-based
            size,
            Sort.by(Sort.Direction.DESC, "updatedAt")
        )
        
        val citizensPage = citizenRepository.findByDocumentStateNote(noteKeyword, pageable)
        
        return citizensPage.content.map { citizenMapper.toResponse(it) }
    }

    /**
     * Validates that a state transition is allowed.
     * Throws an exception if the transition is invalid.
     */
    private fun validateStateTransition(currentState: CitizenState, newState: CitizenState) {
        // Define invalid transitions
        val invalidTransitions = mapOf(
            CitizenState.APPROVED to setOf(
                CitizenState.PENDING_REGISTRATION
            ),
            CitizenState.REJECTED to setOf(
                CitizenState.PENDING_REGISTRATION
            )
        )
        
        invalidTransitions[currentState]?.let { invalidStates ->
            if (newState in invalidStates) {
                throw CitizenException(
                    CitizenException.CitizenErrorCode.INVALID_STATE_TRANSITION,
                    "Cannot transition from $currentState to $newState"
                )
            }
        }
    }
}
