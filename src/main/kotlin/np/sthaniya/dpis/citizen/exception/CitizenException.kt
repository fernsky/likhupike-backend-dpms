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
        CitizenErrorCode.CITIZEN_NOT_FOUND -> HttpStatus.NOT_FOUND
        CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
        CitizenErrorCode.DUPLICATE_EMAIL,
        CitizenErrorCode.CITIZEN_ALREADY_APPROVED,
        CitizenErrorCode.CITIZEN_ALREADY_DELETED -> HttpStatus.CONFLICT
        CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
        CitizenErrorCode.DOCUMENT_TOO_LARGE -> HttpStatus.BAD_REQUEST
        CitizenErrorCode.DOCUMENT_UPLOAD_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR
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
        }
    }
}
