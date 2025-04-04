package np.sthaniya.dpis.location.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Base exception hierarchy for location-related errors.
 *
 * Each exception type corresponds to a specific location-related failure scenario
 * and maps to an appropriate HTTP status code. Error codes follow the format LOC_XXX
 * for consistent error handling and client identification.
 *
 * Exception Categories:
 * 1. Province (001-010): Exceptions related to provinces
 * 2. District (011-020): Exceptions related to districts
 * 3. Municipality (021-030): Exceptions related to municipalities
 * 4. Ward (031-040): Exceptions related to wards
 * 5. Validation (041-050): Validation errors for location data
 * 6. Geographic Search (051-060): Errors in geographic searches
 *
 * @param errorCode Specific error code from [LocationErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class LocationException(
    errorCode: LocationErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /**
     * Error codes specific to location-related failures
     */
    enum class LocationErrorCode : ErrorCode {
        // Province error codes
        PROVINCE_NOT_FOUND {
            override val code = "LOC_001"
            override val defaultMessage = "Province not found"
            override val i18nKey = "location.error.LOC_001"
        },
        PROVINCE_CODE_EXISTS {
            override val code = "LOC_002"
            override val defaultMessage = "Province code already exists"
            override val i18nKey = "location.error.LOC_002"
        },
        PROVINCE_HAS_ACTIVE_DISTRICTS {
            override val code = "LOC_003"
            override val defaultMessage = "Province has active districts"
            override val i18nKey = "location.error.LOC_003"
        },
        PROVINCE_OPERATION_ERROR {
            override val code = "LOC_004"
            override val defaultMessage = "Province operation error"
            override val i18nKey = "location.error.LOC_004"
        },
        
        // District error codes
        DISTRICT_NOT_FOUND {
            override val code = "LOC_011"
            override val defaultMessage = "District not found"
            override val i18nKey = "location.error.LOC_011"
        },
        DISTRICT_CODE_EXISTS {
            override val code = "LOC_012"
            override val defaultMessage = "District code already exists"
            override val i18nKey = "location.error.LOC_012"
        },
        DUPLICATE_DISTRICT_CODE {
            override val code = "LOC_013"
            override val defaultMessage = "Duplicate district code"
            override val i18nKey = "location.error.LOC_013"
        },
        DISTRICT_HAS_ACTIVE_MUNICIPALITIES {
            override val code = "LOC_014"
            override val defaultMessage = "District has active municipalities"
            override val i18nKey = "location.error.LOC_014"
        },
        INVALID_DISTRICT_PROVINCE {
            override val code = "LOC_015"
            override val defaultMessage = "District does not belong to province"
            override val i18nKey = "location.error.LOC_015"
        },
        DISTRICT_OPERATION_ERROR {
            override val code = "LOC_016"
            override val defaultMessage = "District operation error"
            override val i18nKey = "location.error.LOC_016"
        },
        
        // Municipality error codes
        MUNICIPALITY_NOT_FOUND {
            override val code = "LOC_021"
            override val defaultMessage = "Municipality not found"
            override val i18nKey = "location.error.LOC_021"
        },
        DUPLICATE_MUNICIPALITY_CODE {
            override val code = "LOC_022"
            override val defaultMessage = "Duplicate municipality code"
            override val i18nKey = "location.error.LOC_022"
        },
        MUNICIPALITY_HAS_ACTIVE_WARDS {
            override val code = "LOC_023"
            override val defaultMessage = "Municipality has active wards"
            override val i18nKey = "location.error.LOC_023"
        },
        INVALID_MUNICIPALITY_DISTRICT {
            override val code = "LOC_024"
            override val defaultMessage = "Municipality does not belong to district"
            override val i18nKey = "location.error.LOC_024"
        },
        MUNICIPALITY_ACCESS_DENIED {
            override val code = "LOC_025"
            override val defaultMessage = "Access denied to municipality"
            override val i18nKey = "location.error.LOC_025"
        },
        MUNICIPALITY_OPERATION_ERROR {
            override val code = "LOC_026"
            override val defaultMessage = "Municipality operation error"
            override val i18nKey = "location.error.LOC_026"
        },
        
        // Ward error codes
        WARD_NOT_FOUND {
            override val code = "LOC_031"
            override val defaultMessage = "Ward not found"
            override val i18nKey = "location.error.LOC_031"
        },
        DUPLICATE_WARD_NUMBER {
            override val code = "LOC_032"
            override val defaultMessage = "Duplicate ward number"
            override val i18nKey = "location.error.LOC_032"
        },
        WARD_ACCESS_DENIED {
            override val code = "LOC_033"
            override val defaultMessage = "Access denied to ward"
            override val i18nKey = "location.error.LOC_033"
        },
        WARD_OPERATION_ERROR {
            override val code = "LOC_034"
            override val defaultMessage = "Ward operation error" 
            override val i18nKey = "location.error.LOC_034"
        },
        
        // Validation error codes
        INVALID_LOCATION_DATA {
            override val code = "LOC_041"
            override val defaultMessage = "Invalid location data"
            override val i18nKey = "location.error.LOC_041"
        },
        INVALID_COORDINATES {
            override val code = "LOC_042"
            override val defaultMessage = "Invalid coordinates"
            override val i18nKey = "location.error.LOC_042"
        },
        INVALID_POPULATION {
            override val code = "LOC_043"
            override val defaultMessage = "Invalid population value"
            override val i18nKey = "location.error.LOC_043"
        },
        INVALID_AREA {
            override val code = "LOC_044"
            override val defaultMessage = "Invalid area value"
            override val i18nKey = "location.error.LOC_044"
        },
        INVALID_WARD_COUNT {
            override val code = "LOC_045"
            override val defaultMessage = "Invalid ward count"
            override val i18nKey = "location.error.LOC_045"
        },
        
        // Geographic search error codes
        GEO_SEARCH_ERROR {
            override val code = "LOC_051"
            override val defaultMessage = "Geographic search error"
            override val i18nKey = "location.error.LOC_051"
        },
        INVALID_SEARCH_RADIUS {
            override val code = "LOC_052"
            override val defaultMessage = "Invalid search radius"
            override val i18nKey = "location.error.LOC_052"
        },
        EXCEEDED_MAX_RADIUS {
            override val code = "LOC_053"
            override val defaultMessage = "Exceeded maximum search radius"
            override val i18nKey = "location.error.LOC_053"
        };
    }

    // Province Exceptions
    class ProvinceNotFoundException(code: String) : LocationException(
        LocationErrorCode.PROVINCE_NOT_FOUND,
        metadata = mapOf("code" to code),
        status = HttpStatus.NOT_FOUND
    )

    class ProvinceCodeExistsException(code: String) : LocationException(
        LocationErrorCode.PROVINCE_CODE_EXISTS,
        metadata = mapOf("code" to code),
        status = HttpStatus.CONFLICT
    )

    class ProvinceOperationException(
        message: String,
        errorCode: LocationErrorCode = LocationErrorCode.PROVINCE_OPERATION_ERROR
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.FORBIDDEN
    ) {
        companion object {
            fun hasActiveDistricts(provinceCode: String) =
                ProvinceOperationException(
                    message = "Cannot deactivate province with code $provinceCode as it has active districts",
                    errorCode = LocationErrorCode.PROVINCE_HAS_ACTIVE_DISTRICTS
                )
        }
    }

    // District Exceptions
    class DistrictNotFoundException(code: String) : LocationException(
        LocationErrorCode.DISTRICT_NOT_FOUND,
        metadata = mapOf("code" to code),
        status = HttpStatus.NOT_FOUND
    )

    class DistrictCodeExistsException(code: String) : LocationException(
        LocationErrorCode.DISTRICT_CODE_EXISTS,
        metadata = mapOf("code" to code),
        status = HttpStatus.CONFLICT
    )

    class DuplicateDistrictCodeException(code: String, provinceCode: String) : LocationException(
        LocationErrorCode.DUPLICATE_DISTRICT_CODE,
        metadata = mapOf("code" to code, "provinceCode" to provinceCode),
        status = HttpStatus.CONFLICT
    )

    class DistrictOperationException(
        message: String,
        errorCode: LocationErrorCode = LocationErrorCode.DISTRICT_OPERATION_ERROR
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.FORBIDDEN
    ) {
        companion object {
            fun hasActiveMunicipalities(districtCode: String) =
                DistrictOperationException(
                    message = "Cannot deactivate district with code $districtCode as it has active municipalities",
                    errorCode = LocationErrorCode.DISTRICT_HAS_ACTIVE_MUNICIPALITIES
                )

            fun invalidProvince(districtCode: String, provinceCode: String) = 
                DistrictOperationException(
                    message = "District $districtCode does not belong to province $provinceCode",
                    errorCode = LocationErrorCode.INVALID_DISTRICT_PROVINCE
                )
        }
    }

    // Municipality Exceptions
    class MunicipalityNotFoundException(code: String) : LocationException(
        LocationErrorCode.MUNICIPALITY_NOT_FOUND,
        metadata = mapOf("code" to code),
        status = HttpStatus.NOT_FOUND
    )

    class DuplicateMunicipalityCodeException(code: String, districtCode: String) : LocationException(
        LocationErrorCode.DUPLICATE_MUNICIPALITY_CODE,
        metadata = mapOf("code" to code, "districtCode" to districtCode),
        status = HttpStatus.CONFLICT
    )

    class MunicipalityOperationException(
        message: String,
        errorCode: LocationErrorCode = LocationErrorCode.MUNICIPALITY_OPERATION_ERROR
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.FORBIDDEN
    ) {
        companion object {
            fun hasActiveWards(municipalityCode: String) =
                MunicipalityOperationException(
                    message = "Cannot deactivate municipality with code $municipalityCode as it has active wards",
                    errorCode = LocationErrorCode.MUNICIPALITY_HAS_ACTIVE_WARDS
                )

            fun invalidDistrict(municipalityCode: String, districtCode: String) = 
                MunicipalityOperationException(
                    message = "Municipality $municipalityCode does not belong to district $districtCode",
                    errorCode = LocationErrorCode.INVALID_MUNICIPALITY_DISTRICT
                )

            fun accessDenied(municipalityCode: String) =
                MunicipalityOperationException(
                    message = "Access denied to municipality with code $municipalityCode",
                    errorCode = LocationErrorCode.MUNICIPALITY_ACCESS_DENIED
                )
        }
    }

    // Ward Exceptions
    class WardNotFoundException(wardNumber: Int, municipalityCode: String) : LocationException(
        LocationErrorCode.WARD_NOT_FOUND,
        metadata = mapOf("wardNumber" to wardNumber, "municipalityCode" to municipalityCode),
        status = HttpStatus.NOT_FOUND
    )

    class DuplicateWardNumberException(wardNumber: Int, municipalityCode: String) : LocationException(
        LocationErrorCode.DUPLICATE_WARD_NUMBER,
        metadata = mapOf("wardNumber" to wardNumber, "municipalityCode" to municipalityCode),
        status = HttpStatus.CONFLICT
    )

    class InvalidWardOperationException(
        message: String = "Operation not allowed for this ward",
        errorCode: LocationErrorCode = LocationErrorCode.WARD_OPERATION_ERROR
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.FORBIDDEN
    ) {
        companion object {
            fun accessDenied(wardNumber: Int) =
                InvalidWardOperationException(
                    message = "Access denied to ward number $wardNumber",
                    errorCode = LocationErrorCode.WARD_ACCESS_DENIED
                )
        }
    }

    // Validation Exceptions
    class InvalidLocationDataException(
        message: String,
        errorCode: LocationErrorCode = LocationErrorCode.INVALID_LOCATION_DATA
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.BAD_REQUEST
    ) {
        companion object {
            fun invalidCoordinates(latitude: Double, longitude: Double) = 
                InvalidLocationDataException(
                    message = "Invalid coordinates: ($latitude, $longitude)",
                    errorCode = LocationErrorCode.INVALID_COORDINATES
                )

            fun invalidPopulation(value: Long) =
                InvalidLocationDataException(
                    message = "Invalid population value: $value",
                    errorCode = LocationErrorCode.INVALID_POPULATION
                )

            fun invalidArea(value: Double) =
                InvalidLocationDataException(
                    message = "Invalid area value: $value",
                    errorCode = LocationErrorCode.INVALID_AREA
                )

            fun invalidWardCount(value: Int) =
                InvalidLocationDataException(
                    message = "Invalid ward count: $value",
                    errorCode = LocationErrorCode.INVALID_WARD_COUNT
                )
        }
    }

    // Geographic Search Exceptions
    class GeoSearchException(
        message: String,
        errorCode: LocationErrorCode = LocationErrorCode.GEO_SEARCH_ERROR
    ) : LocationException(
        errorCode,
        message = message,
        status = HttpStatus.BAD_REQUEST
    ) {
        companion object {
            fun invalidRadius(radius: Double) =
                GeoSearchException(
                    message = "Invalid search radius: $radius",
                    errorCode = LocationErrorCode.INVALID_SEARCH_RADIUS
                )

            fun exceededMaxRadius(radius: Double, maxRadius: Double) = 
                GeoSearchException(
                    message = "Search radius $radius exceeds maximum allowed radius of $maxRadius",
                    errorCode = LocationErrorCode.EXCEEDED_MAX_RADIUS
                )
        }
    }
}
