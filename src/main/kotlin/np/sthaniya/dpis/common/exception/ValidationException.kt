package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus

/**
 * Exception thrown when request validation fails.
 * 
 * This exception provides detailed validation errors as metadata
 * to help clients identify and correct invalid input.
 *
 * @param message Error message describing the validation failure
 * @param errors Map of field names to error messages for specific field validation failures
 * @param errorCode Specific validation error code
 * @param status HTTP status code to return (defaults to BAD_REQUEST)
 */
class ValidationException(
    message: String,
    errors: Map<String, String> = emptyMap(),
    errorCode: ValidationErrorCode = ValidationErrorCode.VALIDATION_ERROR,
    status: HttpStatus = HttpStatus.BAD_REQUEST
) : dpisException(errorCode, message, status, errors.ifEmpty { null }?.let { mapOf("errors" to it) } ?: emptyMap()) {

    /**
     * Error codes specific to validation failures
     */
    enum class ValidationErrorCode : ErrorCode {
        VALIDATION_ERROR {
            override val code = "VAL_001"
            override val defaultMessage = "Validation failed"
            override val i18nKey = "validation.error.VAL_001"
        },
        INVALID_FORMAT {
            override val code = "VAL_002"
            override val defaultMessage = "Invalid format"
            override val i18nKey = "validation.error.VAL_002"
        },
        REQUIRED_FIELD_MISSING {
            override val code = "VAL_003"
            override val defaultMessage = "Required field missing"
            override val i18nKey = "validation.error.VAL_003"
        },
        INVALID_VALUE_RANGE {
            override val code = "VAL_004"
            override val defaultMessage = "Value out of allowed range"
            override val i18nKey = "validation.error.VAL_004"
        },
        INVALID_PAGINATION {
            override val code = "VAL_005"
            override val defaultMessage = "Invalid pagination parameters"
            override val i18nKey = "validation.error.VAL_005"
        };
    }

    /**
     * Get validation errors map
     */
    @Suppress("UNCHECKED_CAST")
    fun getErrors(): Map<String, String> = 
        (metadata["errors"] as? Map<String, String>) ?: emptyMap()
        
    companion object {
        /**
         * Create a validation exception for a required field
         */
        fun requiredField(fieldName: String): ValidationException =
            ValidationException(
                message = "Required field '$fieldName' is missing",
                errors = mapOf(fieldName to "Field is required"),
                errorCode = ValidationErrorCode.REQUIRED_FIELD_MISSING
            )
            
        /**
         * Create a validation exception for an invalid format
         */
        fun invalidFormat(fieldName: String, expectedFormat: String): ValidationException =
            ValidationException(
                message = "Invalid format for field '$fieldName'",
                errors = mapOf(fieldName to "Expected format: $expectedFormat"),
                errorCode = ValidationErrorCode.INVALID_FORMAT
            )
            
        /**
         * Create a validation exception for pagination errors
         */
        fun invalidPagination(message: String, errors: Map<String, String>): ValidationException =
            ValidationException(
                message = message,
                errors = errors,
                errorCode = ValidationErrorCode.INVALID_PAGINATION
            )
    }
}
