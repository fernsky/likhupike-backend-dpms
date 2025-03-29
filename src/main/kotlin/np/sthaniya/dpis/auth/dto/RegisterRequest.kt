package np.sthaniya.dpis.auth.dto

/**
 * Data Transfer Object (DTO) for user self-registration requests.
 *
 * This class handles validation and transport of new user registration data.
 * It differs from [CreateUserDto] as it's used for self-registration rather than
 * administrative user creation.
 *
 * Features:
 * - Email format validation
 * - Password strength requirements
 * - Password confirmation
 * - Ward-level access configuration
 * - Jakarta Validation integration
 *
 * Security:
 * - Strong password requirements
 * - Input validation
 * - Rate limiting (at service level)
 *
 * Registration Flow:
 * 1. User submits registration data
 * 2. Data is validated
 * 3. Account created in pending state
 * 4. Awaits admin approval
 *
 * @property email User's email address (must be unique)
 * @property password User's desired password
 * @property confirmPassword Password confirmation to prevent typos
 * @property isWardLevelUser Flag indicating if user needs ward-level access
 * @property wardNumber Ward number for ward-level users (1-33)
 */
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
    /**
     * Validates password confirmation match.
     *
     * @return true if passwords match, false otherwise
     */
    @AssertTrue(message = "Passwords do not match")
    fun isPasswordValid(): Boolean = password == confirmPassword

    /**
     * Validates ward number requirements for ward-level users.
     *
     * @return true if ward configuration is valid, false otherwise
     */
    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = !isWardLevelUser || wardNumber != null
}
