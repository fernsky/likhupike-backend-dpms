package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.CreateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.UpdateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.WardWiseDemographicSummaryFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response.WardWiseDemographicSummaryResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise demographic summary data.
 */
@Tag(name = "Ward-wise Demographic Summary", description = "APIs for managing ward-wise demographic summary data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-demographic-summary")
interface WardWiseDemographicSummaryController {

    /**
     * Creates a new ward-wise demographic summary entry.
     *
     * This endpoint allows administrators to create new records for ward-wise demographic summary.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise demographic summary to create
     * @return The created ward-wise demographic summary entry
     */
    @Operation(
        summary = "Create ward-wise demographic summary",
        description = "Creates a new entry for ward-wise demographic summary"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseDemographicSummary(
        @Valid @RequestBody request: CreateWardWiseDemographicSummaryDto
    ): ResponseEntity<DpisApiResponse<WardWiseDemographicSummaryResponse>>

    /**
     * Updates an existing ward-wise demographic summary entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise demographic summary.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise demographic summary entry to update
     * @param request The new details for the ward-wise demographic summary
     * @return The updated ward-wise demographic summary entry
     */
    @Operation(
        summary = "Update ward-wise demographic summary",
        description = "Updates an existing ward-wise demographic summary entry"
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
    fun updateWardWiseDemographicSummary(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseDemographicSummaryDto
    ): ResponseEntity<DpisApiResponse<WardWiseDemographicSummaryResponse>>

    /**
     * Deletes a ward-wise demographic summary entry.
     *
     * This endpoint allows administrators to delete records for ward-wise demographic summary.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise demographic summary entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise demographic summary",
        description = "Deletes a ward-wise demographic summary entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseDemographicSummary(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise demographic summary entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise demographic summary entry.
     *
     * @param id The ID of the ward-wise demographic summary entry
     * @return The ward-wise demographic summary entry details
     */
    @Operation(
        summary = "Get ward-wise demographic summary by ID",
        description = "Retrieves a ward-wise demographic summary entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @GetMapping("/{id}")
    fun getWardWiseDemographicSummaryById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseDemographicSummaryResponse>>

    /**
     * Retrieves all ward-wise demographic summary entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise demographic summary entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise demographic summary entries
     */
    @Operation(
        summary = "Get all ward-wise demographic summary",
        description = "Retrieves all ward-wise demographic summary entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    @GetMapping
    fun getAllWardWiseDemographicSummary(
        @Valid filter: WardWiseDemographicSummaryFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseDemographicSummaryResponse>>>

    /**
     * Retrieves the ward-wise demographic summary entry for a specific ward.
     *
     * This endpoint returns demographic summary data for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return Ward-wise demographic summary entry or null if not found
     */
    @Operation(
        summary = "Get ward-wise demographic summary by ward",
        description = "Retrieves the ward-wise demographic summary entry for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseDemographicSummaryByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<WardWiseDemographicSummaryResponse?>>
}
