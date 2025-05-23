package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.CreateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.UpdateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.WardAgeWisePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.AgeGroupPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeGenderSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeWisePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-age-wise population data.
 */
@Tag(name = "Ward-age-wise Population", description = "APIs for managing ward-age-wise population data")
@RequestMapping("/api/v1/profile/demographics/ward-age-wise-population")
interface WardAgeWisePopulationController {

    /**
     * Creates a new ward-age-wise population entry.
     *
     * This endpoint allows administrators to create new records for ward-age-wise population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-age-wise population to create
     * @return The created ward-age-wise population entry
     */
    @Operation(
        summary = "Create ward-age-wise population",
        description = "Creates a new entry for ward-age-wise population"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardAgeWisePopulation(
        @Valid @RequestBody request: CreateWardAgeWisePopulationDto
    ): ResponseEntity<DpisApiResponse<WardAgeWisePopulationResponse>>

    /**
     * Updates an existing ward-age-wise population entry.
     *
     * This endpoint allows administrators to update existing records for ward-age-wise population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-age-wise population entry to update
     * @param request The new details for the ward-age-wise population
     * @return The updated ward-age-wise population entry
     */
    @Operation(
        summary = "Update ward-age-wise population",
        description = "Updates an existing ward-age-wise population entry"
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
    fun updateWardAgeWisePopulation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardAgeWisePopulationDto
    ): ResponseEntity<DpisApiResponse<WardAgeWisePopulationResponse>>

    /**
     * Deletes a ward-age-wise population entry.
     *
     * This endpoint allows administrators to delete records for ward-age-wise population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-age-wise population entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-age-wise population",
        description = "Deletes a ward-age-wise population entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardAgeWisePopulation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-age-wise population entry by ID.
     *
     * This endpoint returns the details of a specific ward-age-wise population entry.
     *
     * @param id The ID of the ward-age-wise population entry
     * @return The ward-age-wise population entry details
     */
    @Operation(
        summary = "Get ward-age-wise population by ID",
        description = "Retrieves a ward-age-wise population entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardAgeWisePopulationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardAgeWisePopulationResponse>>

    /**
     * Retrieves all ward-age-wise population entries with optional filtering.
     *
     * This endpoint returns a list of ward-age-wise population entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-age-wise population entries
     */
    @Operation(
        summary = "Get all ward-age-wise population",
        description = "Retrieves all ward-age-wise population entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardAgeWisePopulation(
        @Valid filter: WardAgeWisePopulationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardAgeWisePopulationResponse>>>

    /**
     * Retrieves all ward-age-wise population entries for a specific ward.
     *
     * This endpoint returns a list of ward-age-wise population entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-age-wise population entries
     */
    @Operation(
        summary = "Get ward-age-wise population by ward",
        description = "Retrieves all ward-age-wise population entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardAgeWisePopulationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardAgeWisePopulationResponse>>>

    /**
     * Retrieves all ward-age-wise population entries for a specific age group.
     *
     * This endpoint returns a list of ward-age-wise population entries for a given age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of ward-age-wise population entries
     */
    @Operation(
        summary = "Get ward-age-wise population by age group",
        description = "Retrieves all ward-age-wise population entries for a specific age group"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-age-group/{ageGroup}")
    fun getWardAgeWisePopulationByAgeGroup(
        @PathVariable ageGroup: AgeGroup
    ): ResponseEntity<DpisApiResponse<List<WardAgeWisePopulationResponse>>>

    /**
     * Retrieves all ward-age-wise population entries for a specific gender.
     *
     * This endpoint returns a list of ward-age-wise population entries for a given gender.
     *
     * @param gender The gender to filter by
     * @return List of ward-age-wise population entries
     */
    @Operation(
        summary = "Get ward-age-wise population by gender",
        description = "Retrieves all ward-age-wise population entries for a specific gender"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-gender/{gender}")
    fun getWardAgeWisePopulationByGender(
        @PathVariable gender: Gender
    ): ResponseEntity<DpisApiResponse<List<WardAgeWisePopulationResponse>>>

    /**
     * Gets summary of population by age group across all wards.
     *
     * @return List of age group summaries
     */
    @Operation(
        summary = "Get age group population summary",
        description = "Retrieves summary of population statistics by age group across all wards.",
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
    @GetMapping(
        path = ["/summary/by-age-group"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAgeGroupPopulationSummary(): ResponseEntity<DpisApiResponse<List<AgeGroupPopulationSummaryResponse>>>

    /**
     * Gets summary of population by gender across all wards.
     *
     * @return List of gender summaries
     */
    @Operation(
        summary = "Get gender population summary",
        description = "Retrieves summary of population statistics by gender across all wards.",
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
    @GetMapping(
        path = ["/summary/by-gender"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getGenderPopulationSummary(): ResponseEntity<DpisApiResponse<List<GenderPopulationSummaryResponse>>>

    /**
     * Gets detailed summary of population by ward, age group, and gender.
     *
     * @return List of detailed summaries
     */
    @Operation(
        summary = "Get ward-age-gender population summary",
        description = "Retrieves detailed summary of population statistics by ward, age group, and gender.",
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
    @GetMapping(
        path = ["/summary/detailed"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardAgeGenderSummary(): ResponseEntity<DpisApiResponse<List<WardAgeGenderSummaryResponse>>>
}
