package np.sthaniya.dpis.profile.demographics.demographicSummary.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateSingleFieldDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Controller interface for managing demographic summary data.
 */
@Tag(name = "Demographic Summary", description = "APIs for managing demographic summary data")
@RequestMapping("/api/v1/profile/demographics/summary")
interface DemographicSummaryController {

    /**
     * Gets the demographic summary data.
     *
     * This endpoint returns the current demographic summary data for the municipality.
     *
     * @return The demographic summary data
     */
    @Operation(
        summary = "Get demographic summary",
        description = "Retrieves the demographic summary data for the municipality"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "404", description = "No demographic summary found")
    ])
    @GetMapping
    fun getDemographicSummary(): ResponseEntity<DpisApiResponse<DemographicSummaryResponse>>

    /**
     * Updates the demographic summary data.
     *
     * This endpoint allows administrators to update the demographic summary data.
     * If no demographic summary exists, a new one will be created.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The new demographic summary data
     * @return The updated demographic summary data
     */
    @Operation(
        summary = "Update demographic summary",
        description = "Updates the demographic summary data for the municipality"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Updated successfully"),
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    fun updateDemographicSummary(
        @Valid @RequestBody request: UpdateDemographicSummaryDto
    ): ResponseEntity<DpisApiResponse<DemographicSummaryResponse>>

    /**
     * Updates a single field in the demographic summary data.
     *
     * This endpoint allows administrators to update a single field in the demographic summary.
     * If no demographic summary exists, a new one will be created.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The field and value to update
     * @return The updated demographic summary data
     */
    @Operation(
        summary = "Update a single field in demographic summary",
        description = "Updates a single field in the demographic summary data"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Updated successfully"),
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/field")
    fun updateSingleField(
        @Valid @RequestBody request: UpdateSingleFieldDto
    ): ResponseEntity<DpisApiResponse<DemographicSummaryResponse>>
}
