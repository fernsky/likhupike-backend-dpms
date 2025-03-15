package np.likhupikemun.dpms.auth.exception

import np.likhupikemun.dpms.common.exception.DpmsException
import np.likhupikemun.dpms.common.exception.ErrorCode
import org.springframework.http.HttpStatus

sealed class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : DpmsException(errorCode, message, status, metadata) {

    enum class AuthErrorCode : ErrorCode {
        USER_NOT_FOUND {
            override val code = "AUTH_001"
            override val defaultMessage = "User not found"
        },
        USER_ALREADY_EXISTS {
            override val code = "AUTH_002"
            override val defaultMessage = "User already exists"
        },
        USER_ALREADY_DELETED {
            override val code = "AUTH_003"
            override val defaultMessage = "User is already deleted"
        },
        USER_ALREADY_APPROVED {
            override val code = "AUTH_004"
            override val defaultMessage = "User is already approved"
        },
        INVALID_USER_STATE {
            override val code = "AUTH_005"
            override val defaultMessage = "Invalid user state"
        },
        PERMISSION_NOT_FOUND {
            override val code = "AUTH_006"
            override val defaultMessage = "Permission not found"
        },
        MISSING_PERMISSIONS {
            override val code = "AUTH_007"
            override val defaultMessage = "Required permissions are missing"
        },
        UNAUTHENTICATED {
            override val code = "AUTH_008"
            override val defaultMessage = "Authentication required"
        },
        INSUFFICIENT_PERMISSIONS {
            override val code = "AUTH_009"
            override val defaultMessage = "Insufficient permissions"
        },
        INVALID_CREDENTIALS {
            override val code = "AUTH_010"
            override val defaultMessage = "Invalid credentials provided"
        },
        USER_NOT_APPROVED {
            override val code = "AUTH_011"
            override val defaultMessage = "User account is not approved"
        },
        INVALID_TOKEN {
            override val code = "AUTH_012"
            override val defaultMessage = "Invalid or expired token"
        },
        INVALID_PASSWORD {
            override val code = "AUTH_013"
            override val defaultMessage = "Invalid password provided"
        },
        JWT_VALIDATION_FAILED {
            override val code = "AUTH_014"
            override val defaultMessage = "JWT token validation failed"
        },
        PASSWORD_RESET_TOKEN_INVALID {
            override val code = "AUTH_015"
            override val defaultMessage = "Password reset token is invalid or expired"
        }
    }

    class UserNotFoundException(id: String) : AuthException(
        AuthErrorCode.USER_NOT_FOUND,
        metadata = mapOf("id" to id),
        status = HttpStatus.NOT_FOUND
    )

    class UserAlreadyExistsException(email: String) : AuthException(
        AuthErrorCode.USER_ALREADY_EXISTS,
        metadata = mapOf("email" to email),
        status = HttpStatus.CONFLICT
    )

    class UserAlreadyDeletedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_DELETED,
        metadata = mapOf("id" to id),
        status = HttpStatus.CONFLICT
    )

    class UserAlreadyApprovedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_APPROVED,
        metadata = mapOf("id" to id),
        status = HttpStatus.CONFLICT
    )

    class InvalidUserStateException(message: String) : AuthException(
        AuthErrorCode.INVALID_USER_STATE,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    class PermissionNotFoundException(type: String) : AuthException(
        AuthErrorCode.PERMISSION_NOT_FOUND,
        metadata = mapOf("type" to type),
        status = HttpStatus.NOT_FOUND
    )

    class MissingPermissionsException(types: Set<String>) : AuthException(
        AuthErrorCode.MISSING_PERMISSIONS,
        metadata = mapOf("types" to types),
        status = HttpStatus.FORBIDDEN
    )

    class UnauthenticatedException : AuthException(
        AuthErrorCode.UNAUTHENTICATED,
        status = HttpStatus.UNAUTHORIZED
    )

    class InsufficientPermissionsException(
        requiredPermissions: Set<String>? = null,
        message: String? = null
    ) : AuthException(
        AuthErrorCode.INSUFFICIENT_PERMISSIONS,
        message = message,
        metadata = requiredPermissions?.let { mapOf("requiredPermissions" to it) } ?: emptyMap(),
        status = HttpStatus.FORBIDDEN
    )

    class InvalidCredentialsException : AuthException(
        AuthErrorCode.INVALID_CREDENTIALS,
        status = HttpStatus.NOT_FOUND
    )

    class UserNotApprovedException : AuthException(
        AuthErrorCode.USER_NOT_APPROVED,
        status = HttpStatus.FORBIDDEN
    )

    class InvalidTokenException : AuthException(
        AuthErrorCode.INVALID_TOKEN,
        status = HttpStatus.UNAUTHORIZED
    )

    class InvalidPasswordException(message: String? = null) : AuthException(
        AuthErrorCode.INVALID_PASSWORD,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    class JwtAuthenticationException(
        message: String? = null,
        details: Map<String, Any> = emptyMap()
    ) : AuthException(
        AuthErrorCode.JWT_VALIDATION_FAILED,
        message = message,
        metadata = details,
        status = HttpStatus.UNAUTHORIZED
    )

    class InvalidPasswordResetTokenException(message: String? = null) : AuthException(
        AuthErrorCode.PASSWORD_RESET_TOKEN_INVALID,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )
}
