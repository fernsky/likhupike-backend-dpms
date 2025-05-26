package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.CreateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.UpdateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.WardWiseHouseholdsOnLoanFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise households on loan data.
 */
@Tag(name = "Ward-wise Households On Loan", description = "APIs for managing ward-wise households on loan data")
@RequestMapping("/api/v1/profile/economics/ward-wise-households-on-loan")
interface WardWiseHouseholdsOnLoanController {

    /**
     * Creates a new ward-wise households on loan entry.
     *
     * This endpoint allows administrators to create new records for ward-wise households on loan.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise households on loan to create
     * @return The created ward-wise households on loan entry
     */
    @Operation(
        summary = "Create ward-wise households on loan",
        description = "Creates a new entry for ward-wise households on loan"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseHouseholdsOnLoan(
        @Valid @RequestBody request: CreateWardWiseHouseholdsOnLoanDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsOnLoanResponse>>

    /**
     * Updates an existing ward-wise households on loan entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise households on loan.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise households on loan entry to update
     * @param request The new details for the ward-wise households on loan
     * @return The updated ward-wise households on loan entry
     */
    @Operation(
        summary = "Update ward-wise households on loan",
        description = "Updates an existing ward-wise households on loan entry"
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
    fun updateWardWiseHouseholdsOnLoan(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateWardWiseHouseholdsOnLoanDto
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsOnLoanResponse>>

    /**
     * Deletes a ward-wise households on loan entry.
     *
     * This endpoint allows administrators to delete records for ward-wise households on loan.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise households on loan entry to delete
     * @return A success message
     */
    @Operation(
        summary = "Delete ward-wise households on loan",
        description = "Deletes a ward-wise households on loan entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseHouseholdsOnLoan(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise households on loan entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise households on loan entry.
     *
     * @param id The ID of the ward-wise households on loan entry
     * @return The ward-wise households on loan entry details
     */
    @Operation(
        summary = "Get ward-wise households on loan by ID",
        description = "Retrieves a ward-wise households on loan entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseHouseholdsOnLoanById(
        @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsOnLoanResponse>>

    /**
     * Retrieves all ward-wise households on loan entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise households on loan entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise households on loan entries
     */
    @Operation(
        summary = "Get all ward-wise households on loan",
        description = "Retrieves all ward-wise households on loan entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseHouseholdsOnLoan(
        @Valid filter: WardWiseHouseholdsOnLoanFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseHouseholdsOnLoanResponse>>>

    /**
     * Retrieves ward-wise households on loan entry for a specific ward.
     *
     * This endpoint returns the ward-wise households on loan entry for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return The ward-wise households on loan entry or null if not found
     */
    @Operation(
        summary = "Get ward-wise households on loan by ward",
        description = "Retrieves the ward-wise households on loan entry for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseHouseholdsOnLoanByWard(
        @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<WardWiseHouseholdsOnLoanResponse?>>

    /**
     * Gets summary of households on loan across all wards.
     *
     * @return Summary with total households on loan
     */
    @Operation(
        summary = "Get households on loan summary",
        description = "Retrieves summary of households on loan statistics across all wards."
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
        path = ["/summary"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getHouseholdsOnLoanSummary(): ResponseEntity<DpisApiResponse<WardWiseHouseholdsOnLoanSummaryResponse>>
}
