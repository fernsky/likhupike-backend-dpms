package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.*
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.enums.RoleType
import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object for administrative user creation operations.
 * Used by [UserService.createUser] to create new user accounts with specified permissions and roles.
 *
 * Required fields:
 * - email: Must be unique in the system
 * - password: Must meet complexity requirements
 * - isWardLevelUser: Determines access level type
 *
 * Optional fields:
 * - permissions: Map of initial permissions to grant
 * - roles: Map of initial roles to assign
 * - wardNumber: Required only if isWardLevelUser=true
 * - isApproved: Auto-approval flag
 * - approvedBy: Admin ID for auto-approval
 *
 * @throws AuthException.UserAlreadyExistsException if email exists
 * @throws AuthException.InvalidUserStateException if ward config invalid
 * @throws jakarta.validation.ConstraintViolationException if validation fails
 *
 * @see UserController.createUser for API endpoint usage
 * @see UserServiceImpl.createUser for implementation details
 */
@Schema(
    description = "Request payload for creating a new user",
    title = "Create User Request",
    requiredProperties = ["email", "password", "isWardLevelUser"]
)
data class CreateUserDto(
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
        description = "Map of permissions to be granted to the user",
        example = """{"CREATE_USER": true, "VIEW_USER": true}""",
        defaultValue = "{}"
    )
    val permissions: Map<PermissionType, Boolean> = emptyMap(),
    
    @Schema(
        description = "Map of roles to be assigned to the user",
        example = """{"SYSTEM_ADMINISTRATOR": true, "LAND_RECORDS_OFFICER": false}""",
        defaultValue = "{}"
    )
    val roles: Map<RoleType, Boolean> = emptyMap(),

    @Schema(
        description = "Indicates if the user should have ward-level access",
        example = "false",
        required = true
    )
    @field:NotNull(message = "Ward level user flag must be specified")
    val isWardLevelUser: Boolean = false,

    @Schema(
        description = "Ward number (1-33) if user has ward-level access",
        example = "5",
        minimum = "1",
        maximum = "33",
        nullable = true
    )
    @field:Min(value = 1, message = "Ward number must be greater than 0")
    @field:Max(value = 33, message = "Ward number cannot be greater than 33")
    val wardNumber: Int? = null,

    @Schema(
        description = "Indicates if the user account is approved",
        example = "false",
        defaultValue = "false"
    )
    val isApproved: Boolean = false,

    @Schema(
        description = "UUID of the user who approved this account",
        example = "550e8400-e29b-41d4-a716-446655440000",
        nullable = true
    )
    val approvedBy: UUID? = null
) {
    /**
     * Validates ward-level user configuration.
     * Ward number must be provided when isWardLevelUser=true.
     */
    @AssertTrue(message = "Ward number is required for ward level users")
    fun isWardNumberValid(): Boolean = !isWardLevelUser || wardNumber != null
}
