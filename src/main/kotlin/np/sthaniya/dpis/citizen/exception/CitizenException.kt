package np.sthaniya.dpis.citizen.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Exception thrown when citizen-related operations encounter errors.
 *
 * This exception provides granular error codes for different validation and
 * operational failures related to citizen data management.
 *
 * @param errorCode Specific error code indicating the nature of the citizenship error
 * @param message Optional custom error message
 * @param metadata Additional error context data
 */
class CitizenException(
    errorCode: CitizenErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap()
) : dpisException(
    errorCode = errorCode,
    message = message,
    status = when (errorCode) {
        CitizenErrorCode.CITIZEN_NOT_FOUND,
        CitizenErrorCode.PAGE_NOT_FOUND -> HttpStatus.NOT_FOUND
        CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
        CitizenErrorCode.DUPLICATE_EMAIL,
        CitizenErrorCode.CITIZEN_ALREADY_APPROVED,
        CitizenErrorCode.CITIZEN_ALREADY_DELETED,
        CitizenErrorCode.CITIZEN_ALREADY_REGISTERED -> HttpStatus.CONFLICT
        CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
        CitizenErrorCode.DOCUMENT_TOO_LARGE,
        CitizenErrorCode.SELF_REGISTRATION_DISABLED,
        CitizenErrorCode.REGISTRATION_TOKEN_EXPIRED -> HttpStatus.BAD_REQUEST
        CitizenErrorCode.DOCUMENT_UPLOAD_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR
        CitizenErrorCode.INVALID_STATE_TRANSITION,
        CitizenErrorCode.INVALID_DOCUMENT_TYPE,
        CitizenErrorCode.DOCUMENT_NOT_FOUND -> HttpStatus.BAD_REQUEST
        else -> HttpStatus.BAD_REQUEST
    },
    metadata = metadata
) {
    /**
     * Error codes specific to citizen validation and operations
     */
    enum class CitizenErrorCode : ErrorCode {
        CITIZEN_NOT_FOUND {
            override val code = "CIT_001"
            override val defaultMessage = "Citizen not found"
            override val i18nKey = "error.citizen.not_found"
        },
        
        DUPLICATE_CITIZENSHIP_NUMBER {
            override val code = "CIT_002"
            override val defaultMessage = "Citizenship number already exists"
            override val i18nKey = "error.citizen.duplicate_citizenship_number"
        },
        
        DUPLICATE_EMAIL {
            override val code = "CIT_003"
            override val defaultMessage = "Email already registered to another citizen"
            override val i18nKey = "error.citizen.duplicate_email"
        },
        
        INVALID_CITIZENSHIP_DATA {
            override val code = "CIT_004"
            override val defaultMessage = "Invalid citizenship certificate data"
            override val i18nKey = "error.citizen.invalid_citizenship_data"
        },
        
        CITIZEN_ALREADY_APPROVED {
            override val code = "CIT_005"
            override val defaultMessage = "Citizen record already approved"
            override val i18nKey = "error.citizen.already_approved"
        },
        
        CITIZEN_ALREADY_DELETED {
            override val code = "CIT_006"
            override val defaultMessage = "Citizen record already deleted"
            override val i18nKey = "error.citizen.already_deleted"
        },
        
        INVALID_ADDRESS_DATA {
            override val code = "CIT_007"
            override val defaultMessage = "Invalid address data"
            override val i18nKey = "error.citizen.invalid_address"
        },
        
        MISSING_REQUIRED_DATA {
            override val code = "CIT_008"
            override val defaultMessage = "Missing required citizen data"
            override val i18nKey = "error.citizen.missing_required_data"
        },
        
        INVALID_AGE {
            override val code = "CIT_009"
            override val defaultMessage = "Citizen does not meet age requirements"
            override val i18nKey = "error.citizen.invalid_age"
        },
        
        APPROVAL_REQUIRED {
            override val code = "CIT_010"
            override val defaultMessage = "Citizen record approval required for this operation"
            override val i18nKey = "error.citizen.approval_required"
        },
        
        CITIZEN_PROFILE_INCOMPLETE {
            override val code = "CIT_011"
            override val defaultMessage = "Citizen profile is incomplete"
            override val i18nKey = "error.citizen.profile_incomplete"
        },
        
        INVALID_DOCUMENT_FORMAT {
            override val code = "CIT_012"
            override val defaultMessage = "Invalid document format"
            override val i18nKey = "error.citizen.invalid_document_format"
        },
        
        DOCUMENT_TOO_LARGE {
            override val code = "CIT_013"
            override val defaultMessage = "Document size exceeds the allowable limit"
            override val i18nKey = "error.citizen.document_too_large"
        },
        
        DOCUMENT_UPLOAD_FAILED {
            override val code = "CIT_014"
            override val defaultMessage = "Document upload failed due to server error"
            override val i18nKey = "error.citizen.document_upload_failed"
        },

        CITIZEN_ALREADY_REGISTERED {
            override val code = "CIT_015"
            override val defaultMessage = "Citizen with this citizenship number is already registered"
            override val i18nKey = "error.citizen.already_registered"
        },

        SELF_REGISTRATION_DISABLED {
            override val code = "CIT_016"
            override val defaultMessage = "Self-registration is currently disabled"
            override val i18nKey = "error.citizen.self_registration_disabled"
        },

        REGISTRATION_TOKEN_EXPIRED {
            override val code = "CIT_017"
            override val defaultMessage = "Registration verification token has expired"
            override val i18nKey = "error.citizen.registration_token_expired"
        },

        INVALID_REGISTRATION_TOKEN {
            override val code = "CIT_018"
            override val defaultMessage = "Invalid registration verification token"
            override val i18nKey = "error.citizen.invalid_registration_token"
        },
        
        PAGE_NOT_FOUND {
            override val code = "CIT_019"
            override val defaultMessage = "Requested page does not exist"
            override val i18nKey = "error.citizen.page_not_found"
        },
        DOCUMENT_NOT_FOUND {
            override val code = "CIT_020"
            override val defaultMessage = "Document not found"
            override val i18nKey = "error.citizen.document_not_found"
        },

        INVALID_DOCUMENT_TYPE {
            override val code = "CIT_021"
            override val defaultMessage = "Invalid document type"
            override val i18nKey = "error.citizen.invalid_document_type"
        },

        INVALID_STATE_TRANSITION {
            override val code = "CIT_022"
            override val defaultMessage = "Invalid state transition for citizen record"
            override val i18nKey = "error.citizen.invalid_state_transition"
        }
    }
}
