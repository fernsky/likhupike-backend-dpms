package np.sthaniya.dpis.citizen.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import np.sthaniya.dpis.auth.resolver.CurrentUserId
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.domain.entity.DocumentType
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.state.DocumentStateUpdateDto
import np.sthaniya.dpis.citizen.dto.state.StateUpdateDto
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing citizen states and workflow.
 *
 * Provides endpoints for administrators to manage the state of citizens
 * and their documents through the verification workflow.
 */
@Tag(name = "Citizen State Management", description = "APIs for managing citizen state workflow and verification")
@RequestMapping("/api/v1/admin/citizen-state")
@Validated
interface CitizenStateController {

    /**
     * Updates the state of a citizen in the verification workflow.
     *
     * @param id ID of the citizen
     * @param stateUpdateDto DTO with new state and optional note
     * @param currentUserId ID of the administrator making the change
     * @return The updated citizen data
     */
    @Operation(
        summary = "Update citizen state",
        description = "Updates the state of a citizen in the verification workflow",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "State updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid state transition"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Citizen not found")
        ]
    )
    @PutMapping(
        path = ["/citizens/{id}/state"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateCitizenState(
        @Parameter(description = "Citizen ID", required = true)
        @PathVariable("id") id: UUID,
        
        @Parameter(description = "State update data", required = true)
        @Valid @RequestBody stateUpdateDto: StateUpdateDto,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Updates the state of a citizen's document.
     *
     * @param id ID of the citizen
     * @param documentType Type of document
     * @param documentStateUpdateDto DTO with new state and optional note
     * @param currentUserId ID of the administrator making the change
     * @return The updated citizen data
     */
    @Operation(
        summary = "Update document state",
        description = "Updates the state of a citizen's document",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Document state updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid document state"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Citizen or document not found")
        ]
    )
    @PutMapping(
        path = ["/citizens/{id}/documents/{documentType}/state"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateDocumentState(
        @Parameter(description = "Citizen ID", required = true)
        @PathVariable("id") id: UUID,
        
        @Parameter(description = "Document type", required = true)
        @PathVariable("documentType") documentType: DocumentType,
        
        @Parameter(description = "Document state update data", required = true)
        @Valid @RequestBody documentStateUpdateDto: DocumentStateUpdateDto,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Gets citizens requiring administrative action.
     *
     * @param page Page number (1-based)
     * @param size Page size
     * @return List of citizens requiring action
     */
    @Operation(
        summary = "Get citizens requiring action",
        description = "Returns citizens in PENDING_REGISTRATION or ACTION_REQUIRED states",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Citizens retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping(
        path = ["/citizens/requiring-action"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCitizensRequiringAction(
        @Parameter(description = "Page number (1-based)", example = "1")
        @RequestParam(defaultValue = "1") @Min(1) page: Int,
        
        @Parameter(description = "Page size", example = "10")
        @RequestParam(defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<DpisApiResponse<List<CitizenResponse>>>

    /**
     * Gets citizens in a specific state.
     *
     * @param state The state to filter by
     * @param page Page number (1-based)
     * @param size Page size
     * @return List of citizens in the specified state
     */
    @Operation(
        summary = "Get citizens by state",
        description = "Returns citizens in the specified state",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Citizens retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping(
        path = ["/citizens/by-state/{state}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCitizensByState(
        @Parameter(description = "Citizen state", required = true)
        @PathVariable("state") state: CitizenState,
        
        @Parameter(description = "Page number (1-based)", example = "1")
        @RequestParam(defaultValue = "1") @Min(1) page: Int,
        
        @Parameter(description = "Page size", example = "10")
        @RequestParam(defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<DpisApiResponse<List<CitizenResponse>>>

    /**
     * Gets citizens with documents in a specific state.
     *
     * @param documentState The document state to filter by
     * @param page Page number (1-based)
     * @param size Page size
     * @return List of citizens with documents in the specified state
     */
    @Operation(
        summary = "Get citizens by document state",
        description = "Returns citizens with documents in the specified state",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Citizens retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping(
        path = ["/citizens/by-document-state/{documentState}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCitizensByDocumentState(
        @Parameter(description = "Document state", required = true)
        @PathVariable("documentState") documentState: DocumentState,
        
        @Parameter(description = "Page number (1-based)", example = "1")
        @RequestParam(defaultValue = "1") @Min(1) page: Int,
        
        @Parameter(description = "Page size", example = "10")
        @RequestParam(defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<DpisApiResponse<List<CitizenResponse>>>

    /**
     * Gets citizens with specific issues in their documents, using a note keyword search.
     *
     * @param noteKeyword Keyword to search for in document state notes
     * @param page Page number (1-based)
     * @param size Page size
     * @return List of citizens with matching document state notes
     */
    @Operation(
        summary = "Get citizens by document note",
        description = "Returns citizens with document states containing the specified note keyword",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Citizens retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping(
        path = ["/citizens/by-document-note"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCitizensByDocumentStateNote(
        @Parameter(description = "Keyword to search in document notes", required = true)
        @RequestParam("keyword") noteKeyword: String,
        
        @Parameter(description = "Page number (1-based)", example = "1")
        @RequestParam(defaultValue = "1") @Min(1) page: Int,
        
        @Parameter(description = "Page size", example = "10")
        @RequestParam(defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<DpisApiResponse<List<CitizenResponse>>>
}
