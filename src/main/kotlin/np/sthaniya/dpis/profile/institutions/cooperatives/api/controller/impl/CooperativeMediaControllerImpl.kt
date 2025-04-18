package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeMediaController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.MediaUploadResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeMediaService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Implementation of the CooperativeMediaController interface.
 * Provides endpoints for managing cooperative media assets.
 */
@RestController
class CooperativeMediaControllerImpl(
    private val cooperativeMediaService: CooperativeMediaService,
    private val i18nMessageService: I18nMessageService
) : CooperativeMediaController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun uploadMedia(
        cooperativeId: UUID,
        file: MultipartFile,
        createDto: CreateCooperativeMediaDto
    ): ResponseEntity<ApiResponse<MediaUploadResponse>> {
        logger.info("Uploading media for cooperative $cooperativeId of type ${createDto.type}")
        
        // Set the cooperative ID in the DTO
        val mediaDto = createDto.copy()
        
        val response = cooperativeMediaService.uploadMedia(
            cooperativeId = cooperativeId,
            file = file,
            createDto = mediaDto
        )
        
        return ResponseEntity.ok(ApiResponse.success(
            data = response, 
            message = i18nMessageService.getMessage("cooperative.media.upload.success")
        ))
    }

    override fun updateMediaMetadata(
        cooperativeId: UUID,
        mediaId: UUID,
        updateDto: UpdateCooperativeMediaDto
    ): ResponseEntity<ApiResponse<CooperativeMediaResponse>> {
        logger.info("Updating metadata for media $mediaId of cooperative $cooperativeId")
        
        val updatedMedia = cooperativeMediaService.updateMediaMetadata(mediaId, updateDto)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = updatedMedia,
            message = i18nMessageService.getMessage("cooperative.media.update.success")
        ))
    }

    override fun getMediaById(
        cooperativeId: UUID,
        mediaId: UUID
    ): ResponseEntity<ApiResponse<CooperativeMediaResponse>> {
        logger.debug("Fetching media $mediaId of cooperative $cooperativeId")
        
        val media = cooperativeMediaService.getMediaById(mediaId)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = media,
            message = i18nMessageService.getMessage("cooperative.media.get.success")
        ))
    }

    override fun getAllMediaForCooperative(
        cooperativeId: UUID,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeMediaResponse>>> {
        logger.debug("Fetching all media for cooperative $cooperativeId")
        
        val mediaPage = cooperativeMediaService.getAllMediaForCooperative(cooperativeId, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = mediaPage,
            message = i18nMessageService.getMessage("cooperative.media.list.success")
        ))
    }

    override fun getMediaByType(
        cooperativeId: UUID,
        type: CooperativeMediaType,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeMediaResponse>>> {
        logger.debug("Fetching media of type $type for cooperative $cooperativeId")
        
        val mediaPage = cooperativeMediaService.getMediaByType(cooperativeId, type, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = mediaPage,
            message = i18nMessageService.getMessage("cooperative.media.list.by.type.success", arrayOf(type))
        ))
    }

    override fun deleteMedia(
        cooperativeId: UUID,
        mediaId: UUID
    ): ResponseEntity<ApiResponse<Void>> {
        logger.info("Deleting media $mediaId from cooperative $cooperativeId")
        
        cooperativeMediaService.deleteMedia(mediaId)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = null,
            message = i18nMessageService.getMessage("cooperative.media.delete.success")
        ))
    }

    override fun setMediaAsPrimary(
        cooperativeId: UUID,
        mediaId: UUID
    ): ResponseEntity<ApiResponse<CooperativeMediaResponse>> {
        logger.info("Setting media $mediaId as primary for cooperative $cooperativeId")
        
        val media = cooperativeMediaService.setMediaAsPrimary(mediaId)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = media,
            message = i18nMessageService.getMessage("cooperative.media.set.primary.success")
        ))
    }

    override fun updateMediaVisibility(
        cooperativeId: UUID,
        mediaId: UUID,
        status: MediaVisibilityStatus
    ): ResponseEntity<ApiResponse<CooperativeMediaResponse>> {
        logger.info("Updating visibility of media $mediaId to $status")
        
        val media = cooperativeMediaService.updateMediaVisibility(mediaId, status)
        
        return ResponseEntity.ok(ApiResponse.success(
            data = media,
            message = i18nMessageService.getMessage("cooperative.media.visibility.update.success", arrayOf(status))
        ))
    }
}
