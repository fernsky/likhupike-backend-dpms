package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.MediaUploadResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Service for managing cooperative media assets.
 */
interface CooperativeMediaService {

    /**
     * Uploads a media file for a cooperative and creates its metadata.
     *
     * @param cooperativeId Cooperative ID
     * @param file Media file to upload
     * @param createDto DTO containing media metadata
     * @return Media upload response with file information
     */
    fun uploadMedia(
        cooperativeId: UUID, 
        file: MultipartFile, 
        createDto: CreateCooperativeMediaDto
    ): MediaUploadResponse

    /**
     * Updates metadata for an existing media item.
     *
     * @param mediaId Media ID
     * @param updateDto DTO containing updated media metadata
     * @return The updated media response
     */
    fun updateMediaMetadata(mediaId: UUID, updateDto: UpdateCooperativeMediaDto): CooperativeMediaResponse

    /**
     * Retrieves a media item by ID.
     *
     * @param mediaId Media ID
     * @return The media response
     */
    fun getMediaById(mediaId: UUID): CooperativeMediaResponse

    /**
     * Retrieves all media for a cooperative with pagination.
     *
     * @param cooperativeId Cooperative ID
     * @param pageable Pagination information
     * @return Page of media responses
     */
    fun getAllMediaForCooperative(cooperativeId: UUID, pageable: Pageable): Page<CooperativeMediaResponse>

    /**
     * Retrieves media for a cooperative of a specific type with pagination.
     *
     * @param cooperativeId Cooperative ID
     * @param type Media type
     * @param pageable Pagination information
     * @return Page of media responses
     */
    fun getMediaByType(
        cooperativeId: UUID, 
        type: CooperativeMediaType, 
        pageable: Pageable
    ): Page<CooperativeMediaResponse>

    /**
     * Retrieves media for a cooperative with a specific locale with pagination.
     *
     * @param cooperativeId Cooperative ID
     * @param locale Locale code
     * @param pageable Pagination information
     * @return Page of media responses
     */
    fun getMediaByLocale(
        cooperativeId: UUID, 
        locale: String, 
        pageable: Pageable
    ): Page<CooperativeMediaResponse>

    /**
     * Deletes a media item.
     *
     * @param mediaId Media ID
     */
    fun deleteMedia(mediaId: UUID)

    /**
     * Sets a media item as primary for its type in its cooperative.
     *
     * @param mediaId Media ID
     * @return The updated media response
     */
    fun setMediaAsPrimary(mediaId: UUID): CooperativeMediaResponse

    /**
     * Records a view for a media item.
     *
     * @param mediaId Media ID
     */
    fun recordMediaView(mediaId: UUID)

    /**
     * Retrieves featured media items.
     *
     * @param pageable Pagination information
     * @return Page of media responses
     */
    fun getFeaturedMedia(pageable: Pageable): Page<CooperativeMediaResponse>

    /**
     * Searches media by tags.
     *
     * @param tag Tag to search for
     * @param pageable Pagination information
     * @return Page of media responses
     */
    fun searchMediaByTag(tag: String, pageable: Pageable): Page<CooperativeMediaResponse>
    
    /**
     * Updates the visibility status of a media item.
     *
     * @param mediaId Media ID
     * @param status New visibility status
     * @return The updated media response
     */
    fun updateMediaVisibility(mediaId: UUID, status: MediaVisibilityStatus): CooperativeMediaResponse

    /**
     * Retrieves the primary media item for a cooperative of a specific type.
     *
     * @param cooperativeId Cooperative ID
     * @param type Media type
     * @return The media response or null if no primary media exists
     */
    fun getPrimaryMedia(
        cooperativeId: UUID, 
        type: CooperativeMediaType
    ): CooperativeMediaResponse?

    /**
     * Retrieves all primary media items for a cooperative, organized by type.
     *
     * @param cooperativeId Cooperative ID
     * @return Map of media type to media response
     */
    fun getAllPrimaryMediaForCooperative(
        cooperativeId: UUID
    ): Map<CooperativeMediaType, CooperativeMediaResponse>
}
