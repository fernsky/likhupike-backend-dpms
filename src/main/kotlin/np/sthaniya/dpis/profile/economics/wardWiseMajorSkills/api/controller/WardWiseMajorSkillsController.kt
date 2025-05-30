package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.CreateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.UpdateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.WardWiseMajorSkillsFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.SkillPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardSkillsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardWiseMajorSkillsResponse
import np.sthaniya.dpis.profile.economics.common.model.SkillType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise major skills data.
 */
@Tag(name = "Ward-wise Major Skills", description = "APIs for managing ward-wise major skills data")
@RequestMapping("/api/v1/profile/economics/ward-wise-major-skills")
interface WardWiseMajorSkillsController {

    /**
     * Creates a new ward-wise major skills entry.
     *
     * This endpoint allows administrators to create new records for ward-wise major skills.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise major skills to create
     * @return The created ward-wise major skills entry
     */
    @Operation(
        summary = "Create ward-wise major skills",
        description = "Creates a new entry for ward-wise major skills"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseMajorSkills(
        @Valid @RequestBody request: CreateWardWiseMajorSkillsDto
    ): ResponseEntity<DpisApiResponse<WardWiseMajorSkillsResponse>>

    /**
     * Updates an existing ward-wise major skills entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise major skills.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise major skills entry to update
     * @param request The new details for the ward-wise major skills
     * @return The updated ward-wise major skills entry
     */
    @Operation(
        summary = "Update ward-wise major skills",
        description = "Updates an existing ward-wise major skills entry"
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
    fun updateWardWiseMajorSkills(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseMajorSkillsDto
    ): ResponseEntity<DpisApiResponse<WardWiseMajorSkillsResponse>>

    /**
     * Deletes a ward-wise major skills entry.
     *
     * This endpoint allows administrators to delete records for ward-wise major skills.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise major skills entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise major skills",
        description = "Deletes a ward-wise major skills entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseMajorSkills(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise major skills entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise major skills entry.
     *
     * @param id The ID of the ward-wise major skills entry
     * @return The ward-wise major skills entry details
     */
    @Operation(
        summary = "Get ward-wise major skills by ID",
        description = "Retrieves a ward-wise major skills entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseMajorSkillsById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseMajorSkillsResponse>>

    /**
     * Retrieves all ward-wise major skills entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise major skills entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise major skills entries
     */
    @Operation(
        summary = "Get all ward-wise major skills",
        description = "Retrieves all ward-wise major skills entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseMajorSkills(
        @Valid filter: WardWiseMajorSkillsFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorSkillsResponse>>>

    /**
     * Retrieves all ward-wise major skills entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise major skills entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise major skills entries
     */
    @Operation(
        summary = "Get ward-wise major skills by ward",
        description = "Retrieves all ward-wise major skills entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseMajorSkillsByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorSkillsResponse>>>

    /**
     * Retrieves all ward-wise major skills entries for a specific skill type.
     *
     * This endpoint returns a list of ward-wise major skills entries for a given skill type.
     *
     * @param skill The skill type to filter by
     * @return List of ward-wise major skills entries
     */
    @Operation(
        summary = "Get ward-wise major skills by skill",
        description = "Retrieves all ward-wise major skills entries for a specific skill type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-skill/{skill}")
    fun getWardWiseMajorSkillsBySkill(
        @PathVariable skill: SkillType
    ): ResponseEntity<DpisApiResponse<List<WardWiseMajorSkillsResponse>>>

    /**
     * Gets summary of skill population across all wards.
     *
     * @return List of skill summaries
     */
    @Operation(
        summary = "Get skill population summary",
        description = "Retrieves summary of skill population statistics across all wards."
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
        path = ["/summary/by-skill"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getSkillPopulationSummary(): ResponseEntity<DpisApiResponse<List<SkillPopulationSummaryResponse>>>

    /**
     * Gets summary of total population with skills by ward.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward skills summary",
        description = "Retrieves summary of total population with skills by ward."
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
    fun getWardSkillsSummary(): ResponseEntity<DpisApiResponse<List<WardSkillsSummaryResponse>>>
}
