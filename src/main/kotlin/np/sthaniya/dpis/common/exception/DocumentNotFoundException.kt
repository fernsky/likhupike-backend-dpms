package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus

/**
 * Exception thrown when a requested document cannot be found or accessed.
 * 
 * This exception is typically thrown by document storage services when
 * attempting to retrieve a document that doesn't exist or is inaccessible.
 *
 * @param message Optional custom error message
 * @param cause The underlying cause of this exception
 * @param metadata Additional error context data
 */
class DocumentNotFoundException(
    message: String? = null,
    cause: Throwable? = null,
    metadata: Map<String, Any> = emptyMap()
) : dpisException(
    errorCode = DocumentErrorCode.DOCUMENT_NOT_FOUND,
    message = message,
    status = HttpStatus.NOT_FOUND,
    metadata = metadata,
    cause = cause
) {
    /**
     * Constructor that accepts only a message
     */
    constructor(message: String) : this(message = message, cause = null, metadata = emptyMap())
    
    /**
     * Constructor that accepts a message and cause
     */
    constructor(message: String, cause: Throwable) : this(message = message, cause = cause, metadata = emptyMap())

    /**
     * Error codes for document-related exceptions
     */
    enum class DocumentErrorCode : ErrorCode {
        DOCUMENT_NOT_FOUND {
            override val code = "DOC_001"
            override val defaultMessage = "The requested document was not found"
            override val i18nKey = "error.document.not_found"
        },
        DOCUMENT_ACCESS_DENIED {
            override val code = "DOC_002"
            override val defaultMessage = "Access to the requested document was denied"
            override val i18nKey = "error.document.access_denied"
        },
        DOCUMENT_STORAGE_ERROR {
            override val code = "DOC_003"
            override val defaultMessage = "An error occurred while accessing document storage"
            override val i18nKey = "error.document.storage_error"
        }
    }
}
