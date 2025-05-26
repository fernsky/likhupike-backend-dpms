package np.sthaniya.dpis.profile.economics.common.exception

import np.sthaniya.dpis.common.exception.ErrorCode
import np.sthaniya.dpis.common.exception.dpisException
import org.springframework.http.HttpStatus

/**
 * Base exception hierarchy for economics related errors.
 *
 * Error codes follow the format ECON_XXX for consistent error handling.
 *
 * @param errorCode Specific error code from [EconomicsErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class EconomicsException(
        errorCode: EconomicsErrorCode,
        message: String? = null,
        metadata: Map<String, Any> = emptyMap(),
        status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /** Error codes specific to economics data failures */
    enum class EconomicsErrorCode : ErrorCode {
        ECONOMIC_DATA_NOT_FOUND {
            override val code = "ECON_001"
            override val defaultMessage = "Economic data not found"
            override val i18nKey = "economics.error.ECON_001"
        },
        ECONOMIC_DATA_ALREADY_EXISTS {
            override val code = "ECON_002"
            override val defaultMessage = "Economic data already exists"
            override val i18nKey = "economics.error.ECON_002"
        },
        INVALID_ECONOMIC_DATA {
            override val code = "ECON_003"
            override val defaultMessage = "Invalid economic data"
            override val i18nKey = "economics.error.ECON_003"
        },
        INVALID_FILTER_CRITERIA {
            override val code = "ECON_004"
            override val defaultMessage = "Invalid filter criteria"
            override val i18nKey = "economics.error.ECON_004"
        },
        INVALID_ECONOMIC_FIELD {
            override val code = "ECON_005"
            override val defaultMessage = "Invalid economic field"
            override val i18nKey = "economics.error.ECON_005"
        }
    }

    /**
     * Exception thrown when economic data is not found.
     */
    class EconomicDataNotFoundException(id: String) :
            EconomicsException(
                    EconomicsErrorCode.ECONOMIC_DATA_NOT_FOUND,
                    metadata = mapOf("id" to id),
                    status = HttpStatus.NOT_FOUND
            )

    /**
     * Exception thrown when economic data already exists.
     */
    class EconomicDataAlreadyExistsException(message: String) :
            EconomicsException(
                    EconomicsErrorCode.ECONOMIC_DATA_ALREADY_EXISTS,
                    message = message,
                    status = HttpStatus.CONFLICT
            )

    /**
     * Exception thrown when economic data validation fails
     */
    class InvalidEconomicDataException(message: String, details: Map<String, Any> = emptyMap()) :
            EconomicsException(
                    EconomicsErrorCode.INVALID_ECONOMIC_DATA,
                    message = message,
                    metadata = details,
                    status = HttpStatus.BAD_REQUEST
            )

    /**
     * Exception thrown when filter criteria are invalid
     */
    class InvalidFilterCriteriaException(message: String) :
            EconomicsException(
                    EconomicsErrorCode.INVALID_FILTER_CRITERIA,
                    message = message,
                    status = HttpStatus.BAD_REQUEST
            )

    /**
     * Exception thrown when an invalid field name is provided for economic data.
     */
    class InvalidEconomicDataFieldException(message: String, details: Map<String, Any> = emptyMap()) :
            EconomicsException(
                    EconomicsErrorCode.INVALID_ECONOMIC_FIELD,
                    message = message,
                    metadata = details,
                    status = HttpStatus.BAD_REQUEST
            )
}
