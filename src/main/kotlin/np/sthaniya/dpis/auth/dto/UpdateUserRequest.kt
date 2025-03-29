package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.AssertTrue
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object for partial updates to User entities.
 * 
 * Implements the partial update pattern where null values indicate no change
 * should be made to the corresponding field. Validation is performed only on
 * non-null fields.
 *
 * @property email Optional new email address, must be unique if provided
 * @property isWardLevelUser Optional flag to update ward access status
 * @property wardNumber Optional ward number (1-33), required if isWardLevelUser is true
 *
 * @throws AuthException.UserAlreadyExistsException if new email is already in use
 * @throws AuthException.InvalidUserStateException if ward number is missing for ward-level user
 *
 * @see UserServiceImpl.updateUser for usage
 */
@Schema(description = "Request payload for updating user information")
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
    /**
     * Validates the ward number configuration when updating ward-level access.
     * Ward number is required only when isWardLevelUser is true.
     *
     * @return true if either:
     *         - isWardLevelUser is null (no change to ward access)
     *         - isWardLevelUser is false (removing ward access)
     *         - isWardLevelUser is true AND wardNumber is provided
     */
    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = 
        isWardLevelUser?.let { isWardLevel -> 
            !isWardLevel || wardNumber != null 
        } ?: true
}
