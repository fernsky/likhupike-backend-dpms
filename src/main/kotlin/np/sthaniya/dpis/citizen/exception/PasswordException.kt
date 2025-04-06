package np.sthaniya.dpis.citizen.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Exception thrown when password validation fails or password operations encounter errors.
 *
 * This exception is typically thrown during authentication, registration,
 * or password change operations when provided passwords don't meet requirements
 * or don't match expected values.
 *
 * @param errorCode Specific error code indicating the nature of the password error
 * @param message Optional custom error message
 * @param metadata Additional error context data
 */
class InvalidPasswordException(
    errorCode: PasswordErrorCode = PasswordErrorCode.INVALID_PASSWORD,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap()
) : dpisException(
    errorCode = errorCode,
    message = message,
    status = HttpStatus.BAD_REQUEST,
    metadata = metadata
) {
    /**
     * Error codes specific to password validation and operations
     */
    enum class PasswordErrorCode : ErrorCode {
        INVALID_PASSWORD {
            override val code = "PWD_001"
            override val defaultMessage = "Invalid password"
            override val i18nKey = "error.password.invalid"
        },
        PASSWORD_MISMATCH {
            override val code = "PWD_002"
            override val defaultMessage = "Current password is incorrect"
            override val i18nKey = "error.password.mismatch"
        },
        
        PASSWORD_TOO_WEAK {
            override val code = "PWD_003"
            override val defaultMessage = "Password doesn't meet security requirements"
            override val i18nKey = "error.password.weak"
        },
        
        PASSWORD_CONTAINS_PERSONAL_INFO {
            override val code = "PWD_004"
            override val defaultMessage = "Password contains personal information"
            override val i18nKey = "error.password.personal_info"
        },
        
        PASSWORD_RECENTLY_USED {
            override val code = "PWD_005"
            override val defaultMessage = "Password was recently used"
            override val i18nKey = "error.password.recently_used"
        },
        
        PASSWORD_EXPIRED {
            override val code = "PWD_006"
            override val defaultMessage = "Password has expired and must be changed"
            override val i18nKey = "error.password.expired"
        },
        
        PASSWORD_TOO_SHORT {
            override val code = "PWD_007"
            override val defaultMessage = "Password is too short (minimum 8 characters)"
            override val i18nKey = "error.password.too_short"
        },
        
        PASSWORD_MISSING_REQUIREMENTS {
            override val code = "PWD_008"
            override val defaultMessage = "Password is missing required character types"
            override val i18nKey = "error.password.missing_requirements"
        }
    }
}
