package np.sthaniya.dpis.common.storage.impl

import io.minio.*
import io.minio.http.Method
import np.sthaniya.dpis.common.storage.DocumentStorageService
import np.sthaniya.dpis.common.exception.DocumentNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Implementation of [DocumentStorageService] using MinIO as the storage backend.
 * 
 * This service handles all document storage operations including:
 * - Storing documents and images
 * - Retrieving stored files
 * - Generating access URLs
 * - Managing document lifecycle
 *
 * @property minioClient The MinIO client for S3 API operations
 * @property bucketName The S3 bucket name for document storage
 */
@Service
class DocumentStorageServiceImpl(
    private val minioClient: MinioClient,
    
    @Value("\${app.storage.bucket:dpis-documents}")
    private val bucketName: String
) : DocumentStorageService {

    /**
     * Stores a document and returns a unique storage key.
     *
     * Implementation details:
     * - Generates a UUID-based path component for uniqueness
     * - Preserves original file extension if available
     * - Uses folder hierarchy for organizing documents by type
     *
     * @param file The multipart file to store
     * @param folder The storage folder/prefix (e.g., "citizens/photos")
     * @param fileName Optional custom file name (UUID-based name used if null)
     * @return The storage key that can be used to retrieve the document
     */
    override fun storeDocument(file: MultipartFile, folder: String, fileName: String?): String {
        // Generate a unique filename if one isn't provided
        val uniqueFileName = fileName ?: "${UUID.randomUUID()}-${file.originalFilename}"
        
        // Combine folder and filename to create the full object key
        val objectName = "$folder/$uniqueFileName"

        // Use MinIO client to store the file
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )

        return objectName
    }

    /**
     * Retrieves a document as an input stream.
     *
     * @param storageKey The storage key for the document
     * @return InputStream for reading the document data
     * @throws DocumentNotFoundException if the document doesn't exist
     */
    override fun getDocument(storageKey: String): InputStream {
        return try {
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(storageKey)
                    .build()
            )
        } catch (e: Exception) {
            throw DocumentNotFoundException(
                "Document not found with key: $storageKey", 
                e,
                mapOf("storageKey" to storageKey, "bucket" to bucketName)
            )
        }
    }

    /**
     * Deletes a document from storage.
     *
     * @param storageKey The storage key for the document to delete
     * @return true if the document was deleted, false if it didn't exist
     */
    override fun deleteDocument(storageKey: String): Boolean {
        return try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(storageKey)
                    .build()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a document exists in storage.
     *
     * @param storageKey The storage key to check
     * @return true if the document exists, false otherwise
     */
    override fun documentExists(storageKey: String): Boolean {
        return try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(storageKey)
                    .build()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Gets the URL for a document.
     * 
     * This URL can be used for direct access or temporary pre-signed access
     * depending on the implementation.
     *
     * @param storageKey The storage key for the document
     * @param expiryTimeInMinutes For pre-signed URLs, how long the URL should be valid (default: 60 minutes)
     * @return URL string for accessing the document
     */
    override fun getDocumentUrl(storageKey: String, expiryTimeInMinutes: Int): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .`object`(storageKey)
                .method(Method.GET)
                .expiry(expiryTimeInMinutes, TimeUnit.MINUTES)
                .build()
        )
    }
}


