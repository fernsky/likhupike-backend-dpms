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
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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
}
