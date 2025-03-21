package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.*
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import java.util.UUID

data class CreateUserDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character"
    )
    val password: String,

    val permissions: Map<PermissionType, Boolean> = emptyMap(),

    @field:NotNull(message = "Ward level user flag must be specified")
    val isWardLevelUser: Boolean = false,

    @field:Min(value = 1, message = "Ward number must be greater than 0")
    @field:Max(value = 33, message = "Ward number cannot be greater than 33")
    val wardNumber: Int? = null,

    val isApproved: Boolean = false,
    val approvedBy: UUID? = null
) {
    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = !isWardLevelUser || wardNumber != null
}
