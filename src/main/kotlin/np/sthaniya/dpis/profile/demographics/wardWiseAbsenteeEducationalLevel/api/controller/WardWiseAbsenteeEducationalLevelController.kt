package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.CreateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.UpdateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.WardWiseAbsenteeEducationalLevelFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.EducationalLevelPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardWiseAbsenteeEducationalLevelResponse
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise absentee educational level data.
 */
@Tag(name = "Ward-wise Absentee Educational Level", description = "APIs for managing ward-wise absentee educational level data")
@RequestMapping("/api/v1/profile/demographics/ward-wise-absentee-educational-level")
interface WardWiseAbsenteeEducationalLevelController {

    /**
     * Creates a new ward-wise absentee educational level entry.
     *
     * This endpoint allows administrators to create new records for ward-wise absentee educational level.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise absentee educational level to create
     * @return The created ward-wise absentee educational level entry
     */
    @Operation(
        summary = "Create ward-wise absentee educational level",
        description = "Creates a new entry for ward-wise absentee educational level"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseAbsenteeEducationalLevel(
        @Valid @RequestBody request: CreateWardWiseAbsenteeEducationalLevelDto
    ): ResponseEntity<DpisApiResponse<WardWiseAbsenteeEducationalLevelResponse>>

    /**
     * Updates an existing ward-wise absentee educational level entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise absentee educational level.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise absentee educational level entry to update
     * @param request The new details for the ward-wise absentee educational level
     * @return The updated ward-wise absentee educational level entry
     */
    @Operation(
        summary = "Update ward-wise absentee educational level",
        description = "Updates an existing ward-wise absentee educational level entry"
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
    fun updateWardWiseAbsenteeEducationalLevel(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseAbsenteeEducationalLevelDto
    ): ResponseEntity<DpisApiResponse<WardWiseAbsenteeEducationalLevelResponse>>

    /**
     * Deletes a ward-wise absentee educational level entry.
     *
     * This endpoint allows administrators to delete records for ward-wise absentee educational level.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise absentee educational level entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise absentee educational level",
        description = "Deletes a ward-wise absentee educational level entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseAbsenteeEducationalLevel(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise absentee educational level entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise absentee educational level entry.
     *
     * @param id The ID of the ward-wise absentee educational level entry
     * @return The ward-wise absentee educational level entry details
     */
    @Operation(
        summary = "Get ward-wise absentee educational level by ID",
        description = "Retrieves a ward-wise absentee educational level entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // Open to public
    @GetMapping("/{id}")
    fun getWardWiseAbsenteeEducationalLevelById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseAbsenteeEducationalLevelResponse>>

    /**
     * Retrieves all ward-wise absentee educational level entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise absentee educational level entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise absentee educational level entries
     */
    @Operation(
        summary = "Get all ward-wise absentee educational level",
        description = "Retrieves all ward-wise absentee educational level entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Open to public
    @GetMapping
    fun getAllWardWiseAbsenteeEducationalLevel(
        @Valid filter: WardWiseAbsenteeEducationalLevelFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>>

    /**
     * Retrieves all ward-wise absentee educational level entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise absentee educational level entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise absentee educational level entries
     */
    @Operation(
        summary = "Get ward-wise absentee educational level by ward",
        description = "Retrieves all ward-wise absentee educational level entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Open to public
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseAbsenteeEducationalLevelByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>>

    /**
     * Retrieves all ward-wise absentee educational level entries for a specific educational level.
     *
     * This endpoint returns a list of ward-wise absentee educational level entries for a given educational level.
     *
     * @param educationalLevel The educational level to filter by
     * @return List of ward-wise absentee educational level entries
     */
    @Operation(
        summary = "Get ward-wise absentee educational level by educational level",
        description = "Retrieves all ward-wise absentee educational level entries for a specific educational level"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated")
    ])
    // Open to public
    @GetMapping("/by-educational-level/{educationalLevel}")
    fun getWardWiseAbsenteeEducationalLevelByEducationalLevel(
        @PathVariable educationalLevel: EducationalLevelType
    ): ResponseEntity<DpisApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>>

    /**
     * Gets summary of educational level population across all wards.
     *
     * @return List of educational level summaries
     */
    @Operation(
        summary = "Get educational level population summary",
        description = "Retrieves summary of educational level population statistics across all wards.",
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
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    // Open to public
    @GetMapping(
        path = ["/summary/by-educational-level"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getEducationalLevelPopulationSummary(): ResponseEntity<DpisApiResponse<List<EducationalLevelPopulationSummaryResponse>>>

    /**
     * Gets summary of total population by ward across all educational levels.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward population summary",
        description = "Retrieves summary of total population by ward across all educational levels.",
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
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    // Open to public
    @GetMapping(
        path = ["/summary/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getWardPopulationSummary(): ResponseEntity<DpisApiResponse<List<WardPopulationSummaryResponse>>>
}
