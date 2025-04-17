package np.sthaniya.dpis.profile.institutions.cooperatives.exception

import np.sthaniya.dpis.common.exception.dpisException
import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Base exception hierarchy for cooperative-related errors.
 *
 * Each exception type corresponds to a specific cooperative operation failure scenario
 * and maps to an appropriate HTTP status code. Error codes follow the format COOPERATIVE_XXX
 * for consistent error handling and client identification.
 *
 * Exception Categories:
 * 1. Entity State (001-005): Exceptions related to cooperative existence and status
 * 2. Data Validation (006-010): Invalid or duplicate data
 * 3. Media Operations (011-015): Media upload and management errors
 * 4. Translation (016-020): Translation-related errors
 * 5. Geography (021-025): GIS and location-related errors
 * 6. Access Control (026-030): Permission-related errors
 *
 * @param errorCode Specific error code from [CooperativeErrorCode]
 * @param message Optional custom error message
 * @param metadata Additional error context data
 * @param status HTTP status code to return
 */
sealed class CooperativeException(
    errorCode: CooperativeErrorCode,
    message: String? = null,
    metadata: Map<String, Any> = emptyMap(),
    status: HttpStatus
) : dpisException(errorCode, message, status, metadata) {

    /**
     * Error codes specific to cooperative operation failures
     */
    enum class CooperativeErrorCode : ErrorCode {
        COOPERATIVE_NOT_FOUND {
            override val code = "COOPERATIVE_001"
            override val defaultMessage = "Cooperative not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_001"
        },
        COOPERATIVE_ALREADY_EXISTS {
            override val code = "COOPERATIVE_002"
            override val defaultMessage = "Cooperative with the same code already exists"
            override val i18nKey = "cooperative.error.COOPERATIVE_002"
        },
        COOPERATIVE_INACTIVE {
            override val code = "COOPERATIVE_003"
            override val defaultMessage = "Cooperative is inactive"
            override val i18nKey = "cooperative.error.COOPERATIVE_003"
        },
        COOPERATIVE_PENDING_APPROVAL {
            override val code = "COOPERATIVE_004"
            override val defaultMessage = "Cooperative is pending approval"
            override val i18nKey = "cooperative.error.COOPERATIVE_004"
        },
        COOPERATIVE_DISSOLVED {
            override val code = "COOPERATIVE_005"
            override val defaultMessage = "Cooperative has been dissolved"
            override val i18nKey = "cooperative.error.COOPERATIVE_005"
        },
        INVALID_COOPERATIVE_DATA {
            override val code = "COOPERATIVE_006"
            override val defaultMessage = "Invalid cooperative data"
            override val i18nKey = "cooperative.error.COOPERATIVE_006"
        },
        DUPLICATE_SLUG_URL {
            override val code = "COOPERATIVE_007"
            override val defaultMessage = "Duplicate slug URL"
            override val i18nKey = "cooperative.error.COOPERATIVE_007"
        },
        TRANSLATION_REQUIRED {
            override val code = "COOPERATIVE_008"
            override val defaultMessage = "At least one translation is required"
            override val i18nKey = "cooperative.error.COOPERATIVE_008"
        },
        INVALID_GEOLOCATION_DATA {
            override val code = "COOPERATIVE_009"
            override val defaultMessage = "Invalid geolocation data"
            override val i18nKey = "cooperative.error.COOPERATIVE_009"
        },
        INVALID_WARD_DATA {
            override val code = "COOPERATIVE_010"
            override val defaultMessage = "Invalid ward data"
            override val i18nKey = "cooperative.error.COOPERATIVE_010"
        },
        MEDIA_UPLOAD_FAILED {
            override val code = "COOPERATIVE_011"
            override val defaultMessage = "Media upload failed"
            override val i18nKey = "cooperative.error.COOPERATIVE_011"
        },
        MEDIA_NOT_FOUND {
            override val code = "COOPERATIVE_012"
            override val defaultMessage = "Media not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_012"
        },
        MEDIA_TOO_LARGE {
            override val code = "COOPERATIVE_013"
            override val defaultMessage = "Media file too large"
            override val i18nKey = "cooperative.error.COOPERATIVE_013"
        },
        INVALID_MEDIA_FORMAT {
            override val code = "COOPERATIVE_014"
            override val defaultMessage = "Invalid media format"
            override val i18nKey = "cooperative.error.COOPERATIVE_014"
        },
        MEDIA_DELETE_FAILED {
            override val code = "COOPERATIVE_015"
            override val defaultMessage = "Failed to delete media"
            override val i18nKey = "cooperative.error.COOPERATIVE_015"
        },
        TRANSLATION_NOT_FOUND {
            override val code = "COOPERATIVE_016"
            override val defaultMessage = "Translation not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_016"
        },
        DEFAULT_TRANSLATION_REQUIRED {
            override val code = "COOPERATIVE_017"
            override val defaultMessage = "Default locale translation is required"
            override val i18nKey = "cooperative.error.COOPERATIVE_017"
        },
        INVALID_TRANSLATION_STATUS {
            override val code = "COOPERATIVE_018"
            override val defaultMessage = "Invalid translation status transition"
            override val i18nKey = "cooperative.error.COOPERATIVE_018"
        },
        TYPE_TRANSLATION_NOT_FOUND {
            override val code = "COOPERATIVE_019"
            override val defaultMessage = "Cooperative type translation not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_019"
        },
        DUPLICATE_TRANSLATION {
            override val code = "COOPERATIVE_020"
            override val defaultMessage = "Translation for this locale already exists"
            override val i18nKey = "cooperative.error.COOPERATIVE_020"
        },
        INVALID_POLYGON_DATA {
            override val code = "COOPERATIVE_021"
            override val defaultMessage = "Invalid polygon geometry data"
            override val i18nKey = "cooperative.error.COOPERATIVE_021"
        },
        POINT_OUTSIDE_WARD {
            override val code = "COOPERATIVE_022"
            override val defaultMessage = "Cooperative location is outside the specified ward"
            override val i18nKey = "cooperative.error.COOPERATIVE_022"
        },
        WARD_NOT_FOUND {
            override val code = "COOPERATIVE_023"
            override val defaultMessage = "Ward not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_023"
        },
        GEOMETRY_OPERATION_FAILED {
            override val code = "COOPERATIVE_024"
            override val defaultMessage = "Geometry operation failed"
            override val i18nKey = "cooperative.error.COOPERATIVE_024"
        },
        PROXIMITY_SEARCH_FAILED {
            override val code = "COOPERATIVE_025"
            override val defaultMessage = "Proximity search failed"
            override val i18nKey = "cooperative.error.COOPERATIVE_025"
        },
        PERMISSION_DENIED {
            override val code = "COOPERATIVE_026"
            override val defaultMessage = "Permission denied for this operation"
            override val i18nKey = "cooperative.error.COOPERATIVE_026"
        },
        UNAUTHORIZED_MEDIA_ACCESS {
            override val code = "COOPERATIVE_027"
            override val defaultMessage = "Unauthorized access to restricted media"
            override val i18nKey = "cooperative.error.COOPERATIVE_027"
        },
        APPROVAL_REQUIRED {
            override val code = "COOPERATIVE_028"
            override val defaultMessage = "Approval is required for this operation"
            override val i18nKey = "cooperative.error.COOPERATIVE_028"
        },
        COOPERATIVE_TYPE_NOT_FOUND {
            override val code = "COOPERATIVE_029"
            override val defaultMessage = "Cooperative type not found"
            override val i18nKey = "cooperative.error.COOPERATIVE_029"
        },
        OPERATION_NOT_ALLOWED {
            override val code = "COOPERATIVE_030"
            override val defaultMessage = "Operation not allowed on this cooperative"
            override val i18nKey = "cooperative.error.COOPERATIVE_030"
        };
    }

    /**
     * Exception thrown when a cooperative cannot be found by ID or code
     */
    class CooperativeNotFoundException(identifier: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_NOT_FOUND,
        metadata = mapOf("identifier" to identifier),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when attempting to create a cooperative with an existing code
     */
    class CooperativeAlreadyExistsException(code: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_ALREADY_EXISTS,
        metadata = mapOf("code" to code),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when a cooperative is inactive
     */
    class CooperativeInactiveException(id: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_INACTIVE,
        metadata = mapOf("id" to id),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when a cooperative is pending approval
     */
    class CooperativePendingApprovalException(id: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_PENDING_APPROVAL,
        metadata = mapOf("id" to id),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when a cooperative has been dissolved
     */
    class CooperativeDisolvedException(id: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_DISSOLVED,
        metadata = mapOf("id" to id),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when cooperative data is invalid
     */
    class InvalidCooperativeDataException(message: String? = null, details: Map<String, Any> = emptyMap()) : CooperativeException(
        CooperativeErrorCode.INVALID_COOPERATIVE_DATA,
        message = message,
        metadata = details,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when a duplicate slug URL is provided
     */
    class DuplicateSlugUrlException(slugUrl: String) : CooperativeException(
        CooperativeErrorCode.DUPLICATE_SLUG_URL,
        metadata = mapOf("slugUrl" to slugUrl),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when at least one translation is required but not provided
     */
    class TranslationRequiredException : CooperativeException(
        CooperativeErrorCode.TRANSLATION_REQUIRED,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when geolocation data is invalid
     */
    class InvalidGeolocationDataException(message: String? = null) : CooperativeException(
        CooperativeErrorCode.INVALID_GEOLOCATION_DATA,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when ward data is invalid
     */
    class InvalidWardDataException(ward: Int) : CooperativeException(
        CooperativeErrorCode.INVALID_WARD_DATA,
        metadata = mapOf("ward" to ward),
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when media upload fails
     */
    class MediaUploadFailedException(message: String? = null, cause: Throwable? = null) : CooperativeException(
        CooperativeErrorCode.MEDIA_UPLOAD_FAILED,
        message = message ?: cause?.message,
        status = HttpStatus.INTERNAL_SERVER_ERROR
    )

    /**
     * Exception thrown when media is not found
     */
    class MediaNotFoundException(id: String) : CooperativeException(
        CooperativeErrorCode.MEDIA_NOT_FOUND,
        metadata = mapOf("id" to id),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when media file is too large
     */
    class MediaTooLargeException(size: Long, maxSize: Long) : CooperativeException(
        CooperativeErrorCode.MEDIA_TOO_LARGE,
        metadata = mapOf("size" to size, "maxSize" to maxSize),
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when media format is invalid
     */
    class InvalidMediaFormatException(format: String?, allowedFormats: Set<String>) : CooperativeException(
        CooperativeErrorCode.INVALID_MEDIA_FORMAT,
        metadata = mapOf(
            "format" to (format ?: "unknown"), 
            "allowedFormats" to allowedFormats.joinToString(", ")
        ),
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when media deletion fails
     */
    class MediaDeleteFailedException(key: String, cause: Throwable? = null) : CooperativeException(
        CooperativeErrorCode.MEDIA_DELETE_FAILED,
        message = cause?.message,
        metadata = mapOf("key" to key),
        status = HttpStatus.INTERNAL_SERVER_ERROR
    )

    /**
     * Exception thrown when a translation is not found
     */
    class TranslationNotFoundException(cooperativeId: String, locale: String) : CooperativeException(
        CooperativeErrorCode.TRANSLATION_NOT_FOUND,
        metadata = mapOf("cooperativeId" to cooperativeId, "locale" to locale),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when default locale translation is required but not provided
     */
    class DefaultTranslationRequiredException(defaultLocale: String) : CooperativeException(
        CooperativeErrorCode.DEFAULT_TRANSLATION_REQUIRED,
        metadata = mapOf("defaultLocale" to defaultLocale),
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when translation status transition is invalid
     */
    class InvalidTranslationStatusException(message: String? = null) : CooperativeException(
        CooperativeErrorCode.INVALID_TRANSLATION_STATUS,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when cooperative type translation is not found
     */
    class TypeTranslationNotFoundException(type: String, locale: String) : CooperativeException(
        CooperativeErrorCode.TYPE_TRANSLATION_NOT_FOUND,
        metadata = mapOf("type" to type, "locale" to locale),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when a translation for the same locale already exists
     */
    class DuplicateTranslationException(cooperativeId: String, locale: String) : CooperativeException(
        CooperativeErrorCode.DUPLICATE_TRANSLATION,
        metadata = mapOf("cooperativeId" to cooperativeId, "locale" to locale),
        status = HttpStatus.CONFLICT
    )

    /**
     * Exception thrown when polygon geometry data is invalid
     */
    class InvalidPolygonDataException(message: String? = null) : CooperativeException(
        CooperativeErrorCode.INVALID_POLYGON_DATA,
        message = message,
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when cooperative location is outside the specified ward
     */
    class PointOutsideWardException(wardNumber: Int) : CooperativeException(
        CooperativeErrorCode.POINT_OUTSIDE_WARD,
        metadata = mapOf("ward" to wardNumber),
        status = HttpStatus.BAD_REQUEST
    )

    /**
     * Exception thrown when ward is not found
     */
    class WardNotFoundException(wardNumber: Int) : CooperativeException(
        CooperativeErrorCode.WARD_NOT_FOUND,
        metadata = mapOf("ward" to wardNumber),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when a geometry operation fails
     */
    class GeometryOperationFailedException(message: String? = null, cause: Throwable? = null) : CooperativeException(
        CooperativeErrorCode.GEOMETRY_OPERATION_FAILED,
        message = message ?: cause?.message,
        status = HttpStatus.INTERNAL_SERVER_ERROR
    )

    /**
     * Exception thrown when proximity search fails
     */
    class ProximitySearchFailedException(message: String? = null, cause: Throwable? = null) : CooperativeException(
        CooperativeErrorCode.PROXIMITY_SEARCH_FAILED,
        message = message ?: cause?.message,
        status = HttpStatus.INTERNAL_SERVER_ERROR
    )

    /**
     * Exception thrown when permission is denied for an operation
     */
    class PermissionDeniedException(operation: String) : CooperativeException(
        CooperativeErrorCode.PERMISSION_DENIED,
        metadata = mapOf("operation" to operation),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when unauthorized access to restricted media is attempted
     */
    class UnauthorizedMediaAccessException(mediaId: String) : CooperativeException(
        CooperativeErrorCode.UNAUTHORIZED_MEDIA_ACCESS,
        metadata = mapOf("mediaId" to mediaId),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when approval is required for an operation
     */
    class ApprovalRequiredException(operation: String) : CooperativeException(
        CooperativeErrorCode.APPROVAL_REQUIRED,
        metadata = mapOf("operation" to operation),
        status = HttpStatus.FORBIDDEN
    )

    /**
     * Exception thrown when cooperative type is not found
     */
    class CooperativeTypeNotFoundException(type: String) : CooperativeException(
        CooperativeErrorCode.COOPERATIVE_TYPE_NOT_FOUND,
        metadata = mapOf("type" to type),
        status = HttpStatus.NOT_FOUND
    )

    /**
     * Exception thrown when an operation is not allowed on a cooperative
     */
    class OperationNotAllowedException(operation: String, cooperativeId: String, reason: String? = null) : CooperativeException(
        CooperativeErrorCode.OPERATION_NOT_ALLOWED,
        message = reason,
        metadata = mapOf("operation" to operation, "cooperativeId" to cooperativeId),
        status = HttpStatus.FORBIDDEN
    )
}
