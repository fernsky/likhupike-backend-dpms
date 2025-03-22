package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.AssertTrue

data class UpdateUserRequest(
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,
    val isWardLevelUser: Boolean? = null,
    @field:Min(value = 1, message = "Ward number must be greater than 0")
    @field:Max(value = 33, message = "Ward number cannot be greater than 33")
    val wardNumber: Int? = null,
) {
    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = 
        isWardLevelUser?.let { isWardLevel -> 
            !isWardLevel || wardNumber != null 
        } ?: true
}
