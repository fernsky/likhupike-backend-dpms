package np.sthaniya.dpis.auth.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus
/**
 * Base exception hierarchy for authentication and authorization errors.
 *
 * Each exception type corresponds to a specific authentication/authorization failure scenario
 * and maps to an appropriate HTTP status code. Error codes follow the format AUTH_XXX
 * for consistent error handling and client identification.
 *
 * Exception Categories:
 * 1. User State (001-005): Exceptions related to user existence and status
 * 2. Permissions (006-009): Permission and authorization related failures
 * 3. Authentication (010-014): Login and token related failures
 * 4. Password Reset (015-017): OTP and password reset specific errors
 * 5. Pagination (018): Page navigation errors
 *
 * @param errorCode Specific error code from [AuthErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 *
 * @see dpisException Base exception class
 * @see ErrorCode Error code interface
 */
sealed class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /**
     * Error codes specific to authentication and authorization failures
     */
    enum class AuthErrorCode : ErrorCode {
        USER_NOT_FOUND {
            override val code = "AUTH_001"
            override val defaultMessage = "User not found"
            override val i18nKey = "auth.error.AUTH_001"
        },
        USER_ALREADY_EXISTS {
            override val code = "AUTH_002"
            override val defaultMessage = "User already exists"
            override val i18nKey = "auth.error.AUTH_002"
        },
        USER_ALREADY_DELETED {
            override val code = "AUTH_003"
            override val defaultMessage = "User is already deleted"
            override val i18nKey = "auth.error.AUTH_003"
        },
        USER_ALREADY_APPROVED {
            override val code = "AUTH_004"
            override val defaultMessage = "User is already approved"
            override val i18nKey = "auth.error.AUTH_004"
        },
        INVALID_USER_STATE {
            override val code = "AUTH_005"
            override val defaultMessage = "Invalid user state"
            override val i18nKey = "auth.error.AUTH_005"
        },
        PERMISSION_NOT_FOUND {
            override val code = "AUTH_006"
            override val defaultMessage = "Permission not found"
            override val i18nKey = "auth.error.AUTH_006"
        },
        MISSING_PERMISSIONS {
            override val code = "AUTH_007"
            override val defaultMessage = "Required permissions are missing"
            override val i18nKey = "auth.error.AUTH_007"
        },
        UNAUTHENTICATED {
            override val code = "AUTH_008"
            override val defaultMessage = "Authentication required"
            override val i18nKey = "auth.error.AUTH_008"
        },
        INSUFFICIENT_PERMISSIONS {
            override val code = "AUTH_009"
            override val defaultMessage = "Insufficient permissions"
            override val i18nKey = "auth.error.AUTH_009"
        },
        INVALID_CREDENTIALS {
            override val code = "AUTH_010"
            override val defaultMessage = "Invalid credentials"
            override val i18nKey = "auth.error.AUTH_010"
        },
        USER_NOT_APPROVED {
            override val code = "AUTH_011"
            override val defaultMessage = "User not approved"
            override val i18nKey = "auth.error.AUTH_011"
        },
        INVALID_TOKEN {
            override val code = "AUTH_012"
            override val defaultMessage = "Invalid or expired token"
            override val i18nKey = "auth.error.AUTH_012"
        },
        INVALID_PASSWORD {
            override val code = "AUTH_013"
            override val defaultMessage = "Invalid password provided"
            override val i18nKey = "auth.error.AUTH_013"
        },
        JWT_VALIDATION_FAILED {
            override val code = "AUTH_014"
            override val defaultMessage = "JWT token validation failed"
            override val i18nKey = "auth.error.AUTH_014"
        },
        PASSWORD_RESET_OTP_INVALID {
            override val code = "AUTH_015"
            override val defaultMessage = "Invalid or expired OTP"
            override val i18nKey = "auth.error.AUTH_015"
        },
        PASSWORDS_DO_NOT_MATCH {
            override val code = "AUTH_016"
            override val defaultMessage = "Passwords do not match"
            override val i18nKey = "auth.error.AUTH_016"
        },
        TOO_MANY_ATTEMPTS {
            override val code = "AUTH_017"
            override val defaultMessage = "Too many invalid attempts"
            override val i18nKey = "auth.error.AUTH_017"
        },
        PAGE_DOES_NOT_EXIST {
            override val code = "AUTH_018"
            override val defaultMessage = "Page does not exist"
            override val i18nKey = "auth.error.AUTH_018"
        }
    }

    /**
     * Exception thrown when a user cannot be found by ID or email
     */
    class UserNotFoundException(id: String) : AuthException(
        AuthErrorCode.USER_NOT_FOUND,
        metadata = mapOf("id" to id),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when attempting to create a user with an existing email
     */
    class UserAlreadyExistsException(email: String) : AuthException(
        AuthErrorCode.USER_ALREADY_EXISTS,
        metadata = mapOf("email" to email),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when attempting to delete a user that is already deleted
     */
    class UserAlreadyDeletedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_DELETED,
        metadata = mapOf("id" to id),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when attempting to approve a user that is already approved
     */
    class UserAlreadyApprovedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_APPROVED,
        metadata = mapOf("id" to id),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when a user is in an invalid state for the requested operation
     */
    class InvalidUserStateException(message: String) : AuthException(
        AuthErrorCode.INVALID_USER_STATE,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a required permission type cannot be found
     */
    class PermissionNotFoundException(type: String) : AuthException(
        AuthErrorCode.PERMISSION_NOT_FOUND,
        metadata = mapOf("type" to type),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when required permissions are missing for an operation
     */
    class MissingPermissionsException(types: Set<String>) : AuthException(
        AuthErrorCode.MISSING_PERMISSIONS,
        metadata = mapOf("types" to types),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when authentication is required but not provided
     */
    class UnauthenticatedException : AuthException(
        AuthErrorCode.UNAUTHENTICATED,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when the user has insufficient permissions for an operation
     */
    class InsufficientPermissionsException(
        requiredPermissions: Set<String>? = null,
        message: String? = null
    ) : AuthException(
        AuthErrorCode.INSUFFICIENT_PERMISSIONS,
        message = message,
        metadata = requiredPermissions?.let { mapOf("requiredPermissions" to it) } ?: emptyMap(),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when invalid credentials are provided during authentication
     */
    class InvalidCredentialsException : AuthException(
        AuthErrorCode.INVALID_CREDENTIALS,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when a user is not approved for access
     */
    class UserNotApprovedException : AuthException(
        AuthErrorCode.USER_NOT_APPROVED,
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when an invalid or expired token is provided
     */
    class InvalidTokenException : AuthException(
        AuthErrorCode.INVALID_TOKEN,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when an invalid password is provided
     */
    class InvalidPasswordException(message: String? = null) : AuthException(
        AuthErrorCode.INVALID_PASSWORD,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when JWT token validation fails
     */
    class JwtAuthenticationException(
        message: String? = null,
        details: Map<String, Any> = emptyMap()
    ) : AuthException(
        AuthErrorCode.JWT_VALIDATION_FAILED,
        message = message,
        metadata = details,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when an invalid or expired OTP is provided for password reset
     */
    class InvalidPasswordResetTokenException(message: String? = null) : AuthException(
        AuthErrorCode.PASSWORD_RESET_OTP_INVALID,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when passwords do not match during password reset
     */
    class PasswordsDoNotMatchException : AuthException(
        AuthErrorCode.PASSWORDS_DO_NOT_MATCH,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when too many invalid attempts are made
     */
    class TooManyAttemptsException : AuthException(
        AuthErrorCode.TOO_MANY_ATTEMPTS,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a requested page does not exist
     */
    class PageDoesNotExistException(message: String) : AuthException(
        AuthErrorCode.PAGE_DOES_NOT_EXIST,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a requested role is not found.
     *
     * @param roleName The name of the role that was not found
     */
    class RoleNotFoundException(roleName: String) : AuthException(
        "Role not found: $roleName",
        "role.not.found",
        mapOf("roleName" to roleName)
    )

    /**
     * Exception thrown when one or more required roles are missing from the system.
     *
     * @param missingRoles Set of missing role names
     */
    class MissingRolesException(missingRoles: Set<String>) : AuthException(
        "Missing roles: ${missingRoles.joinToString()}",
        "roles.missing",
        mapOf("missingRoles" to missingRoles)
    )
}
