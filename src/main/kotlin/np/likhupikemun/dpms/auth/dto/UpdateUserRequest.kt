package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.AssertTrue
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Request payload for updating user information",
    title = "Update User Request"
)
data class UpdateUserRequest(
    @Schema(
        description = "New email address for the user - must be unique in the system",
        example = "newuser@example.com",
        nullable = true
    )
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,

    @Schema(
        description = "Flag to update user's ward-level access status",
        example = "true",
        nullable = true
    )
    val isWardLevelUser: Boolean? = null,

    @Schema(
        description = "Ward number (1-33) required if isWardLevelUser is true",
        example = "5",
        minimum = "1",
        maximum = "33",
        nullable = true
    )
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
