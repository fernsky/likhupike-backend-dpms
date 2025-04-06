package np.sthaniya.dpis.location.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Exception thrown when address validation fails.
 *
 * This exception is typically thrown during validation of address data
 * when components of the address (province, district, municipality, ward)
 * don't exist or have inconsistent relationships.
 *
 * @param errorCode Specific error code indicating the nature of the address error
 * @param message Optional custom error message
 * @param metadata Additional error context data
 */
class AddressException(
    errorCode: AddressErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap()
) : dpisException(
    errorCode = errorCode,
    message = message,
    status = HttpStatus.BAD_REQUEST,
    metadata = metadata
) {
    /**
     * Error codes specific to address validation errors
     */
    enum class AddressErrorCode : ErrorCode {
        PROVINCE_NOT_FOUND {
            override val code = "ADDR_001"
            override val defaultMessage = "Province not found"
            override val i18nKey = "error.address.province_not_found"
        },
        DISTRICT_NOT_FOUND {
            override val code = "ADDR_002"
            override val defaultMessage = "District not found"
            override val i18nKey = "error.address.district_not_found"
        },
        MUNICIPALITY_NOT_FOUND {
            override val code = "ADDR_003"
            override val defaultMessage = "Municipality not found"
            override val i18nKey = "error.address.municipality_not_found"
        },
        WARD_NOT_FOUND {
            override val code = "ADDR_004"
            override val defaultMessage = "Ward not found"
            override val i18nKey = "error.address.ward_not_found"
        },
        DISTRICT_NOT_IN_PROVINCE {
            override val code = "ADDR_005"
            override val defaultMessage = "District does not belong to the specified province"
            override val i18nKey = "error.address.district_not_in_province"
        },
        MUNICIPALITY_NOT_IN_DISTRICT {
            override val code = "ADDR_006"
            override val defaultMessage = "Municipality does not belong to the specified district"
            override val i18nKey = "error.address.municipality_not_in_district"
        },
        WARD_NOT_IN_MUNICIPALITY {
            override val code = "ADDR_007"
            override val defaultMessage = "Ward does not belong to the specified municipality"
            override val i18nKey = "error.address.ward_not_in_municipality"
        },
        INVALID_ADDRESS {
            override val code = "ADDR_008"
            override val defaultMessage = "Address validation failed"
            override val i18nKey = "error.address.invalid"
        },
        INCOMPLETE_ADDRESS {
            override val code = "ADDR_009"
            override val defaultMessage = "Address information is incomplete"
            override val i18nKey = "error.address.incomplete"
        }
    }
}
