package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.CreateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.UpdateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.WardWiseHouseholdsLoanUseFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.LoanUseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardHouseholdsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardWiseHouseholdsLoanUseResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise households loan use data.
 */
@Tag(name = "Ward-wise Households Loan Use", description = "APIs for managing ward-wise households loan use data")
@RequestMapping("/api/v1/profile/economics/ward-wise-households-loan-use")
interface WardWiseHouseholdsLoanUseController {

    /**
     * Creates a new ward-wise households loan use entry.
     *
     * This endpoint allows administrators to create new records for ward-wise households loan use.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise households loan use to create
     * @return The created ward-wise households loan use entry
     */
    @Operation(
        summary = "Create ward-wise households loan use",
        description = "Creates a new entry for ward-wise households loan use"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseHouseholdsLoanUse(
        @Valid @RequestBody request: CreateWardWiseHouseholdsLoanUseDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsLoanUseResponse>>

    /**
     * Updates an existing ward-wise households loan use entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise households loan use.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise households loan use entry to update
     * @param request The new details for the ward-wise households loan use
     * @return The updated ward-wise households loan use entry
     */
    @Operation(
        summary = "Update ward-wise households loan use",
        description = "Updates an existing ward-wise households loan use entry"
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
    fun updateWardWiseHouseholdsLoanUse(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseHouseholdsLoanUseDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsLoanUseResponse>>

    /**
     * Deletes a ward-wise households loan use entry.
     *
     * This endpoint allows administrators to delete records for ward-wise households loan use.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise households loan use entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise households loan use",
        description = "Deletes a ward-wise households loan use entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseHouseholdsLoanUse(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise households loan use entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise households loan use entry.
     *
     * @param id The ID of the ward-wise households loan use entry
     * @return The ward-wise households loan use entry details
     */
    @Operation(
        summary = "Get ward-wise households loan use by ID",
        description = "Retrieves a ward-wise households loan use entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseHouseholdsLoanUseById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsLoanUseResponse>>

    /**
     * Retrieves all ward-wise households loan use entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise households loan use entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise households loan use entries
     */
    @Operation(
        summary = "Get all ward-wise households loan use",
        description = "Retrieves all ward-wise households loan use entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseHouseholdsLoanUse(
        @Valid filter: WardWiseHouseholdsLoanUseFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseholdsLoanUseResponse>>>

    /**
     * Retrieves all ward-wise households loan use entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise households loan use entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise households loan use entries
     */
    @Operation(
        summary = "Get ward-wise households loan use by ward",
        description = "Retrieves all ward-wise households loan use entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseHouseholdsLoanUseByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseholdsLoanUseResponse>>>

    /**
     * Retrieves all ward-wise households loan use entries for a specific loan use type.
     *
     * This endpoint returns a list of ward-wise households loan use entries for a given loan use type.
     *
     * @param loanUse The loan use type to filter by
     * @return List of ward-wise households loan use entries
     */
    @Operation(
        summary = "Get ward-wise households loan use by loan use type",
        description = "Retrieves all ward-wise households loan use entries for a specific loan use type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-loan-use/{loanUse}")
    fun getWardWiseHouseholdsLoanUseByLoanUse(
        @PathVariable loanUse: LoanUseType
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseholdsLoanUseResponse>>>

    /**
     * Gets summary of households by loan use type across all wards.
     *
     * @return List of loan use summaries
     */
    @Operation(
        summary = "Get loan use summary",
        description = "Retrieves summary of households by loan use type across all wards."
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
        path = ["/summary/by-loan-use"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLoanUseSummary(): ResponseEntity<DpisApiResponse<List<LoanUseSummaryResponse>>>

    /**
     * Gets summary of total households by ward across all loan use types.
     *
     * @return List of ward summaries
     */
    @Operation(
        summary = "Get ward households summary",
        description = "Retrieves summary of total households by ward across all loan use types."
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
    fun getWardHouseholdsSummary(): ResponseEntity<DpisApiResponse<List<WardHouseholdsSummaryResponse>>>
}
