package np.sthaniya.dpis.profile.demographics.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.dto.request.CreateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.UpdateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.WardWiseReligionPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.dto.response.ReligionPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardWiseReligionPopulationResponse
import np.sthaniya.dpis.profile.demographics.model.ReligionType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise religion population data.
 */
@Tag(name = "Ward-wise Religion Population", description = "APIs for managing ward-wise religion population data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-religion-population")
interface WardWiseReligionPopulationController {

    /**
     * Creates a new ward-wise religion population entry.
     *
     * This endpoint allows administrators to create new records for ward-wise religion population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise religion population to create
     * @return The created ward-wise religion population entry
     */
    @Operation(
        summary = "Create ward-wise religion population",
        description = "Creates a new entry for ward-wise religion population"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseReligionPopulation(
        @Valid @RequestBody request: CreateWardWiseReligionPopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseReligionPopulationResponse>>

    /**
     * Updates an existing ward-wise religion population entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise religion population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise religion population entry to update
     * @param request The new details for the ward-wise religion population
     * @return The updated ward-wise religion population entry
     */
    @Operation(
        summary = "Update ward-wise religion population",
        description = "Updates an existing ward-wise religion population entry"
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
    fun updateWardWiseReligionPopulation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseReligionPopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseReligionPopulationResponse>>

    /**
     * Deletes a ward-wise religion population entry.
     *
     * This endpoint allows administrators to delete records for ward-wise religion population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise religion population entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise religion population",
        description = "Deletes a ward-wise religion population entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseReligionPopulation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise religion population entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise religion population entry.
     *
     * Security:
     * - Requires VIEW_DEMOGRAPHIC_DATA permission
     *
     * @param id The ID of the ward-wise religion population entry
     * @return The ward-wise religion population entry details
     */
    @Operation(
        summary = "Get ward-wise religion population by ID",
        description = "Retrieves a ward-wise religion population entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    // @PreAuthorize("hasPermission(null, 'VIEW_DEMOGRAPHIC_DATA')")
    @GetMapping("/{id}")
    fun getWardWiseReligionPopulationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseReligionPopulationResponse>>

    /**
     * Retrieves all ward-wise religion population entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise religion population entries.
     *
     * Security:
     * - Requires VIEW_DEMOGRAPHIC_DATA permission
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise religion population entries
     */
    @Operation(
        summary = "Get all ward-wise religion population",
        description = "Retrieves all ward-wise religion population entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    // @PreAuthorize("hasPermission(null, 'VIEW_DEMOGRAPHIC_DATA')")
    @GetMapping
    fun getAllWardWiseReligionPopulation(
        @Valid filter: WardWiseReligionPopulationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseReligionPopulationResponse>>>

    /**
     * Retrieves all ward-wise religion population entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise religion population entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise religion population entries
     */
    @Operation(
        summary = "Get ward-wise religion population by ward",
        description = "Retrieves all ward-wise religion population entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseReligionPopulationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseReligionPopulationResponse>>>

    /**
     * Retrieves all ward-wise religion population entries for a specific religion type.
     *
     * This endpoint returns a list of ward-wise religion population entries for a given religion type.
     *
     * @param religionType The religion type to filter by
     * @return List of ward-wise religion population entries
     */
    @Operation(
        summary = "Get ward-wise religion population by religion",
        description = "Retrieves all ward-wise religion population entries for a specific religion type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-religion/{religionType}")
    fun getWardWiseReligionPopulationByReligion(
        @PathVariable religionType: ReligionType
    ): ResponseEntity<DpisApiResponse<List<WardWiseReligionPopulationResponse>>>

    /**
     * Gets summary of religion population across all wards.
     *
     * @return List of religion summaries
     */
    @Operation(
        summary = "Get religion population summary",
        description = "Retrieves summary of religion population statistics across all wards. " +
                "Requires VIEW_DEMOGRAPHIC_DATA permission.",
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
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    // We want the general public to be able to see this data
    // @PreAuthorize("hasAuthority('PERMISSION_VIEW_DEMOGRAPHIC_DATA')")
    @GetMapping(
        path = ["/summary/by-religion"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getReligionPopulationSummary(): ResponseEntity<DpisApiResponse<List<ReligionPopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all religions.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all religions. " +
                "Requires VIEW_DEMOGRAPHIC_DATA permission.",
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
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    // We want the general public to be able to see this data
    // @PreAuthorize("hasAuthority('PERMISSION_VIEW_DEMOGRAPHIC_DATA')")
    @GetMapping(
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardPopulationSummaryResponse>>>
}
