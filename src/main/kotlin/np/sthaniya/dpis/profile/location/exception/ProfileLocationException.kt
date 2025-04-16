package np.sthaniya.dpis.profile.location.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus
import java.util.UUID

/**
 * Base exception hierarchy for location-related errors.
 *
 * Each exception type corresponds to a specific location operation failure scenario
 * and maps to an appropriate HTTP status code. Error codes follow consistent formatting
 * for predictable error handling and client identification.
 *
 * Exception Categories:
 * 1. Municipality (001-099): Municipality-related exceptions
 * 2. Ward (100-199): Ward-related exceptions
 * 3. Settlement (200-299): Settlement-related exceptions
 * 4. General Location (300-399): General location data exceptions
 *
 * @param errorCode Specific error code from [LocationErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class ProfileLocationException(
    errorCode: LocationErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /**
     * Error codes specific to location validation and operations
     */
    enum class LocationErrorCode : ErrorCode {
        // Municipality error codes
        MUNICIPALITY_NOT_FOUND {
            override val code = "LOC_001"
            override val defaultMessage = "Municipality not found"
            override val i18nKey = "error.location.municipality.not_found"
        },
        
        MUNICIPALITY_ALREADY_EXISTS {
            override val code = "LOC_002"
            override val defaultMessage = "Municipality already exists"
            override val i18nKey = "error.location.municipality.already_exists"
        },
        
        INVALID_MUNICIPALITY_DATA {
            override val code = "LOC_003"
            override val defaultMessage = "Invalid municipality data"
            override val i18nKey = "error.location.municipality.invalid_data"
        },
        
        INVALID_GEOLOCATION_DATA {
            override val code = "LOC_004"
            override val defaultMessage = "Invalid geolocation data"
            override val i18nKey = "error.location.invalid_geolocation_data"
        },
        
        // Ward error codes
        WARD_NOT_FOUND {
            override val code = "LOC_101"
            override val defaultMessage = "Ward not found"
            override val i18nKey = "error.location.ward.not_found"
        },
        
        DUPLICATE_WARD_NUMBER {
            override val code = "LOC_102"
            override val defaultMessage = "Ward with this number already exists"
            override val i18nKey = "error.location.ward.duplicate_number"
        },
        
        INVALID_WARD_DATA {
            override val code = "LOC_103"
            override val defaultMessage = "Invalid ward data"
            override val i18nKey = "error.location.ward.invalid_data"
        },
        
        MUNICIPALITY_REQUIRED {
            override val code = "LOC_104"
            override val defaultMessage = "Municipality must be created before adding wards"
            override val i18nKey = "error.location.municipality.required"
        },
        
        // Settlement error codes
        SETTLEMENT_NOT_FOUND {
            override val code = "LOC_201"
            override val defaultMessage = "Settlement not found"
            override val i18nKey = "error.location.settlement.not_found"
        },
        
        INVALID_SETTLEMENT_DATA {
            override val code = "LOC_202"
            override val defaultMessage = "Invalid settlement data"
            override val i18nKey = "error.location.settlement.invalid_data"
        },
        
        DUPLICATE_SETTLEMENT_NAME {
            override val code = "LOC_203"
            override val defaultMessage = "Settlement with this name already exists in the ward"
            override val i18nKey = "error.location.settlement.duplicate_name"
        }
    }

    /**
     * Exception thrown when a municipality isn't found or doesn't exist yet
     */
    class MunicipalityNotFoundException : ProfileLocationException(
        LocationErrorCode.MUNICIPALITY_NOT_FOUND,
        message = "Municipality not found. Please create one first.",
        status = HttpStatus.NOT_FOUND
    )
    
    /**
     * Exception thrown when attempting to create a second municipality
     */
    class MunicipalityAlreadyExistsException : ProfileLocationException(
        LocationErrorCode.MUNICIPALITY_ALREADY_EXISTS,
        message = "Municipality already exists. Only one municipality is allowed.",
        status = HttpStatus.CONFLICT
    )
    
    /**
     * Exception thrown when invalid municipality data is provided
     */
    class InvalidMunicipalityDataException(message: String? = null) : ProfileLocationException(
        LocationErrorCode.INVALID_MUNICIPALITY_DATA,
        message = message ?: "Invalid municipality data provided.",
        status = HttpStatus.BAD_REQUEST
    )
    
    /**
     * Exception thrown when invalid geolocation data is provided
     */
    class InvalidGeolocationDataException(message: String? = null) : ProfileLocationException(
        LocationErrorCode.INVALID_GEOLOCATION_DATA,
        message = message ?: "Invalid geolocation data provided.",
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a ward is not found by ID
     */
    class WardNotFoundException(id: UUID) : ProfileLocationException(
        LocationErrorCode.WARD_NOT_FOUND,
        message = "Ward with ID $id not found.",
        metadata = mapOf("wardId" to id),
        status = HttpStatus.NOT_FOUND
    )
    
    /**
     * Exception thrown when a ward is not found by number
     */
    class WardNotFoundByNumberException(number: Int) : ProfileLocationException(
        LocationErrorCode.WARD_NOT_FOUND,
        message = "Ward with number $number not found.",
        metadata = mapOf("wardNumber" to number),
        status = HttpStatus.NOT_FOUND
    )
    
    /**
     * Exception thrown when a duplicate ward number is detected
     */
    class DuplicateWardNumberException(number: Int) : ProfileLocationException(
        LocationErrorCode.DUPLICATE_WARD_NUMBER,
        message = "Ward with number $number already exists.",
        metadata = mapOf("wardNumber" to number),
        status = HttpStatus.CONFLICT
    )
    
    /**
     * Exception thrown when a settlement is not found
     */
    class SettlementNotFoundException(id: UUID) : ProfileLocationException(
        LocationErrorCode.SETTLEMENT_NOT_FOUND,
        message = "Settlement with ID $id not found.",
        metadata = mapOf("settlementId" to id),
        status = HttpStatus.NOT_FOUND
    )
    
    /**
     * Exception thrown when a municipality is required but not present
     */
    class MunicipalityRequiredException : ProfileLocationException(
        LocationErrorCode.MUNICIPALITY_REQUIRED,
        message = "Municipality not found. Please create a municipality first.",
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a duplicate settlement name is detected in the same ward
     */
    class DuplicateSettlementNameException(
        wardNumber: Int, 
        settlementName: String
    ) : ProfileLocationException(
        LocationErrorCode.DUPLICATE_SETTLEMENT_NAME,
        message = "Settlement with name '$settlementName' already exists in ward $wardNumber",
        metadata = mapOf("wardNumber" to wardNumber, "settlementName" to settlementName),
        status = HttpStatus.CONFLICT
    )
}
