package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.CreateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.UpdateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.WardAgeWiseMaritalStatusFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.AgeGroupSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.MaritalStatusSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.WardAgeWiseMaritalStatusResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward age-wise marital status data.
 */
@Tag(name = "Ward Age-wise Marital Status", description = "APIs for managing ward age-wise marital status data")
@RequestMapping("/api/v1/profile/demographics/ward-age-wise-marital-status")
interface WardAgeWiseMaritalStatusController {

    /**
     * Creates a new ward age-wise marital status entry.
     *
     * This endpoint allows administrators to create new records for ward age-wise marital status.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward age-wise marital status to create
     * @return The created ward age-wise marital status entry
     */
    @Operation(
        summary = "Create ward age-wise marital status",
        description = "Creates a new entry for ward age-wise marital status"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardAgeWiseMaritalStatus(
        @Valid @RequestBody request: CreateWardAgeWiseMaritalStatusDto
    ): ResponseEntity<DpisApiResponse<WardAgeWiseMaritalStatusResponse>>

    /**
     * Updates an existing ward age-wise marital status entry.
     *
     * This endpoint allows administrators to update existing records for ward age-wise marital status.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward age-wise marital status entry to update
     * @param request The new details for the ward age-wise marital status
     * @return The updated ward age-wise marital status entry
     */
    @Operation(
        summary = "Update ward age-wise marital status",
        description = "Updates an existing ward age-wise marital status entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Updated successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun updateWardAgeWiseMaritalStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardAgeWiseMaritalStatusDto
    ): ResponseEntity<DpisApiResponse<WardAgeWiseMaritalStatusResponse>>

    /**
     * Deletes a ward age-wise marital status entry.
     *
     * This endpoint allows administrators to delete records for ward age-wise marital status.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward age-wise marital status entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward age-wise marital status",
        description = "Deletes a ward age-wise marital status entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardAgeWiseMaritalStatus(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward age-wise marital status entry by ID.
     *
     * This endpoint returns the details of a specific ward age-wise marital status entry.
     *
     * @param id The ID of the ward age-wise marital status entry
     * @return The ward age-wise marital status entry details
     */
    @Operation(
        summary = "Get ward age-wise marital status by ID",
        description = "Retrieves a ward age-wise marital status entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardAgeWiseMaritalStatusById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardAgeWiseMaritalStatusResponse>>

    /**
     * Retrieves all ward age-wise marital status entries with optional filtering.
     *
     * This endpoint returns a list of ward age-wise marital status entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward age-wise marital status entries
     */
    @Operation(
        summary = "Get all ward age-wise marital status",
        description = "Retrieves all ward age-wise marital status entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardAgeWiseMaritalStatus(
        @Valid filter: WardAgeWiseMaritalStatusFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardAgeWiseMaritalStatusResponse>>>

    /**
     * Retrieves all ward age-wise marital status entries for a specific ward.
     *
     * This endpoint returns a list of ward age-wise marital status entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward age-wise marital status entries
     */
    @Operation(
        summary = "Get ward age-wise marital status by ward",
        description = "Retrieves all ward age-wise marital status entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardAgeWiseMaritalStatusByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardAgeWiseMaritalStatusResponse>>>

    /**
     * Retrieves all ward age-wise marital status entries for a specific age group.
     *
     * This endpoint returns a list of ward age-wise marital status entries for a given age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of ward age-wise marital status entries
     */
    @Operation(
        summary = "Get ward age-wise marital status by age group",
        description = "Retrieves all ward age-wise marital status entries for a specific age group"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-age-group/{ageGroup}")
    fun getWardAgeWiseMaritalStatusByAgeGroup(
        @PathVariable ageGroup: MaritalAgeGroup
    ): ResponseEntity<DpisApiResponse<List<WardAgeWiseMaritalStatusResponse>>>

    /**
     * Retrieves all ward age-wise marital status entries for a specific marital status.
     *
     * This endpoint returns a list of ward age-wise marital status entries for a given marital status.
     *
     * @param maritalStatus The marital status to filter by
     * @return List of ward age-wise marital status entries
     */
    @Operation(
        summary = "Get ward age-wise marital status by marital status",
        description = "Retrieves all ward age-wise marital status entries for a specific marital status"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-marital-status/{maritalStatus}")
    fun getWardAgeWiseMaritalStatusByMaritalStatus(
        @PathVariable maritalStatus: MaritalStatusGroup
    ): ResponseEntity<DpisApiResponse<List<WardAgeWiseMaritalStatusResponse>>>

    /**
     * Gets summary of marital status population across all wards and age groups.
     *
     * @return List of marital status summaries
     */
    @Operation(
        summary = "Get marital status population summary",
        description = "Retrieves summary of marital status population statistics across all wards and age groups",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Summary retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    // We want the general public to be able to see this data
    @GetMapping(
        path = ["/summary/by-marital-status"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMaritalStatusSummary(): ResponseEntity<DpisApiResponse<List<MaritalStatusSummaryResponse>>>

    /**
     * Gets summary of age group population across all wards and marital statuses.
     *
     * @return List of age group summaries
     */
    @Operation(
        summary = "Get age group population summary",
        description = "Retrieves summary of age group population statistics across all wards and marital statuses",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Summary retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    // We want the general public to be able to see this data
    @GetMapping(
        path = ["/summary/by-age-group"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAgeGroupSummary(): ResponseEntity<DpisApiResponse<List<AgeGroupSummaryResponse>>>
}
