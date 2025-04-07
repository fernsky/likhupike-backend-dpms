package np.sthaniya.dpis.citizen.dto.search

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDate

/**
 * Search criteria for citizen queries with pagination, filtering, and sorting support.
 * Used to filter and paginate citizen data in search operations.
 *
 * Search Parameters:
 * - Exact match: email, citizenshipNumber, phoneNumber
 * - Pattern match: name, nameDevnagari
 * - State filters: state (CitizenState enum), isApproved, documentStates
 * - Document states: photoState, citizenshipFrontState, citizenshipBackState
 * - Date range: createdAfter, createdBefore
 * - Address-based: addressTerm (searches in both permanent and temporary addresses)
 *
 * Pagination/Sorting:
 * - Page number (1-based)
 * - Page size
 * - Sort field
 * - Sort direction
 *
 * Column Selection:
 * Supports field selection via [columns]. Valid columns defined in [ALLOWED_COLUMNS].
 *
 * Usage:
 * ```kotlin
 * val criteria = CitizenSearchCriteria(
 *     name = "Sharma",
 *     state = CitizenState.ACTION_REQUIRED,
 *     page = 1,
 *     size = 20,
 *     sortBy = "createdAt"
 * )
 * citizenService.searchCitizens(criteria)
 * ```
 */
