package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

@Schema(
    description = "Request payload for user registration",
    title = "Register Request",
    requiredProperties = ["email", "password", "confirmPassword", "isWardLevelUser"]
)
data class RegisterRequest(
    @Schema(
        description = "User's email address - must be unique in the system",
        example = "user@example.com",
        required = true
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String,

    @Schema(
        description = """
            Password that meets the following criteria:
            - At least 8 characters long
            - Contains at least one digit
            - Contains at least one lowercase letter
            - Contains at least one uppercase letter
            - Contains at least one special character (@#$%^&+=)
        """,
        example = "StrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character"
    )
    val password: String,

    @Schema(
        description = "Confirmation of the password - must match password field",
        example = "StrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String,

    @Schema(
        description = "Indicates if the user requires ward-level access",
        example = "false",
        defaultValue = "false",
        required = true
    )
    @field:NotNull(message = "Ward level user flag must be specified")
    val isWardLevelUser: Boolean = false,

    @Schema(
        description = "Ward number (1-33) required if isWardLevelUser is true",
        example = "5",
        minimum = "1",
        maximum = "5",
        nullable = true
    )
    @field:Min(value = 1, message = "Ward number must be greater than 0")
    @field:Max(value = 5, message = "Ward number cannot be greater than 5")
    val wardNumber: Int? = null
) {
    @AssertTrue(message = "Passwords do not match")
    fun isPasswordValid(): Boolean = password == confirmPassword

    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = !isWardLevelUser || wardNumber != null
}
