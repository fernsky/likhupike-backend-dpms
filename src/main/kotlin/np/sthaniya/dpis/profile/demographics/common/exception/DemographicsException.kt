package np.sthaniya.dpis.profile.demographics.common.exception

import np.sthaniya.dpis.common.exception.ErrorCode
import np.sthaniya.dpis.common.exception.dpisException
import org.springframework.http.HttpStatus

/**
 * Base exception hierarchy for demographics related errors.
 *
 * Error codes follow the format DEMO_XXX for consistent error handling.
 *
 * @param errorCode Specific error code from [DemographicsErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class DemographicsException(
        errorCode: DemographicsErrorCode,
        message: String? = null,
        metadata: Map<String, Any> = emptyMap(),
        status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /** Error codes specific to demographics data failures */
    enum class DemographicsErrorCode : ErrorCode {
        DEMOGRAPHIC_DATA_NOT_FOUND {
            override val code = "DEMO_001"
            override val defaultMessage = "Demographic data not found"
            override val i18nKey = "demographics.error.DEMO_001"
        },
        DEMOGRAPHIC_DATA_ALREADY_EXISTS {
            override val code = "DEMO_002"
            override val defaultMessage = "Demographic data already exists"
            override val i18nKey = "demographics.error.DEMO_002"
        },
        INVALID_DEMOGRAPHIC_DATA {
            override val code = "DEMO_003"
            override val defaultMessage = "Invalid demographic data"
            override val i18nKey = "demographics.error.DEMO_003"
        },
        INVALID_FILTER_CRITERIA {
            override val code = "DEMO_004"
            override val defaultMessage = "Invalid filter criteria"
            override val i18nKey = "demographics.error.DEMO_004"
        },
        INVALID_DEMOGRAPHIC_FIELD {
            override val code = "DEMO_005"
            override val defaultMessage = "Invalid demographic field"
            override val i18nKey = "demographics.error.DEMO_005"
        }
    }

    /** Exception thrown when demographic data cannot be found by ID or other criteria */
    class DemographicDataNotFoundException(id: String) :
            DemographicsException(
                    DemographicsErrorCode.DEMOGRAPHIC_DATA_NOT_FOUND,
                    metadata = mapOf("id" to id),
                    status = HttpStatus.NOT_FOUND
            )

    /** Exception thrown when attempting to create duplicate demographic data */
    class DemographicDataAlreadyExistsException(message: String) :
            DemographicsException(
                    DemographicsErrorCode.DEMOGRAPHIC_DATA_ALREADY_EXISTS,
                    message = message,
                    status = HttpStatus.CONFLICT
            )

    /** Exception thrown when demographic data validation fails */
    class InvalidDemographicDataException(message: String, details: Map<String, Any> = emptyMap()) :
            DemographicsException(
                    DemographicsErrorCode.INVALID_DEMOGRAPHIC_DATA,
                    message = message,
                    metadata = details,
                    status = HttpStatus.BAD_REQUEST
            )

    /** Exception thrown when filter criteria are invalid */
    class InvalidFilterCriteriaException(message: String) :
            DemographicsException(
                    DemographicsErrorCode.INVALID_FILTER_CRITERIA,
                    message = message,
                    status = HttpStatus.BAD_REQUEST
            )

    /**
     * Exception thrown when an invalid field name is provided for demographic data.
     */
    class InvalidDemographicDataFieldException(message: String, details: Map<String, Any> = emptyMap()) :
            DemographicsException(
                    DemographicsErrorCode.INVALID_DEMOGRAPHIC_FIELD,
                    message = message,
                    metadata = details,
                    status = HttpStatus.BAD_REQUEST
            )
}
