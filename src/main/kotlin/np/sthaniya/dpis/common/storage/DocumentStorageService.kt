package np.sthaniya.dpis.common.storage

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.UUID

/**
 * Service interface for document storage operations.
 * 
 * This interface abstracts the storage operations for citizen documents and photos,
 * allowing for flexible implementation with different storage providers like MinIO, S3, etc.
 */
interface DocumentStorageService {
    
    /**
     * Stores a document and returns a unique storage key.
     *
     * @param file The multipart file to store
     * @param folder The storage folder/prefix (e.g., "citizens/photos")
     * @param fileName Optional custom file name (UUID-based name used if null)
     * @return The storage key that can be used to retrieve the document
     */
    fun storeDocument(
        file: MultipartFile, 
        folder: String, 
        fileName: String? = null
    ): String
    
    /**
     * Retrieves a document as an input stream.
     *
     * @param storageKey The storage key for the document
     * @return InputStream for reading the document data
     * @throws DocumentNotFoundException if the document doesn't exist
     */
    fun getDocument(storageKey: String): InputStream
    
    /**
     * Deletes a document from storage.
     *
     * @param storageKey The storage key for the document to delete
     * @return true if the document was deleted, false if it didn't exist
     */
    fun deleteDocument(storageKey: String): Boolean
    
    /**
     * Checks if a document exists in storage.
     *
     * @param storageKey The storage key to check
     * @return true if the document exists, false otherwise
     */
    fun documentExists(storageKey: String): Boolean
    
    /**
     * Gets the URL for a document.
     * 
     * This URL can be used for direct access or temporary pre-signed access
     * depending on the implementation.
     *
     * @param storageKey The storage key for the document
     * @param expiryTimeInMinutes For pre-signed URLs, how long the URL should be valid
     * @return URL string for accessing the document
     */
    fun getDocumentUrl(storageKey: String, expiryTimeInMinutes: Int = 60): String
}
