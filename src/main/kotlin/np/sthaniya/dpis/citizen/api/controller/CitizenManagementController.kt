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
import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.auth.resolver.CurrentUserId
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Controller interface for administrative management of citizen records.
 * 
 * Provides endpoints for creating and managing citizen data by authorized
 * administrative users. These endpoints require appropriate permissions.
 */
@Tag(name = "Citizen Management", description = "APIs for administrative management of citizen records")
@RequestMapping("/api/v1/admin/citizens")
interface CitizenManagementController {

    /**
     * Creates a new citizen record in the system.
     * 
     * @param createCitizenDto The data for creating a new citizen
     * @return The created citizen data with generated ID
     */
    @Operation(
        summary = "Create a new citizen record",
        description = "Creates a new citizen record with the provided information. " +
                "Requires CREATE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", 
                description = "Citizen created successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "409", description = "Conflict - Citizen with same email or citizenship number already exists")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_CITIZEN')")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createCitizen(
        @Parameter(
            description = "Citizen creation data",
            required = true
        )
        @Valid @RequestBody createCitizenDto: CreateCitizenDto
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Updates an existing citizen record in the system.
     * 
     * @param id The unique identifier of the citizen to update
     * @param updateCitizenDto The data for updating the citizen
     * @return The updated citizen data
     */
    @Operation(
        summary = "Update an existing citizen record",
        description = "Updates an existing citizen record with the provided information. " +
                "Requires EDIT_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Citizen updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Citizen with same email or citizenship number already exists")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_CITIZEN')")
    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateCitizen(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @Parameter(
            description = "Citizen update data",
            required = true
        )
        @Valid @RequestBody updateCitizenDto: UpdateCitizenDto
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Retrieves a citizen record by ID.
     * 
     * @param id The unique identifier of the citizen to retrieve
     * @return The citizen data if found
     */
    @Operation(
        summary = "Get a citizen record by ID",
        description = "Retrieves details of a specific citizen record. " +
                "Requires VIEW_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Citizen found and returned successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_CITIZEN')")
    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCitizenById(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Approves a citizen record.
     * 
     * @param id The unique identifier of the citizen to approve
     * @param currentUserId ID of the administrator approving the citizen
     * @return The approved citizen data
     */
    @Operation(
        summary = "Approve a citizen record",
        description = "Approves an existing citizen record. " +
                "Requires APPROVE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Citizen approved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Citizen is already approved")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_APPROVE_CITIZEN')")
    @PostMapping(
        path = ["/{id}/approve"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun approveCitizen(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Deletes a citizen record.
     * 
     * @param id The unique identifier of the citizen to delete
     * @param currentUserId ID of the administrator deleting the citizen
     * @return The deleted citizen data
     */
    @Operation(
        summary = "Delete a citizen record",
        description = "Soft deletes a citizen record. " +
                "Requires DELETE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Citizen deleted successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Citizen is already deleted")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_CITIZEN')")
    @DeleteMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteCitizen(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Uploads a citizen's photo.
     *
     * @param id The unique identifier of the citizen
     * @param photo The photo file to upload
     * @param currentUserId ID of the administrator uploading the document
     * @return Details about the uploaded photo
     */
    @Operation(
        summary = "Upload citizen photo",
        description = "Upload or replace a citizen's photo. " +
                "Requires UPDATE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Photo uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_CITIZEN')")
    @PostMapping(
        path = ["/{id}/photo"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadCitizenPhoto(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @Parameter(
            description = "Photo file to upload (JPG or PNG format, max 5MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("photo") photo: MultipartFile,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>

    /**
     * Uploads the front page of a citizen's citizenship certificate.
     *
     * @param id The unique identifier of the citizen
     * @param document The document file to upload
     * @param currentUserId ID of the administrator uploading the document
     * @return Details about the uploaded document
     */
    @Operation(
        summary = "Upload citizenship certificate front page",
        description = "Upload or replace the front page of a citizen's citizenship certificate. " +
                "Requires UPDATE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Document uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_CITIZEN')")
    @PostMapping(
        path = ["/{id}/citizenship/front"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadCitizenshipFront(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @Parameter(
            description = "Document file to upload (JPG, PNG or PDF format, max 10MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("document") document: MultipartFile,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>

    /**
     * Uploads the back page of a citizen's citizenship certificate.
     *
     * @param id The unique identifier of the citizen
     * @param document The document file to upload
     * @param currentUserId ID of the administrator uploading the document
     * @return Details about the uploaded document
     */
    @Operation(
        summary = "Upload citizenship certificate back page",
        description = "Upload or replace the back page of a citizen's citizenship certificate. " +
                "Requires UPDATE_CITIZEN permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Document uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not Found - Citizen does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_CITIZEN')")
    @PostMapping(
        path = ["/{id}/citizenship/back"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadCitizenshipBack(
        @Parameter(
            description = "Unique identifier of the citizen",
            required = true
        )
        @PathVariable id: UUID,
        
        @Parameter(
            description = "Document file to upload (JPG, PNG or PDF format, max 10MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("document") document: MultipartFile,
        
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>
}
