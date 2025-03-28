package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.Email
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDate
import jakarta.validation.constraints.AssertTrue
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Search criteria for filtering and paginating users",
    title = "User Search Criteria"
)
data class UserSearchCriteria(
    @Schema(
        description = "Filter by exact email address",
        example = "user@example.com",
        nullable = true
    )
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,

    @Schema(
        description = "Generic search term that matches against email",
        example = "john",
        nullable = true
    )
    val searchTerm: String? = null,

    @Schema(
        description = "Filter by user approval status",
        example = "true",
        nullable = true
    )
    val isApproved: Boolean? = null,

    @Schema(
        description = "Filter by ward-level access status",
        example = "true",
        nullable = true
    )
    val isWardLevelUser: Boolean? = null,

    @Schema(
        description = "Filter by specific ward number",
        example = "5",
        minimum = "1",
        maximum = "33",
        nullable = true
    )
    val wardNumber: Int? = null,

    @Schema(
        description = "Filter users created after this date (inclusive)",
        example = "2024-03-01",
        nullable = true
    )
    val createdAfter: LocalDate? = null,

    @Schema(
        description = "Filter users created before this date (inclusive)",
        example = "2024-03-15",
        nullable = true
    )
    val createdBefore: LocalDate? = null,

    @Schema(
        description = "Filter by specific permissions",
        example = "[\"CREATE_USER\", \"VIEW_USER\"]",
        nullable = true
    )
    val permissions: Set<PermissionType>? = null,

    @Schema(
        description = "Specific columns to include in the response",
        example = "[\"email\", \"isApproved\", \"permissions\"]",
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
    val sortDirection: Sort.Direction = Sort.Direction.DESC,
) {
    fun toPageable(): Pageable =
        PageRequest.of(
            (page - 1).coerceAtLeast(0), // Convert 1-based to 0-based
            size,
            Sort.by(sortDirection, sortBy),
        )

    companion object {
        val ALLOWED_COLUMNS =
            setOf(
                "id",
                "email",
                "isWardLevelUser",
                "wardNumber",
                "isApproved",
                "approvedBy",
                "approvedAt",
                "createdAt",
                "updatedAt",
                "permissions",
            )
    }

    fun getValidColumns(): Set<String> = columns?.filter { it in ALLOWED_COLUMNS }?.toSet() ?: ALLOWED_COLUMNS
    @AssertTrue(message = "Page number must be greater than 0")
    fun isPageNumberValid(): Boolean = page > 0
}
