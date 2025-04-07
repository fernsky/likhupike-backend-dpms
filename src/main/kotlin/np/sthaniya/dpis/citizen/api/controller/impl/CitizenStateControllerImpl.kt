package np.sthaniya.dpis.citizen.api.controller.impl

import np.sthaniya.dpis.citizen.api.controller.CitizenStateController
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.domain.entity.DocumentType
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.state.DocumentStateUpdateDto
import np.sthaniya.dpis.citizen.dto.state.StateUpdateDto
import np.sthaniya.dpis.citizen.service.CitizenStateService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the CitizenStateController interface.
 *
 * Handles HTTP requests for citizen state management operations.
 */
@RestController
class CitizenStateControllerImpl(
    private val citizenStateService: CitizenStateService,
    private val i18nMessageService: I18nMessageService
) : CitizenStateController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Updates the state of a citizen in the verification workflow.
     */
    override fun updateCitizenState(
        id: UUID,
        stateUpdateDto: StateUpdateDto,
        currentUserId: UUID
    ): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Updating state for citizen ID: $id to ${stateUpdateDto.state} by user: $currentUserId")
        
        val updatedCitizen = citizenStateService.updateCitizenState(
            citizenId = id,
            newState = stateUpdateDto.state,
            note = stateUpdateDto.note,
            updatedBy = currentUserId
        )
        
        logger.info("Successfully updated state for citizen ID: $id to ${stateUpdateDto.state}")
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedCitizen,
                message = i18nMessageService.getMessage(
                    "citizen.state.update.success"
                )
            )
        )
    }

    /**
     * Updates the state of a citizen's document.
     */
    override fun updateDocumentState(
        id: UUID,
        documentType: DocumentType,
        documentStateUpdateDto: DocumentStateUpdateDto,
        currentUserId: UUID
    ): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info(
            "Updating document state for citizen ID: $id, document type: $documentType " +
            "to ${documentStateUpdateDto.state} by user: $currentUserId"
        )
        
        val updatedCitizen = citizenStateService.updateDocumentState(
            citizenId = id,
            documentType = documentType,
            newState = documentStateUpdateDto.state,
            note = documentStateUpdateDto.note,
            updatedBy = currentUserId
        )
        
        logger.info(
            "Successfully updated document state for citizen ID: $id, document type: $documentType " +
            "to ${documentStateUpdateDto.state}"
        )
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedCitizen,
                message = i18nMessageService.getMessage(
                    "citizen.document.state.update.success" 
                )
            )
        )
    }

    /**
     * Gets citizens requiring administrative action.
     */
    override fun getCitizensRequiringAction(
        page: Int,
        size: Int
    ): ResponseEntity<ApiResponse<List<CitizenResponse>>> {
        logger.debug("Getting citizens requiring action, page: $page, size: $size")
        
        val citizens = citizenStateService.getCitizensRequiringAction(page, size)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = citizens,
                message = i18nMessageService.getMessage("citizen.list.requiring.action.success")
            )
        )
    }

    /**
     * Gets citizens in a specific state.
     */
    override fun getCitizensByState(
        state: CitizenState,
        page: Int,
        size: Int
    ): ResponseEntity<ApiResponse<List<CitizenResponse>>> {
        logger.debug("Getting citizens in state: $state, page: $page, size: $size")
        
        val citizens = citizenStateService.getCitizensByState(state, page, size)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = citizens,
                message = i18nMessageService.getMessage(
                    "citizen.list.by.state.success"
                )
            )
        )
    }

    /**
     * Gets citizens with documents in a specific state.
     */
    override fun getCitizensByDocumentState(
        documentState: DocumentState,
        page: Int,
        size: Int
    ): ResponseEntity<ApiResponse<List<CitizenResponse>>> {
        logger.debug("Getting citizens with document state: $documentState, page: $page, size: $size")
        
        val citizens = citizenStateService.getCitizensByDocumentState(documentState, page, size)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = citizens,
                message = i18nMessageService.getMessage(
                    "citizen.list.by.document.state.success"
                )
            )
        )
    }

    /**
     * Gets citizens with specific issues in their documents, using a note keyword search.
     */
    override fun getCitizensByDocumentStateNote(
        noteKeyword: String,
        page: Int,
        size: Int
    ): ResponseEntity<ApiResponse<List<CitizenResponse>>> {
        logger.debug("Getting citizens with document note containing: $noteKeyword, page: $page, size: $size")
        
        val citizens = citizenStateService.getCitizensByDocumentStateNote(noteKeyword, page, size)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = citizens,
                message = i18nMessageService.getMessage(
                    "citizen.list.by.document.note.success")
            )
        )
    }
}
