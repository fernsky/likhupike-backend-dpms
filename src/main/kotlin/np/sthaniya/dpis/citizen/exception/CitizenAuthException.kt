package np.sthaniya.dpis.citizen.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Base exception hierarchy for citizen authentication errors.
 *
 * Each exception type corresponds to a specific authentication failure scenario
 * and maps to an appropriate HTTP status code. Error codes follow the format CITIZEN_AUTH_XXX
 * for consistent error handling and client identification.
 *
 * Exception Categories:
 * 1. User State (001-005): Exceptions related to citizen existence and status
 * 2. Authentication (006-010): Login and token related failures
 * 3. Password Reset (011-013): OTP and password reset specific errors
 *
 * @param errorCode Specific error code from [CitizenAuthErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class CitizenAuthException(
    errorCode: CitizenAuthErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /**
     * Error codes specific to citizen authentication failures
     */
    enum class CitizenAuthErrorCode : ErrorCode {
        CITIZEN_NOT_FOUND {
            override val code = "CITIZEN_AUTH_001"
            override val defaultMessage = "Citizen not found"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_001"
        },
        CITIZEN_ALREADY_EXISTS {
            override val code = "CITIZEN_AUTH_002"
            override val defaultMessage = "Citizen already exists"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_002"
        },
        CITIZEN_NOT_APPROVED {
            override val code = "CITIZEN_AUTH_003"
            override val defaultMessage = "Citizen profile not approved"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_003"
        },
        CITIZEN_ACCOUNT_REJECTED {
            override val code = "CITIZEN_AUTH_004"
            override val defaultMessage = "Citizen account rejected"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_004"
        },
        CITIZEN_ACCOUNT_DISABLED {
            override val code = "CITIZEN_AUTH_005"
            override val defaultMessage = "Citizen account disabled"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_005"
        },
        INVALID_CREDENTIALS {
            override val code = "CITIZEN_AUTH_006"
            override val defaultMessage = "Invalid credentials"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_006"
        },
        INVALID_TOKEN {
            override val code = "CITIZEN_AUTH_007"
            override val defaultMessage = "Invalid or expired token"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_007"
        },
        UNAUTHENTICATED {
            override val code = "CITIZEN_AUTH_008"
            override val defaultMessage = "Authentication required"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_008"
        },
        JWT_VALIDATION_FAILED {
            override val code = "CITIZEN_AUTH_009"
            override val defaultMessage = "JWT token validation failed"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_009"
        },
        INVALID_PASSWORD {
            override val code = "CITIZEN_AUTH_010"
            override val defaultMessage = "Invalid password provided" 
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_010"
        },
        PASSWORD_RESET_OTP_INVALID {
            override val code = "CITIZEN_AUTH_011"
            override val defaultMessage = "Invalid or expired OTP"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_011"
        },
        PASSWORDS_DO_NOT_MATCH {
            override val code = "CITIZEN_AUTH_012"
            override val defaultMessage = "Passwords do not match"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_012"
        },
        TOO_MANY_ATTEMPTS {
            override val code = "CITIZEN_AUTH_013"
            override val defaultMessage = "Too many invalid attempts"
            override val i18nKey = "citizen.auth.error.CITIZEN_AUTH_013"
        };
    }

    /**
     * Exception thrown when a citizen cannot be found by ID or email
     */
    class CitizenNotFoundException(id: String) : CitizenAuthException(
        CitizenAuthErrorCode.CITIZEN_NOT_FOUND,
        metadata = mapOf("id" to id),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when attempting to create a citizen with an existing email or citizenship number
     */
    class CitizenAlreadyExistsException(email: String) : CitizenAuthException(
        CitizenAuthErrorCode.CITIZEN_ALREADY_EXISTS,
        metadata = mapOf("email" to email),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when a citizen is not yet approved for access
     */
    class CitizenNotApprovedException : CitizenAuthException(
        CitizenAuthErrorCode.CITIZEN_NOT_APPROVED,
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when a citizen's application has been rejected
     */
    class CitizenAccountRejectedException(message: String? = null) : CitizenAuthException(
        CitizenAuthErrorCode.CITIZEN_ACCOUNT_REJECTED,
        message = message,
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when a citizen account has been disabled
     */
    class CitizenAccountDisabledException : CitizenAuthException(
        CitizenAuthErrorCode.CITIZEN_ACCOUNT_DISABLED,
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when invalid credentials are provided during authentication
     */
    class InvalidCredentialsException : CitizenAuthException(
        CitizenAuthErrorCode.INVALID_CREDENTIALS,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when an invalid or expired token is provided
     */
    class InvalidTokenException : CitizenAuthException(
        CitizenAuthErrorCode.INVALID_TOKEN,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when authentication is required but not provided
     */
    class UnauthenticatedException : CitizenAuthException(
        CitizenAuthErrorCode.UNAUTHENTICATED,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when JWT token validation fails
     */
    class JwtAuthenticationException(
        message: String? = null,
        details: Map<String, Any> = emptyMap()
    ) : CitizenAuthException(
        CitizenAuthErrorCode.JWT_VALIDATION_FAILED,
        message = message,
        metadata = details,
        status = HttpStatus.UNAUTHORIZED
    )

    /**
     * Exception thrown when an invalid password is provided
     */
    class InvalidPasswordException(message: String? = null) : CitizenAuthException(
        CitizenAuthErrorCode.INVALID_PASSWORD,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when an invalid or expired OTP is provided for password reset
     */
    class InvalidPasswordResetTokenException(message: String? = null) : CitizenAuthException(
        CitizenAuthErrorCode.PASSWORD_RESET_OTP_INVALID,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when passwords do not match during password reset
     */
    class PasswordsDoNotMatchException : CitizenAuthException(
        CitizenAuthErrorCode.PASSWORDS_DO_NOT_MATCH,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when too many invalid attempts are made
     */
    class TooManyAttemptsException : CitizenAuthException(
        CitizenAuthErrorCode.TOO_MANY_ATTEMPTS,
        status = HttpStatus.BAD_REQUEST
    )
}
