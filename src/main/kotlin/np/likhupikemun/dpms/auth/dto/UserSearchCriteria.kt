package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.Email
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDate

data class UserSearchCriteria(
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,
    val searchTerm: String? = null,
    val isApproved: Boolean? = null,
    val isWardLevelUser: Boolean? = null,
    val wardNumber: Int? = null,  // Replaced wardNumberFrom/To with single wardNumber
    val createdAfter: LocalDate? = null,
    val createdBefore: LocalDate? = null,
    val permissions: Set<PermissionType>? = null,
    val columns: Set<String>? = null,
    val page: Int = 1,  // Changed default from 0 to 1
    val size: Int = 10,
    val sortBy: String = "createdAt",
    val sortDirection: Sort.Direction = Sort.Direction.DESC,
) {
    fun toPageable(): Pageable =
        PageRequest.of(
            (page - 1).coerceAtLeast(0), // Convert 1-based to 0-based index
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
}
