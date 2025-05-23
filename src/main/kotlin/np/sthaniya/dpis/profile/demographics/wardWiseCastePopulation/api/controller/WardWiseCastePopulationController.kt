package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.CreateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.UpdateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.WardWiseCastePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.CastePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardWiseCastePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise caste population data.
 */
@Tag(name = "Ward-wise Caste Population", description = "APIs for managing ward-wise caste population data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-caste-population")
interface WardWiseCastePopulationController {

    /**
     * Creates a new ward-wise caste population entry.
     *
     * This endpoint allows administrators to create new records for ward-wise caste population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise caste population to create
     * @return The created ward-wise caste population entry
     */
    @Operation(
        summary = "Create ward-wise caste population",
        description = "Creates a new entry for ward-wise caste population"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseCastePopulation(
        @Valid @RequestBody request: CreateWardWiseCastePopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseCastePopulationResponse>>

    /**
     * Updates an existing ward-wise caste population entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise caste population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise caste population entry to update
     * @param request The new details for the ward-wise caste population
     * @return The updated ward-wise caste population entry
     */
    @Operation(
        summary = "Update ward-wise caste population",
        description = "Updates an existing ward-wise caste population entry"
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
    fun updateWardWiseCastePopulation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseCastePopulationDto
    ): ResponseEntity<DpisApiResponse<WardWiseCastePopulationResponse>>

    /**
     * Deletes a ward-wise caste population entry.
     *
     * This endpoint allows administrators to delete records for ward-wise caste population.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise caste population entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise caste population",
        description = "Deletes a ward-wise caste population entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseCastePopulation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise caste population entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise caste population entry.
     *
     * @param id The ID of the ward-wise caste population entry
     * @return The ward-wise caste population entry details
     */
    @Operation(
        summary = "Get ward-wise caste population by ID",
        description = "Retrieves a ward-wise caste population entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseCastePopulationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseCastePopulationResponse>>

    /**
     * Retrieves all ward-wise caste population entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise caste population entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise caste population entries
     */
    @Operation(
        summary = "Get all ward-wise caste population",
        description = "Retrieves all ward-wise caste population entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseCastePopulation(
        @Valid filter: WardWiseCastePopulationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseCastePopulationResponse>>>

    /**
     * Retrieves all ward-wise caste population entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise caste population entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise caste population entries
     */
    @Operation(
        summary = "Get ward-wise caste population by ward",
        description = "Retrieves all ward-wise caste population entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseCastePopulationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseCastePopulationResponse>>>

    /**
     * Retrieves all ward-wise caste population entries for a specific caste type.
     *
     * This endpoint returns a list of ward-wise caste population entries for a given caste type.
     *
     * @param casteType The caste type to filter by
     * @return List of ward-wise caste population entries
     */
    @Operation(
        summary = "Get ward-wise caste population by caste",
        description = "Retrieves all ward-wise caste population entries for a specific caste type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-caste/{casteType}")
    fun getWardWiseCastePopulationByCaste(
        @PathVariable casteType: CasteType
    ): ResponseEntity<DpisApiResponse<List<WardWiseCastePopulationResponse>>>

    /**
     * Gets summary of caste population across all wards.
     *
     * @return List of caste summaries
     */
    @Operation(
        summary = "Get caste population summary",
        description = "Retrieves summary of caste population statistics across all wards."
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
        path = ["/summary/by-caste"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCastePopulationSummary(): ResponseEntity<DpisApiResponse<List<CastePopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all castes.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all castes."
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
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardPopulationSummaryResponse>>>
}
