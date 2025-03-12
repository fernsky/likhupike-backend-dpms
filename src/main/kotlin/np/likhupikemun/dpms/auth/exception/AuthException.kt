package np.likhupikemun.dpms.auth.exception

import np.likhupikemun.dpms.common.exception.DpmsException
import np.likhupikemun.dpms.common.exception.ErrorCode
import org.springframework.http.HttpStatus

sealed class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus = HttpStatus.BAD_REQUEST
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
        }
    }

    class UserNotFoundException(id: String) : AuthException(
        AuthErrorCode.USER_NOT_FOUND,
        metadata = mapOf("id" to id)
    )

    class UserAlreadyExistsException(email: String) : AuthException(
        AuthErrorCode.USER_ALREADY_EXISTS,
        metadata = mapOf("email" to email)
    )

    class UserAlreadyDeletedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_DELETED,
        metadata = mapOf("id" to id)
    )

    class UserAlreadyApprovedException(id: String) : AuthException(
        AuthErrorCode.USER_ALREADY_APPROVED,
        metadata = mapOf("id" to id)
    )

    class InvalidUserStateException(message: String) : AuthException(
        AuthErrorCode.INVALID_USER_STATE,
        message
    )

    class PermissionNotFoundException(type: String) : AuthException(
        AuthErrorCode.PERMISSION_NOT_FOUND,
        metadata = mapOf("type" to type)
    )

    class MissingPermissionsException(types: Set<String>) : AuthException(
        AuthErrorCode.MISSING_PERMISSIONS,
        metadata = mapOf("types" to types)
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
}
