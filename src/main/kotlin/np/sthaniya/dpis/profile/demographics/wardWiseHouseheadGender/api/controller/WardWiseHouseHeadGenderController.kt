package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.CreateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.UpdateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.WardWiseHouseHeadGenderFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardWiseHouseHeadGenderResponse
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise house head gender data.
 */
@Tag(name = "Ward-wise House Head Gender", description = "APIs for managing ward-wise house head gender data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-househead-gender")
interface WardWiseHouseHeadGenderController {

    /**
     * Creates a new ward-wise house head gender entry.
     *
     * This endpoint allows administrators to create new records for ward-wise house head gender.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise house head gender to create
     * @return The created ward-wise house head gender entry
     */
    @Operation(
        summary = "Create ward-wise house head gender",
        description = "Creates a new entry for ward-wise house head gender"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseHouseHeadGender(
        @Valid @RequestBody request: CreateWardWiseHouseHeadGenderDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseHeadGenderResponse>>

    /**
     * Updates an existing ward-wise house head gender entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise house head gender.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise house head gender entry to update
     * @param request The new details for the ward-wise house head gender
     * @return The updated ward-wise house head gender entry
     */
    @Operation(
        summary = "Update ward-wise house head gender",
        description = "Updates an existing ward-wise house head gender entry"
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
    fun updateWardWiseHouseHeadGender(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseHouseHeadGenderDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseHeadGenderResponse>>

    /**
     * Deletes a ward-wise house head gender entry.
     *
     * This endpoint allows administrators to delete records for ward-wise house head gender.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise house head gender entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise house head gender",
        description = "Deletes a ward-wise house head gender entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseHouseHeadGender(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise house head gender entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise house head gender entry.
     *
     * @param id The ID of the ward-wise house head gender entry
     * @return The ward-wise house head gender entry details
     */
    @Operation(
        summary = "Get ward-wise house head gender by ID",
        description = "Retrieves a ward-wise house head gender entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseHouseHeadGenderById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseHouseHeadGenderResponse>>

    /**
     * Retrieves all ward-wise house head gender entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise house head gender entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise house head gender entries
     */
    @Operation(
        summary = "Get all ward-wise house head gender",
        description = "Retrieves all ward-wise house head gender entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseHouseHeadGender(
        @Valid filter: WardWiseHouseHeadGenderFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseHeadGenderResponse>>>

    /**
     * Retrieves all ward-wise house head gender entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise house head gender entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise house head gender entries
     */
    @Operation(
        summary = "Get ward-wise house head gender by ward",
        description = "Retrieves all ward-wise house head gender entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseHouseHeadGenderByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseHeadGenderResponse>>>

    /**
     * Retrieves all ward-wise house head gender entries for a specific gender.
     *
     * This endpoint returns a list of ward-wise house head gender entries for a given gender.
     *
     * @param gender The gender to filter by
     * @return List of ward-wise house head gender entries
     */
    @Operation(
        summary = "Get ward-wise house head gender by gender",
        description = "Retrieves all ward-wise house head gender entries for a specific gender"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-gender/{gender}")
    fun getWardWiseHouseHeadGenderByGender(
        @PathVariable gender: Gender
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseHeadGenderResponse>>>

    /**
     * Gets summary of gender distribution across all wards.
     *
     * @return List of gender summaries
     */
    @Operation(
        summary = "Get gender population summary",
        description = "Retrieves summary of gender statistics across all wards."
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
        path = ["/summary/by-gender"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getGenderPopulationSummary(): ResponseEntity<DpisApiResponse<List<GenderPopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all genders.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all genders."
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
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardPopulationSummaryResponse>>>
}
