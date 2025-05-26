package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.CreateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.UpdateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.WardWiseTrainedPopulationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.TrainedPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardWiseTrainedPopulationResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise trained population data.
 */
@Tag(name = "Ward-wise Trained Population", description = "APIs for managing ward-wise trained population data")
@RequestMapping("/api/v1/profile/economics/ward-wise-trained-population")
interface WardWiseTrainedPopulationController {

    /**
     * Creates a new ward-wise trained population entry.
     *
     * This endpoint allows administrators to create new records for ward-wise trained population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise trained population to create
     * @return The created ward-wise trained population entry
     */
    @Operation(
        summary = "Create ward-wise trained population",
        description = "Creates a new entry for ward-wise trained population"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseTrainedPopulation(
        @Valid @RequestBody request: CreateWardWiseTrainedPopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseTrainedPopulationResponse>>

    /**
     * Updates an existing ward-wise trained population entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise trained population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise trained population entry to update
     * @param request The new details for the ward-wise trained population
     * @return The updated ward-wise trained population entry
     */
    @Operation(
        summary = "Update ward-wise trained population",
        description = "Updates an existing ward-wise trained population entry"
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
    fun updateWardWiseTrainedPopulation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseTrainedPopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseTrainedPopulationResponse>>

    /**
     * Deletes a ward-wise trained population entry.
     *
     * This endpoint allows administrators to delete records for ward-wise trained population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise trained population entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise trained population",
        description = "Deletes a ward-wise trained population entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseTrainedPopulation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise trained population entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise trained population entry.
     *
     * @param id The ID of the ward-wise trained population entry
     * @return The ward-wise trained population entry details
     */
    @Operation(
        summary = "Get ward-wise trained population by ID",
        description = "Retrieves a ward-wise trained population entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @GetMapping("/{id}")
    fun getWardWiseTrainedPopulationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseTrainedPopulationResponse>>

    /**
     * Retrieves all ward-wise trained population entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise trained population entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise trained population entries
     */
    @Operation(
        summary = "Get all ward-wise trained population",
        description = "Retrieves all ward-wise trained population entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @GetMapping
    fun getAllWardWiseTrainedPopulation(
        @Valid filter: WardWiseTrainedPopulationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseTrainedPopulationResponse>>>

    /**
     * Retrieves ward-wise trained population entry for a specific ward.
     *
     * This endpoint returns the ward-wise trained population for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return Ward-wise trained population entry
     */
    @Operation(
        summary = "Get ward-wise trained population by ward",
        description = "Retrieves ward-wise trained population for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseTrainedPopulationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<WardWiseTrainedPopulationResponse?>>

    /**
     * Gets summary of total trained population across all wards.
     *
     * @return Summary response with total trained population
     */
    @Operation(
        summary = "Get trained population summary",
        description = "Retrieves summary of total trained population across all wards."
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
    @GetMapping(
        path = ["/summary/total"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTrainedPopulationSummary(): ResponseEntity<DpisApiResponse<TrainedPopulationSummaryResponse>>

    /**
     * Gets summary of trained population by ward.
     *
     * @return List of ward summaries with trained population
     */
    @Operation(
        summary = "Get ward trained population summary",
        description = "Retrieves summary of trained population by ward."
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
    @GetMapping(
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardTrainedPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardTrainedPopulationResponse>>>
}
