package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing cooperatives.
 */
@Tag(name = "Cooperatives", description = "APIs for managing cooperatives")
@RequestMapping("/api/v1/cooperatives")
interface CooperativeController {

    /**
     * Creates a new cooperative.
     *
     * @param createDto The DTO containing the cooperative data
     * @return The created cooperative
     */
    @Operation(
        summary = "Create a new cooperative",
        description = "Creates a new cooperative with the provided data. " +
                "Requires CREATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Cooperative created successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "409", description = "Conflict - Cooperative with same code already exists")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_COOPERATIVE')")
    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createCooperative(
        @Parameter(description = "Cooperative creation data", required = true)
        @Valid @RequestBody createDto: CreateCooperativeDto
    ): ResponseEntity<DpisApiResponse<CooperativeResponse>>

    /**
     * Updates an existing cooperative.
     *
     * @param id The ID of the cooperative to update
     * @param updateDto The DTO containing the updated data
     * @return The updated cooperative
     */
    @Operation(
        summary = "Update a cooperative",
        description = "Updates an existing cooperative with the provided data. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperative updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Cooperative with same code already exists")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PutMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateCooperative(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable id: UUID,
        @Parameter(description = "Cooperative update data", required = true)
        @Valid @RequestBody updateDto: UpdateCooperativeDto
    ): ResponseEntity<DpisApiResponse<CooperativeResponse>>

    /**
     * Retrieves a cooperative by its ID.
     *
     * @param id The ID of the cooperative to retrieve
     * @return The cooperative data
     */
    @Operation(
        summary = "Get a cooperative by ID",
        description = "Retrieves a cooperative by its unique identifier. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperative retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativeById(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<CooperativeResponse>>

    /**
     * Retrieves a cooperative by its code.
     *
     * @param code The code of the cooperative to retrieve
     * @return The cooperative data
     */
    @Operation(
        summary = "Get a cooperative by code",
        description = "Retrieves a cooperative by its unique code. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperative retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/code/{code}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativeByCode(
        @Parameter(description = "Cooperative code", required = true)
        @PathVariable code: String
    ): ResponseEntity<DpisApiResponse<CooperativeResponse>>

    /**
     * Deletes a cooperative.
     *
     * @param id The ID of the cooperative to delete
     * @return A success response
     */
    @Operation(
        summary = "Delete a cooperative",
        description = "Deletes a cooperative from the system. " +
                "Requires DELETE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperative deleted successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_COOPERATIVE')")
    @DeleteMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteCooperative(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves all cooperatives with pagination.
     *
     * @param pageable Pagination information
     * @return A paginated list of cooperatives
     */
    @Operation(
        summary = "Get all cooperatives",
        description = "Retrieves all cooperatives with pagination support. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperatives retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllCooperatives(
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Changes the status of a cooperative.
     *
     * @param id The ID of the cooperative
     * @param status The new status
     * @return The updated cooperative
     */
    @Operation(
        summary = "Change cooperative status",
        description = "Updates the status of an existing cooperative. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Status changed successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid status"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PatchMapping(
        path = ["/{id}/status"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun changeCooperativeStatus(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable id: UUID,
        @Parameter(description = "New status", required = true)
        @RequestParam status: CooperativeStatus
    ): ResponseEntity<DpisApiResponse<CooperativeResponse>>
}
