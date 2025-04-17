package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.storage.DocumentStorageService
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.MediaUploadResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.exception.CooperativeException
import np.sthaniya.dpis.profile.institutions.cooperatives.mapper.CooperativeMediaMapper
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMedia
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeMediaRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeMediaService
import np.sthaniya.dpis.profile.institutions.cooperatives.util.ImageUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.UUID
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import org.springframework.scheduling.annotation.Async

/**
 * Implementation of [CooperativeMediaService] for managing cooperative media assets.
 */
@Service
class CooperativeMediaServiceImpl(
    private val mediaRepository: CooperativeMediaRepository,
    private val cooperativeRepository: CooperativeRepository,
    private val mediaMapper: CooperativeMediaMapper,
    private val documentStorageService: DocumentStorageService,
    private val securityService: SecurityService,
    private val imageUtils: ImageUtils
) : CooperativeMediaService {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val thumbnailExecutor = Executors.newFixedThreadPool(4)

    @Value("\${app.media.max-size.image:5242880}") // 5MB default
    private val maxImageSize: Long = 5 * 1024 * 1024

    @Value("\${app.media.max-size.video:52428800}") // 50MB default
    private val maxVideoSize: Long = 50 * 1024 * 1024

    @Value("\${app.media.max-size.document:20971520}") // 20MB default
    private val maxDocumentSize: Long = 20 * 1024 * 1024

    companion object {
        const val MEDIA_FOLDER = "cooperatives/media"
        const val THUMBNAILS_FOLDER = "cooperatives/media/thumbnails"

        val ALLOWED_IMAGE_TYPES = setOf("image/jpeg", "image/png", "image/jpg", "image/webp", "image/svg+xml")
        val ALLOWED_DOCUMENT_TYPES = setOf("application/pdf", "application/msword", 
                                          "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                          "application/vnd.ms-excel", 
                                          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        val ALLOWED_VIDEO_TYPES = setOf("video/mp4", "video/webm", "video/ogg")
    }

    /**
     * Uploads a media file for a cooperative and creates its metadata.
     */
    @Transactional
    override fun uploadMedia(
        cooperativeId: UUID,
        file: MultipartFile,
        createDto: CreateCooperativeMediaDto
    ): MediaUploadResponse {
        logger.info("Uploading media for cooperative $cooperativeId of type ${createDto.type}")
        
        val cooperative = cooperativeRepository.findById(cooperativeId).orElseThrow {
            logger.warn("Cooperative not found with ID: $cooperativeId")
            CooperativeException.CooperativeNotFoundException("id: $cooperativeId")
        }
        
        // Validate file size
        val maxSize = getMaxSizeForMediaType(createDto.type)
        if (file.size > maxSize) {
            logger.warn("Media file too large: ${file.size} bytes (max: $maxSize)")
            throw CooperativeException.MediaTooLargeException(file.size, maxSize)
        }

        // Validate file format
        val contentType = file.contentType
        val allowedFormats = getAllowedFormatsForMediaType(createDto.type)
        if (contentType.isNullOrBlank() || !allowedFormats.contains(contentType)) {
            logger.warn("Invalid media format: $contentType")
            throw CooperativeException.InvalidMediaFormatException(contentType, allowedFormats)
        }
        
        try {
            // Generate a unique filename
            val fileExtension = getFileExtension(file.originalFilename)
            val baseFilename = "${UUID.randomUUID()}$fileExtension"
            val storageKey = "$MEDIA_FOLDER/${cooperative.id}/$baseFilename"
            
            // Store the file
            documentStorageService.storeDocument(
                file = file,
                folder = "$MEDIA_FOLDER/${cooperative.id}",
                fileName = baseFilename
            )
            
            // Get file URL
            val fileUrl = documentStorageService.getDocumentUrl(storageKey)
            
            // Create and save media metadata
            val media = CooperativeMedia().apply {
                this.cooperative = cooperative
                locale = createDto.locale
                type = createDto.type
                title = createDto.title
                description = createDto.description
                altText = createDto.altText ?: createDto.title
                filePath = storageKey
                this.fileUrl = fileUrl
                this.mimeType = contentType 
                fileSize = file.size
                tags = createDto.tags
                license = createDto.license
                attribution = createDto.attribution
                sortOrder = createDto.sortOrder
                isPrimary = createDto.isPrimary
                isFeatured = createDto.isFeatured
                visibilityStatus = createDto.visibilityStatus
                uploadedBy = securityService.getCurrentUser().id
                uploadedAt = Instant.now()
            }
            
            // Extract media-specific metadata
            if (contentType.startsWith("image/")) {
                extractImageMetadata(media, file)
                
                // Generate thumbnail
                val thumbnailStorageKey = generateThumbnail(file, cooperative.id ?: 
                    throw IllegalStateException("Cooperative ID cannot be null"))
                media.thumbnailUrl = documentStorageService.getDocumentUrl(thumbnailStorageKey)
            } else if (contentType.startsWith("video/")) {
                // Here we could extract video metadata like duration
                // For now just set a placeholder thumbnail
                media.thumbnailUrl = "https://via.placeholder.com/150?text=Video"
            }
            
            // If this is set as primary, make sure no other media of the same type is primary
            if (media.isPrimary) {
                cooperative.media.filter { it.type == media.type }.forEach { it.isPrimary = false }
            }
            
            // Add media to cooperative
            cooperative.addMedia(media)
            
            // Save the cooperative with the new media
            val savedCooperative = cooperativeRepository.save(cooperative)
            
            // Find the saved media
            val savedMedia = savedCooperative.media.find { 
                it.filePath == storageKey && it.type == createDto.type 
            } ?: throw CooperativeException.MediaNotFoundException("recently uploaded media")
            
            logger.info("Media uploaded successfully for cooperative $cooperativeId: ${savedMedia.id}")
            
            return MediaUploadResponse(
                id = savedMedia.id ?: throw IllegalStateException("Media ID cannot be null"),
                storageKey = storageKey,
                originalFilename = file.originalFilename ?: baseFilename,
                contentType = contentType,
                size = file.size,
                url = fileUrl,
                thumbnailUrl = savedMedia.thumbnailUrl
            )
            
        } catch (ex: Exception) {
            logger.error("Failed to upload media", ex)
            throw CooperativeException.MediaUploadFailedException("Media upload failed", ex)
        }
    }

    /**
     * Updates metadata for an existing media item.
     */
    @Transactional
    override fun updateMediaMetadata(mediaId: UUID, updateDto: UpdateCooperativeMediaDto): CooperativeMediaResponse {
        logger.info("Updating metadata for media: $mediaId")
        
        val media = getMediaEntity(mediaId)
        
        // Update fields if provided
        updateDto.locale?.let { media.locale = it }
        updateDto.type?.let { media.type = it }
        updateDto.title?.let { media.title = it }
        updateDto.description?.let { media.description = it }
        updateDto.altText?.let { media.altText = it }
        updateDto.tags?.let { media.tags = it }
        updateDto.license?.let { media.license = it }
        updateDto.attribution?.let { media.attribution = it }
        updateDto.sortOrder?.let { media.sortOrder = it }
        updateDto.visibilityStatus?.let { media.visibilityStatus = it }
        
        // Handle isPrimary status
        if (updateDto.isPrimary == true && !media.isPrimary) {
            // Set as primary and unset others
            media.isPrimary = true
            media.cooperative?.let { cooperative ->
                mediaRepository.setAsPrimary(media.id ?: throw IllegalStateException("Media ID cannot be null"))
            }
        } else if (updateDto.isPrimary == false) {
            media.isPrimary = false
        }
        
        updateDto.isFeatured?.let { media.isFeatured = it }
        
        media.updatedAt = Instant.now()
        
        val savedMedia = mediaRepository.save(media)
        logger.info("Media metadata updated: $mediaId")
        
        return mediaMapper.toResponse(savedMedia)
    }

    /**
     * Retrieves a media item by ID.
     */
    @Transactional(readOnly = true)
    override fun getMediaById(mediaId: UUID): CooperativeMediaResponse {
        logger.debug("Fetching media with ID: $mediaId")
        
        val media = getMediaEntity(mediaId)
        return mediaMapper.toResponse(media)
    }

    /**
     * Retrieves all media for a cooperative with pagination.
     */
    @Transactional(readOnly = true)
    override fun getAllMediaForCooperative(cooperativeId: UUID, pageable: Pageable): Page<CooperativeMediaResponse> {
        logger.debug("Fetching all media for cooperative: $cooperativeId")
        
        return mediaRepository.findByCooperativeId(cooperativeId, pageable)
            .map { mediaMapper.toResponse(it) }
    }

    /**
     * Retrieves media for a cooperative of a specific type with pagination.
     */
    @Transactional(readOnly = true)
    override fun getMediaByType(
        cooperativeId: UUID,
        type: CooperativeMediaType,
        pageable: Pageable
    ): Page<CooperativeMediaResponse> {
        logger.debug("Fetching media for cooperative $cooperativeId of type $type")
        
        return mediaRepository.findByCooperativeIdAndType(cooperativeId, type, pageable)
            .map { mediaMapper.toResponse(it) }
    }

    /**
     * Retrieves media for a cooperative with a specific locale with pagination.
     */
    @Transactional(readOnly = true)
    override fun getMediaByLocale(
        cooperativeId: UUID,
        locale: String,
        pageable: Pageable
    ): Page<CooperativeMediaResponse> {
        logger.debug("Fetching media for cooperative $cooperativeId in locale $locale")
        
        return mediaRepository.findByCooperativeIdAndLocale(cooperativeId, locale, pageable)
            .map { mediaMapper.toResponse(it) }
    }

    /**
     * Deletes a media item.
     */
    @Transactional
    override fun deleteMedia(mediaId: UUID) {
        logger.info("Deleting media with ID: $mediaId")
        
        val media = getMediaEntity(mediaId)
        
        try {
            // Delete the file from storage if it exists
            media.filePath?.let { filePath ->
                if (documentStorageService.documentExists(filePath)) {
                    documentStorageService.deleteDocument(filePath)
                    logger.debug("Deleted media file: $filePath")
                }
            }
            
            // Delete thumbnail if it exists
            if (media.thumbnailUrl != null) {
                val thumbnailKey = extractStorageKeyFromUrl(media.thumbnailUrl!!)
                if (thumbnailKey != null && documentStorageService.documentExists(thumbnailKey)) {
                    documentStorageService.deleteDocument(thumbnailKey)
                    logger.debug("Deleted thumbnail: $thumbnailKey")
                }
            }
            
            // Delete media entity
            mediaRepository.delete(media)
            logger.info("Media deleted: $mediaId")
            
        } catch (ex: Exception) {
            logger.error("Failed to delete media", ex)
            throw CooperativeException.MediaDeleteFailedException(
                media.filePath ?: "unknown",
                ex
            )
        }
    }

    /**
     * Sets a media item as primary for its type in its cooperative.
     */
    @Transactional
    override fun setMediaAsPrimary(mediaId: UUID): CooperativeMediaResponse {
        logger.info("Setting media $mediaId as primary")
        
        val media = getMediaEntity(mediaId)
        
        // Use non-null assertion for media ID
        val id = media.id ?: throw IllegalStateException("Media ID cannot be null")
        mediaRepository.setAsPrimary(id)
        
        // Refresh the media entity to get the updated state
        val updatedMedia = getMediaEntity(mediaId)
        logger.info("Media set as primary: $mediaId")
        
        return mediaMapper.toResponse(updatedMedia)
    }

    /**
     * Records a view for a media item.
     */
    @Transactional
    override fun recordMediaView(mediaId: UUID) {
        logger.debug("Recording view for media: $mediaId")
        
        mediaRepository.incrementViewCount(mediaId)
    }

    /**
     * Retrieves featured media items.
     */
    @Transactional(readOnly = true)
    override fun getFeaturedMedia(pageable: Pageable): Page<CooperativeMediaResponse> {
        logger.debug("Fetching featured media")
        
        return mediaRepository.findByIsFeaturedTrue(pageable)
            .map { mediaMapper.toResponse(it) }
    }

    /**
     * Searches media by tags.
     */
    @Transactional(readOnly = true)
    override fun searchMediaByTag(tag: String, pageable: Pageable): Page<CooperativeMediaResponse> {
        logger.debug("Searching media with tag: $tag")
        
        return mediaRepository.findByTag(tag, pageable)
            .map { mediaMapper.toResponse(it) }
    }
    
    /**
     * Updates the visibility status of a media item.
     */
    @Transactional
    override fun updateMediaVisibility(mediaId: UUID, status: MediaVisibilityStatus): CooperativeMediaResponse {
        logger.info("Updating visibility status of media $mediaId to $status")
        
        val media = getMediaEntity(mediaId)
        media.visibilityStatus = status
        media.updatedAt = Instant.now()
        
        val savedMedia = mediaRepository.save(media)
        logger.info("Media visibility updated: $mediaId")
        
        return mediaMapper.toResponse(savedMedia)
    }

    /**
     * Retrieves the primary media item for a cooperative of a specific type.
     */
    @Transactional(readOnly = true)
    override fun getPrimaryMedia(
        cooperativeId: UUID,
        type: CooperativeMediaType
    ): CooperativeMediaResponse? {
        logger.debug("Fetching primary media for cooperative $cooperativeId of type $type")
        
        val media = mediaRepository.findByCooperativeIdAndTypeAndIsPrimaryTrue(cooperativeId, type)
        return media.map { mediaMapper.toResponse(it) }.orElse(null)
    }

    /**
     * Retrieves all primary media items for a cooperative, organized by type.
     */
    @Transactional(readOnly = true)
    override fun getAllPrimaryMediaForCooperative(
        cooperativeId: UUID
    ): Map<CooperativeMediaType, CooperativeMediaResponse> {
        logger.debug("Fetching all primary media for cooperative: $cooperativeId")
        
        val result = mutableMapOf<CooperativeMediaType, CooperativeMediaResponse>()
        
        // For each media type, try to get the primary media
        CooperativeMediaType.values().forEach { mediaType ->
            getPrimaryMedia(cooperativeId, mediaType)?.let { 
                result[mediaType] = it
            }
        }
        
        return result
    }
    
    /**
     * Helper method to get a media entity by ID.
     */
    private fun getMediaEntity(mediaId: UUID): CooperativeMedia {
        return mediaRepository.findById(mediaId).orElseThrow {
            logger.warn("Media not found with ID: $mediaId")
            CooperativeException.MediaNotFoundException("id: $mediaId")
        }
    }
    
    /**
     * Extracts image metadata.
     */
    private fun extractImageMetadata(media: CooperativeMedia, file: MultipartFile) {
        try {
            val image = ImageIO.read(file.inputStream)
            if (image != null) {
                val width = image.width
                val height = image.height
                media.dimensions = "${width}x${height}"
            }
        } catch (ex: Exception) {
            logger.warn("Failed to extract image metadata", ex)
        }
    }
    
    /**
     * Generates a thumbnail for an image.
     */
    private fun generateThumbnail(file: MultipartFile, cooperativeId: UUID): String {
        val fileExtension = getFileExtension(file.originalFilename)
        val thumbnailFilename = "thumb_${UUID.randomUUID()}$fileExtension"
        val thumbnailStorageKey = "$THUMBNAILS_FOLDER/${cooperativeId}/$thumbnailFilename"
        
        try {
            val thumbnailBytes = imageUtils.createThumbnail(file.bytes, 300)
            val thumbnailInputStream = ByteArrayInputStream(thumbnailBytes)
            
            // FIX: Use the correct method signature for storeDocument
            // Check your DocumentStorageService implementation and use the appropriate method
            documentStorageService.storeDocument(
                file = object : MultipartFile {
                    override fun getName() = thumbnailFilename
                    override fun getOriginalFilename() = thumbnailFilename
                    override fun getContentType() = "image/jpeg" // Adjust based on your thumbnail type
                    override fun isEmpty(): Boolean = thumbnailBytes.isEmpty()
                    override fun getSize() = thumbnailBytes.size.toLong()
                    override fun getBytes() = thumbnailBytes
                    override fun getInputStream() = ByteArrayInputStream(thumbnailBytes)
                    override fun transferTo(dest: java.io.File) {
                        dest.writeBytes(thumbnailBytes)
                    }
                },
                folder = "$THUMBNAILS_FOLDER/${cooperativeId}",
                fileName = thumbnailFilename
            )
            
            return thumbnailStorageKey
        } catch (ex: Exception) {
            logger.warn("Failed to generate thumbnail, using default", ex)
            return "default-thumbnail.jpg"
        }
    }
    
    /**
     * Gets the maximum allowed size for a specific media type.
     */
    private fun getMaxSizeForMediaType(mediaType: CooperativeMediaType): Long {
        return when (mediaType) {
            CooperativeMediaType.VIDEO -> maxVideoSize
            CooperativeMediaType.DOCUMENT, 
            CooperativeMediaType.BROCHURE, 
            CooperativeMediaType.ANNUAL_REPORT -> maxDocumentSize
            else -> maxImageSize
        }
    }
    
    /**
     * Gets the allowed formats for a specific media type.
     */
    private fun getAllowedFormatsForMediaType(mediaType: CooperativeMediaType): Set<String> {
        return when (mediaType) {
            CooperativeMediaType.VIDEO -> ALLOWED_VIDEO_TYPES
            CooperativeMediaType.DOCUMENT, 
            CooperativeMediaType.BROCHURE, 
            CooperativeMediaType.ANNUAL_REPORT -> ALLOWED_DOCUMENT_TYPES
            else -> ALLOWED_IMAGE_TYPES
        }
    }
    
    /**
     * Extracts file extension from filename.
     */
    private fun getFileExtension(filename: String?): String {
        if (filename.isNullOrBlank()) return ""
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            filename.substring(lastDotIndex)
        } else {
            ""
        }
    }
    
    /**
     * Extracts storage key from a full URL.
     */
    private fun extractStorageKeyFromUrl(url: String): String? {
        // This is a simple implementation and may need to be adjusted based on how your URLs are structured
        val parts = url.split("/")
        if (parts.size < 2) return null
        
        // Attempt to find the media folder in the URL
        val mediaFolderIndex = parts.indexOf(MEDIA_FOLDER.split("/").last())
        if (mediaFolderIndex < 0) return null
        
        // Reconstruct the storage key
        return parts.subList(mediaFolderIndex - 1, parts.size).joinToString("/")
    }
}
