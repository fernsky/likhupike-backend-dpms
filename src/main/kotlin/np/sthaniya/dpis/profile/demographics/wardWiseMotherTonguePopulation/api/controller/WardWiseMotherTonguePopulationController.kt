package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.CreateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.UpdateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.WardWiseMotherTonguePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.LanguagePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardWiseMotherTonguePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise mother tongue population data.
 */
@Tag(name = "Ward-wise Mother Tongue Population", description = "APIs for managing ward-wise mother tongue population data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-mother-tongue-population")
interface WardWiseMotherTonguePopulationController {

    /**
     * Creates a new ward-wise mother tongue population entry.
     *
     * This endpoint allows administrators to create new records for ward-wise mother tongue population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise mother tongue population to create
     * @return The created ward-wise mother tongue population entry
     */
    @Operation(
        summary = "Create ward-wise mother tongue population",
        description = "Creates a new entry for ward-wise mother tongue population"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseMotherTonguePopulation(
        @Valid @RequestBody request: CreateWardWiseMotherTonguePopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseMotherTonguePopulationResponse>>

    /**
     * Updates an existing ward-wise mother tongue population entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise mother tongue population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise mother tongue population entry to update
     * @param request The new details for the ward-wise mother tongue population
     * @return The updated ward-wise mother tongue population entry
     */
    @Operation(
        summary = "Update ward-wise mother tongue population",
        description = "Updates an existing ward-wise mother tongue population entry"
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
    fun updateWardWiseMotherTonguePopulation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseMotherTonguePopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseMotherTonguePopulationResponse>>

    /**
     * Deletes a ward-wise mother tongue population entry.
     *
     * This endpoint allows administrators to delete records for ward-wise mother tongue population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise mother tongue population entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise mother tongue population",
        description = "Deletes a ward-wise mother tongue population entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseMotherTonguePopulation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise mother tongue population entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise mother tongue population entry.
     *
     * Security:
     * - Requires VIEW_DEMOGRAPHIC_DATA permission
     *
     * @param id The ID of the ward-wise mother tongue population entry
     * @return The ward-wise mother tongue population entry details
     */
    @Operation(
        summary = "Get ward-wise mother tongue population by ID",
        description = "Retrieves a ward-wise mother tongue population entry by its ID"
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
    fun getWardWiseMotherTonguePopulationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseMotherTonguePopulationResponse>>

    /**
     * Retrieves all ward-wise mother tongue population entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise mother tongue population entries.
     *
     * Security:
     * - Requires VIEW_DEMOGRAPHIC_DATA permission
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise mother tongue population entries
     */
    @Operation(
        summary = "Get all ward-wise mother tongue population",
        description = "Retrieves all ward-wise mother tongue population entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    // @PreAuthorize("hasPermission(null, 'VIEW_DEMOGRAPHIC_DATA')")
    @GetMapping
    fun getAllWardWiseMotherTonguePopulation(
        @Valid filter: WardWiseMotherTonguePopulationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseMotherTonguePopulationResponse>>>

    /**
     * Retrieves all ward-wise mother tongue population entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise mother tongue population entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise mother tongue population entries
     */
    @Operation(
        summary = "Get ward-wise mother tongue population by ward",
        description = "Retrieves all ward-wise mother tongue population entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseMotherTonguePopulationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseMotherTonguePopulationResponse>>>

    /**
     * Retrieves all ward-wise mother tongue population entries for a specific language type.
     *
     * This endpoint returns a list of ward-wise mother tongue population entries for a given language type.
     *
     * @param languageType The language type to filter by
     * @return List of ward-wise mother tongue population entries
     */
    @Operation(
        summary = "Get ward-wise mother tongue population by language",
        description = "Retrieves all ward-wise mother tongue population entries for a specific language type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-language/{languageType}")
    fun getWardWiseMotherTonguePopulationByLanguage(
        @PathVariable languageType: LanguageType
    ): ResponseEntity<DpisApiResponse<List<WardWiseMotherTonguePopulationResponse>>>

    /**
     * Gets summary of language population across all wards.
     *
     * @return List of language summaries
     */
    @Operation(
        summary = "Get language population summary",
        description = "Retrieves summary of language population statistics across all wards. " +
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
        path = ["/summary/by-language"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLanguagePopulationSummary(): ResponseEntity<DpisApiResponse<List<LanguagePopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all languages.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all languages. " +
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