@Schema(
    description = "Search criteria for filtering and paginating citizens",
    title = "Citizen Search Criteria"
)
data class CitizenSearchCriteria(
    @Schema(
        description = "Filter by partial name match (case insensitive)",
        example = "Sharma",
        nullable = true
    )
    val name: String? = null,
    
    @Schema(
        description = "Filter by partial Devanagari name match",
        example = "शर्मा",
        nullable = true
    )
    val nameDevnagari: String? = null,
    
    @Schema(
        description = "Filter by exact citizenship number",
        example = "123-456-78901",
        nullable = true
    )
    val citizenshipNumber: String? = null,
    
    @Schema(
        description = "Filter by citizenship issuing office (partial match)",
        example = "Kathmandu",
        nullable = true
    )
    val citizenshipIssuedOffice: String? = null,
    
    @Schema(
        description = "Filter by citizenship issued date range - start date",
        example = "2010-01-01",
        nullable = true
    )
    val citizenshipIssuedDateStart: LocalDate? = null,
    
    @Schema(
        description = "Filter by citizenship issued date range - end date",
        example = "2020-12-31",
        nullable = true
    )
    val citizenshipIssuedDateEnd: LocalDate? = null,
    
    @Schema(
        description = "Filter by exact email address",
        example = "citizen@example.com",
        nullable = true
    )
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,
    
    @Schema(
        description = "Filter by phone number (partial or exact match)",
        example = "9801234567",
        nullable = true
    )
    val phoneNumber: String? = null,
    
    @Schema(
        description = "Filter by verification state",
        example = "ACTION_REQUIRED",
        nullable = true
    )
    val state: CitizenState? = null,
    
    @Schema(
        description = "Filter to include multiple states",
        example = "[\"PENDING_REGISTRATION\", \"ACTION_REQUIRED\"]",
        nullable = true
    )
    val states: Set<CitizenState>? = null,
    
    @Schema(
        description = "Filter by approval status",
        example = "true",
        nullable = true
    )
    val isApproved: Boolean? = null,
    
    @Schema(
        description = "Filter by photo document state",
        example = "REJECTED_BLURRY",
        nullable = true
    )
    val photoState: DocumentState? = null,
    
    @Schema(
        description = "Filter by front citizenship certificate document state",
        example = "AWAITING_REVIEW",
        nullable = true
    )
    val citizenshipFrontState: DocumentState? = null,
    
    @Schema(
        description = "Filter by back citizenship certificate document state",
        example = "REJECTED_MISMATCH",
        nullable = true
    )
    val citizenshipBackState: DocumentState? = null,
    
    @Schema(
        description = "Filter by document states across any document type",
        example = "[\"NOT_UPLOADED\", \"AWAITING_REVIEW\"]",
        nullable = true
    )
    val documentStates: Set<DocumentState>? = null,
    
    @Schema(
        description = "Filter by note content in state or document notes (partial match, case insensitive)",
        example = "blurry",
        nullable = true
    )
    val notesSearch: String? = null,
    
    @Schema(
        description = "Filter by address fields (partial match in any address field)",
        example = "Kathmandu",
        nullable = true
    )
    val addressTerm: String? = null,
    
    @Schema(
        description = "Filter by province code in permanent address",
        example = "P3",
        nullable = true
    )
    val permanentProvinceCode: String? = null,
    
    @Schema(
        description = "Filter by district code in permanent address",
        example = "D301",
        nullable = true
    )
    val permanentDistrictCode: String? = null,
    
    @Schema(
        description = "Filter by municipality code in permanent address",
        example = "M30101",
        nullable = true
    )
    val permanentMunicipalityCode: String? = null,
    
    @Schema(
        description = "Filter by ward number in permanent address",
        example = "5",
        nullable = true
    )
    val permanentWardNumber: Int? = null,
    
    @Schema(
        description = "Filter by citizens created after this date (inclusive)",
        example = "2024-01-01",
        nullable = true
    )
    val createdAfter: LocalDate? = null,
    
    @Schema(
        description = "Filter by citizens created before this date (inclusive)",
        example = "2024-03-31",
        nullable = true
    )
    val createdBefore: LocalDate? = null,
    
    @Schema(
        description = "Filter by citizens with state updated after this date (inclusive)",
        example = "2024-02-15",
        nullable = true
    )
    val stateUpdatedAfter: LocalDate? = null,
    
    @Schema(
        description = "Filter by citizens with state updated before this date (inclusive)",
        example = "2024-02-28",
        nullable = true
    )
    val stateUpdatedBefore: LocalDate? = null,
    
    @Schema(
        description = "Filter by citizens updated by this user ID",
        example = "550e8400-e29b-41d4-a716-446655440000",
        nullable = true
    )
    val updatedByUserId: String? = null,
    
    @Schema(
        description = "Specific columns to include in the response",
        example = "[\"name\", \"email\", \"state\", \"isApproved\"]",
        nullable = true
    )
    val columns: Set<String>? = null,
    
    @Schema(
        description = "Page number (1-based)",
        example = "1",
        minimum = "1",
        defaultValue = "1"
    )
    val page: Int = 1,
    
    @Schema(
        description = "Number of items per page",
        example = "10",
        defaultValue = "10"
    )
    val size: Int = 10,
    
    @Schema(
        description = "Field to sort by",
        example = "createdAt",
        defaultValue = "createdAt"
    )
    val sortBy: String = "createdAt",
    
    @Schema(
        description = "Sort direction",
        example = "DESC",
        allowableValues = ["ASC", "DESC"],
        defaultValue = "DESC"
    )
    val sortDirection: Sort.Direction = Sort.Direction.DESC
) {
    /**
     * Converts the criteria to Spring Data's Pageable object.
     * Handles 1-based to 0-based page number conversion.
     */
    fun toPageable(): Pageable =
        PageRequest.of(
            (page - 1).coerceAtLeast(0), // Convert 1-based to 0-based
            size,
            Sort.by(sortDirection, sortBy)
        )
    
    companion object {
        /**
         * Set of field names that can be included in query results.
         * Used to validate and filter requested columns.
         */
        val ALLOWED_COLUMNS = setOf(
            "id",
            "name",
            "nameDevnagari",
            "citizenshipNumber",
            "citizenshipIssuedDate",
            "citizenshipIssuedOffice",
            "email",
            "phoneNumber",
            "state",
            "stateNote",
            "stateUpdatedAt",
            "stateUpdatedBy",
            "photoState",
            "photoStateNote",
            "citizenshipFrontState",
            "citizenshipFrontStateNote",
            "citizenshipBackState",
            "citizenshipBackStateNote",
            "isApproved",
            "approvedBy",
            "approvedAt",
            "isDeleted",
            "deletedBy",
            "deletedAt",
            "permanentAddress",
            "temporaryAddress",
            "createdAt",
            "updatedAt"
        )
    }
    
    /**
     * Returns validated set of columns to include in query results.
     * Falls back to all allowed columns if none specified.
     */
    fun getValidColumns(): Set<String> = columns?.filter { it in ALLOWED_COLUMNS }?.toSet() ?: ALLOWED_COLUMNS
    
    /**
     * Validates that page number is positive.
     * Required by Jakarta Validation.
     */
    @AssertTrue(message = "Page number must be greater than 0")
    fun isPageNumberValid(): Boolean = page > 0
    
    /**
     * Validates date ranges are logical.
     */
    @AssertTrue(message = "End date must be after start date")
    fun isDateRangeValid(): Boolean {
        val citizenshipDatesValid = citizenshipIssuedDateStart == null || citizenshipIssuedDateEnd == null ||
                !citizenshipIssuedDateEnd!!.isBefore(citizenshipIssuedDateStart)
                
        val createdDatesValid = createdAfter == null || createdBefore == null ||
                !createdBefore!!.isBefore(createdAfter)
                
        val stateUpdateDatesValid = stateUpdatedAfter == null || stateUpdatedBefore == null ||
                !stateUpdatedBefore!!.isBefore(stateUpdatedAfter)
                
        return citizenshipDatesValid && createdDatesValid && stateUpdateDatesValid
    }
}
