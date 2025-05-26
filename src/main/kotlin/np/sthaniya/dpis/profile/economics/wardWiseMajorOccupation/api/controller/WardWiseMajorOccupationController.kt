package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.CreateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.UpdateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.WardWiseMajorOccupationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.OccupationPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardOccupationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardWiseMajorOccupationResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise major occupation data.
 */
@Tag(name = "Ward-wise Major Occupation", description = "APIs for managing ward-wise major occupation data")
@RequestMapping("/api/v1/profile/economics/ward-wise-major-occupation")
interface WardWiseMajorOccupationController {

    /**
     * Creates a new ward-wise major occupation entry.
     *
     * This endpoint allows administrators to create new records for ward-wise major occupation.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise major occupation to create
     * @return The created ward-wise major occupation entry
     */
    @Operation(
        summary = "Create ward-wise major occupation",
        description = "Creates a new entry for ward-wise major occupation"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseMajorOccupation(
        @Valid @RequestBody request: CreateWardWiseMajorOccupationDto
    ): ResponseEntity<DpisApiResponse<WardWiseMajorOccupationResponse>>

    /**
     * Updates an existing ward-wise major occupation entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise major occupation.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise major occupation entry to update
     * @param request The new details for the ward-wise major occupation
     * @return The updated ward-wise major occupation entry
     */
    @Operation(
        summary = "Update ward-wise major occupation",
        description = "Updates an existing ward-wise major occupation entry"
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
    fun updateWardWiseMajorOccupation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseMajorOccupationDto
    ): ResponseEntity<DpisApiResponse<WardWiseMajorOccupationResponse>>

    /**
     * Deletes a ward-wise major occupation entry.
     *
     * This endpoint allows administrators to delete records for ward-wise major occupation.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise major occupation entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise major occupation",
        description = "Deletes a ward-wise major occupation entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseMajorOccupation(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise major occupation entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise major occupation entry.
     *
     * @param id The ID of the ward-wise major occupation entry
     * @return The ward-wise major occupation entry details
     */
    @Operation(
        summary = "Get ward-wise major occupation by ID",
        description = "Retrieves a ward-wise major occupation entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseMajorOccupationById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseMajorOccupationResponse>>

    /**
     * Retrieves all ward-wise major occupation entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise major occupation entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise major occupation entries
     */
    @Operation(
        summary = "Get all ward-wise major occupation",
        description = "Retrieves all ward-wise major occupation entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseMajorOccupation(
        @Valid filter: WardWiseMajorOccupationFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorOccupationResponse>>>

    /**
     * Retrieves all ward-wise major occupation entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise major occupation entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise major occupation entries
     */
    @Operation(
        summary = "Get ward-wise major occupation by ward",
        description = "Retrieves all ward-wise major occupation entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseMajorOccupationByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorOccupationResponse>>>

    /**
     * Retrieves all ward-wise major occupation entries for a specific occupation type.
     *
     * This endpoint returns a list of ward-wise major occupation entries for a given occupation type.
     *
     * @param occupation The occupation type to filter by
     * @return List of ward-wise major occupation entries
     */
    @Operation(
        summary = "Get ward-wise major occupation by occupation",
        description = "Retrieves all ward-wise major occupation entries for a specific occupation type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-occupation/{occupation}")
    fun getWardWiseMajorOccupationByOccupation(
        @PathVariable occupation: OccupationType
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorOccupationResponse>>>

    /**
     * Gets summary of occupation population across all wards.
     *
     * @return List of occupation summaries
     */
    @Operation(
        summary = "Get occupation population summary",
        description = "Retrieves summary of occupation population statistics across all wards."
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
            )
        ]
    )
    // We want the general public to be able to see this data
    @GetMapping(
        path = ["/summary/by-occupation"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOccupationPopulationSummary(): ResponseEntity<DpisApiResponse<List<OccupationPopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all occupations.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward occupation summary",
        description = "Retrieves summary of total population by ward across all occupations."
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
            )
        ]
    )
    // We want the general public to be able to see this data
    @GetMapping(
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardOccupationSummary(): ResponseEntity<DpisApiResponse<List<WardOccupationSummaryResponse>>>
}
