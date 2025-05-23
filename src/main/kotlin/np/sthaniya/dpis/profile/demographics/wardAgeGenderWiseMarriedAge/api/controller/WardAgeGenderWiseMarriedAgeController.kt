package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.CreateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.UpdateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.WardAgeGenderWiseMarriedAgeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardAgeGenderWiseMarriedAgeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-age-gender-wise married age data.
 */
@Tag(name = "Ward-Age-Gender-Wise Married Age", description = "APIs for managing ward-age-gender-wise married age data")
@RequestMapping("/api/v1/profile/demographics/ward-age-gender-wise-married-age")
interface WardAgeGenderWiseMarriedAgeController {

    /**
     * Creates a new ward-age-gender-wise married age entry.
     *
     * This endpoint allows administrators to create new records for ward-age-gender-wise married age data.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-age-gender-wise married age to create
     * @return The created ward-age-gender-wise married age entry
     */
    @Operation(
        summary = "Create ward-age-gender-wise married age",
        description = "Creates a new entry for ward-age-gender-wise married age data"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardAgeGenderWiseMarriedAge(
        @Valid @RequestBody request: CreateWardAgeGenderWiseMarriedAgeDto
    ): ResponseEntity<DpisApiResponse<WardAgeGenderWiseMarriedAgeResponse>>

    /**
     * Updates an existing ward-age-gender-wise married age entry.
     *
     * This endpoint allows administrators to update existing records for ward-age-gender-wise married age data.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-age-gender-wise married age entry to update
     * @param request The new details for the ward-age-gender-wise married age
     * @return The updated ward-age-gender-wise married age entry
     */
    @Operation(
        summary = "Update ward-age-gender-wise married age",
        description = "Updates an existing ward-age-gender-wise married age entry"
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
    fun updateWardAgeGenderWiseMarriedAge(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardAgeGenderWiseMarriedAgeDto
    ): ResponseEntity<DpisApiResponse<WardAgeGenderWiseMarriedAgeResponse>>

    /**
     * Deletes a ward-age-gender-wise married age entry.
     *
     * This endpoint allows administrators to delete records for ward-age-gender-wise married age data.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-age-gender-wise married age entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-age-gender-wise married age",
        description = "Deletes a ward-age-gender-wise married age entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardAgeGenderWiseMarriedAge(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-age-gender-wise married age entry by ID.
     *
     * This endpoint returns the details of a specific ward-age-gender-wise married age entry.
     *
     * @param id The ID of the ward-age-gender-wise married age entry
     * @return The ward-age-gender-wise married age entry details
     */
    @Operation(
        summary = "Get ward-age-gender-wise married age by ID",
        description = "Retrieves a ward-age-gender-wise married age entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // Public access
    @GetMapping("/{id}")
    fun getWardAgeGenderWiseMarriedAgeById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardAgeGenderWiseMarriedAgeResponse>>

    /**
     * Retrieves all ward-age-gender-wise married age entries with optional filtering.
     *
     * This endpoint returns a list of ward-age-gender-wise married age entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-age-gender-wise married age entries
     */
    @Operation(
        summary = "Get all ward-age-gender-wise married age data",
        description = "Retrieves all ward-age-gender-wise married age entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Public access
    @GetMapping
    fun getAllWardAgeGenderWiseMarriedAge(
        @Valid filter: WardAgeGenderWiseMarriedAgeFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>>

    /**
     * Retrieves all ward-age-gender-wise married age entries for a specific ward.
     *
     * This endpoint returns a list of ward-age-gender-wise married age entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-age-gender-wise married age entries
     */
    @Operation(
        summary = "Get ward-age-gender-wise married age by ward",
        description = "Retrieves all ward-age-gender-wise married age entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Public access
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardAgeGenderWiseMarriedAgeByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>>

    /**
     * Retrieves all ward-age-gender-wise married age entries for a specific age group.
     *
     * This endpoint returns a list of ward-age-gender-wise married age entries for a given age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of ward-age-gender-wise married age entries
     */
    @Operation(
        summary = "Get ward-age-gender-wise married age by age group",
        description = "Retrieves all ward-age-gender-wise married age entries for a specific age group"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Public access
    @GetMapping("/by-age-group/{ageGroup}")
    fun getWardAgeGenderWiseMarriedAgeByAgeGroup(
        @PathVariable ageGroup: MarriedAgeGroup
    ): ResponseEntity<DpisApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>>

    /**
     * Retrieves all ward-age-gender-wise married age entries for a specific gender.
     *
     * This endpoint returns a list of ward-age-gender-wise married age entries for a given gender.
     *
     * @param gender The gender to filter by
     * @return List of ward-age-gender-wise married age entries
     */
    @Operation(
        summary = "Get ward-age-gender-wise married age by gender",
        description = "Retrieves all ward-age-gender-wise married age entries for a specific gender"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Public access
    @GetMapping("/by-gender/{gender}")
    fun getWardAgeGenderWiseMarriedAgeByGender(
        @PathVariable gender: Gender
    ): ResponseEntity<DpisApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>>

    /**
     * Gets summary of age group and gender population across all wards.
     *
     * @return List of age group and gender summaries
     */
    @Operation(
        summary = "Get age group and gender population summary",
        description = "Retrieves summary of age group and gender population statistics across all wards."
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
    // Public access
    @GetMapping(
        path = ["/summary/by-age-group-gender"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAgeGroupGenderPopulationSummary(): ResponseEntity<DpisApiResponse<List<AgeGroupGenderPopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all age groups and genders.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all age groups and genders."
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
    // Public access
    @GetMapping(
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardPopulationSummaryResponse>>>
}
