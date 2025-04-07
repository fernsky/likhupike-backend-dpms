package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentType
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import java.util.UUID

/**
 * Service interface for managing citizen state transitions and document states.
 */
interface CitizenStateService {

    /**
     * Updates a citizen's state in the verification workflow.
     *
     * @param citizenId The ID of the citizen to update
     * @param newState The new state to set
     * @param note Optional note explaining the state change
     * @param updatedBy ID of the user updating the state
     * @return The updated citizen data
     */
    fun updateCitizenState(
        citizenId: UUID,
        newState: CitizenState,
        note: String? = null,
        updatedBy: UUID
    ): CitizenResponse

    /**
     * Updates the state of a citizen's document.
     *
     * @param citizenId The ID of the citizen
     * @param documentType Type of document being updated (PHOTO, CITIZENSHIP_FRONT, etc.)
     * @param newState The new document state
     * @param note Optional note explaining the state change
     * @param updatedBy ID of the user updating the state
     * @return The updated citizen data
     */
    fun updateDocumentState(
        citizenId: UUID,
        documentType: DocumentType,
        newState: DocumentState,
        note: String? = null,
        updatedBy: UUID
    ): CitizenResponse
    
    /**
     * Gets citizens that require administrative action.
     * 
     * @param page Page number (1-based)
     * @param size Page size
     * @return Page of citizens requiring action
     */
    fun getCitizensRequiringAction(page: Int, size: Int): List<CitizenResponse>
    
    /**
     * Gets citizens in a specific state.
     *
     * @param state The state to filter by
     * @param page Page number (1-based)
     * @param size Page size
     * @return Page of citizens in the specified state
     */
    fun getCitizensByState(state: CitizenState, page: Int, size: Int): List<CitizenResponse>
    
    /**
     * Gets citizens with documents in a specific state.
     *
     * @param documentState The document state to filter by
     * @param page Page number (1-based)
     * @param size Page size
     * @return Page of citizens with documents in the specified state
     */
    fun getCitizensByDocumentState(documentState: DocumentState, page: Int, size: Int): List<CitizenResponse>
    
    /**
     * Gets citizens with specific issues in their documents, using a note keyword search.
     *
     * @param noteKeyword Keyword to search for in document state notes
     * @param page Page number (1-based)
     * @param size Page size
     * @return Page of citizens with matching document state notes
     */
    fun getCitizensByDocumentStateNote(noteKeyword: String, page: Int, size: Int): List<CitizenResponse>
}
