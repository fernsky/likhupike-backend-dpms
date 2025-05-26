package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.CreateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.UpdateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.WardWiseRemittanceExpensesFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.RemittanceExpenseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardRemittanceSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardWiseRemittanceExpensesResponse
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing ward-wise remittance expenses data.
 */
@Tag(name = "Ward-wise Remittance Expenses", description = "APIs for managing ward-wise remittance expenses data")
@RequestMapping("/api/v1/profile/economics/ward-wise-remittance-expenses")
interface WardWiseRemittanceExpensesController {

    /**
     * Creates a new ward-wise remittance expenses entry.
     *
     * This endpoint allows administrators to create new records for ward-wise remittance expenses.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param request The details of the ward-wise remittance expenses to create
     * @return The created ward-wise remittance expenses entry
     */
    @Operation(
            summary = "Create ward-wise remittance expenses",
            description = "Creates a new entry for ward-wise remittance expenses"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createWardWiseRemittanceExpenses(
            @Valid @RequestBody request: CreateWardWiseRemittanceExpensesDto
    ): ResponseEntity<DpisApiResponse<WardWiseRemittanceExpensesResponse>>

    /**
     * Updates an existing ward-wise remittance expenses entry.
     *
     * This endpoint allows administrators to update existing records for ward-wise remittance expenses.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise remittance expenses entry to update
     * @param request The new details for the ward-wise remittance expenses
     * @return The updated ward-wise remittance expenses entry
     */
    @Operation(
            summary = "Update ward-wise remittance expenses",
            description = "Updates an existing ward-wise remittance expenses entry"
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
    fun updateWardWiseRemittanceExpenses(
            @PathVariable id: UUID,
            @Valid @RequestBody request: UpdateWardWiseRemittanceExpensesDto
    ): ResponseEntity<DpisApiResponse<WardWiseRemittanceExpensesResponse>>

    /**
     * Deletes a ward-wise remittance expenses entry.
     *
     * This endpoint allows administrators to delete records for ward-wise remittance expenses.
     *
     * Security:
     * - Requires ADMIN role
     *
     * @param id The ID of the ward-wise remittance expenses entry to delete
     * @return A success message
     */
    @Operation(
            summary = "Delete ward-wise remittance expenses",
            description = "Deletes a ward-wise remittance expenses entry"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteWardWiseRemittanceExpenses(
            @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Retrieves a ward-wise remittance expenses entry by ID.
     *
     * This endpoint returns the details of a specific ward-wise remittance expenses entry.
     *
     * @param id The ID of the ward-wise remittance expenses entry
     * @return The ward-wise remittance expenses entry details
     */
    @Operation(
            summary = "Get ward-wise remittance expenses by ID",
            description = "Retrieves a ward-wise remittance expenses entry by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions"),
        ApiResponse(responseCode = "404", description = "Entry not found")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/{id}")
    fun getWardWiseRemittanceExpensesById(
            @PathVariable id: UUID
    ): ResponseEntity<DpisApiResponse<WardWiseRemittanceExpensesResponse>>

    /**
     * Retrieves all ward-wise remittance expenses entries with optional filtering.
     *
     * This endpoint returns a list of ward-wise remittance expenses entries.
     *
     * @param filter Optional filtering criteria
     * @return A list of ward-wise remittance expenses entries
     */
    @Operation(
            summary = "Get all ward-wise remittance expenses",
            description = "Retrieves all ward-wise remittance expenses entries with optional filtering"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping
    fun getAllWardWiseRemittanceExpenses(
            @Valid filter: WardWiseRemittanceExpensesFilterDto?
    ): ResponseEntity<DpisApiResponse<List<WardWiseRemittanceExpensesResponse>>>

    /**
     * Retrieves all ward-wise remittance expenses entries for a specific ward.
     *
     * This endpoint returns a list of ward-wise remittance expenses entries for a given ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of ward-wise remittance expenses entries
     */
    @Operation(
            summary = "Get ward-wise remittance expenses by ward",
            description = "Retrieves all ward-wise remittance expenses entries for a specific ward"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-ward/{wardNumber}")
    fun getWardWiseRemittanceExpensesByWard(
            @PathVariable wardNumber: Int
    ): ResponseEntity<DpisApiResponse<List<WardWiseRemittanceExpensesResponse>>>

    /**
     * Retrieves all ward-wise remittance expenses entries for a specific remittance expense type.
     *
     * This endpoint returns a list of ward-wise remittance expenses entries for a given remittance expense type.
     *
     * @param remittanceExpense The remittance expense type to filter by
     * @return List of ward-wise remittance expenses entries
     */
    @Operation(
            summary = "Get ward-wise remittance expenses by expense type",
            description = "Retrieves all ward-wise remittance expenses entries for a specific remittance expense type"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Retrieved successfully"),
        ApiResponse(responseCode = "401", description = "Not authenticated"),
        ApiResponse(responseCode = "403", description = "Missing permissions")
    ])
    // We want the general public to be able to see this data
    @GetMapping("/by-expense/{remittanceExpense}")
    fun getWardWiseRemittanceExpensesByRemittanceExpense(
            @PathVariable remittanceExpense: RemittanceExpenseType
    ): ResponseEntity<DpisApiResponse<List<WardWiseRemittanceExpensesResponse>>>

    /**
     * Gets summary of remittance expenses across all wards.
     *
     * @return List of remittance expense summaries
     */
    @Operation(
            summary = "Get remittance expense summary",
            description = "Retrieves summary of remittance expense statistics across all wards."
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
            path = ["/summary/by-expense"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getRemittanceExpenseSummary(): ResponseEntity<DpisApiResponse<List<RemittanceExpenseSummaryResponse>>>

    /**
     * Gets summary of total households by ward across all remittance expense types.
     *
     * @return List of ward summaries
     */
    @Operation(
            summary = "Get ward remittance summary",
            description = "Retrieves summary of total households by ward across all remittance expense types."
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
    fun getWardRemittanceSummary(): ResponseEntity<DpisApiResponse<List<WardRemittanceSummaryResponse>>>
}
