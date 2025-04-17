package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeTranslationController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTranslationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the CooperativeTranslationController interface.
 * Provides endpoints for managing cooperative translations.
 */
@RestController
class CooperativeTranslationControllerImpl(
    private val cooperativeTranslationService: CooperativeTranslationService
) : CooperativeTranslationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createTranslation(
        cooperativeId: UUID,
        createDto: CreateCooperativeTranslationDto
    ): ResponseEntity<ApiResponse<CooperativeTranslationResponse>> {
        logger.info("Creating translation for cooperative $cooperativeId in locale ${createDto.locale}")
        
        val translation = cooperativeTranslationService.createTranslation(cooperativeId, createDto)
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(translation, "Translation created successfully"))
    }

    override fun updateTranslation(
        cooperativeId: UUID,
        translationId: UUID,
        updateDto: UpdateCooperativeTranslationDto
    ): ResponseEntity<ApiResponse<CooperativeTranslationResponse>> {
        logger.info("Updating translation $translationId for cooperative $cooperativeId")
        
        // Fixed method call to match service interface
        val translation = cooperativeTranslationService.updateTranslation(
            translationId, 
            updateDto
        )
        
        return ResponseEntity.ok(ApiResponse.success(translation, "Translation updated successfully"))
    }

    override fun getTranslationById(
        cooperativeId: UUID,
        translationId: UUID
    ): ResponseEntity<ApiResponse<CooperativeTranslationResponse>> {
        logger.debug("Fetching translation $translationId for cooperative $cooperativeId")
        
        val translation = cooperativeTranslationService.getTranslationById(translationId)
        
        return ResponseEntity.ok(ApiResponse.success(translation))
    }

    override fun getTranslationByLocale(
        cooperativeId: UUID,
        locale: String
    ): ResponseEntity<ApiResponse<CooperativeTranslationResponse>> {
        logger.debug("Fetching translation in locale $locale for cooperative $cooperativeId")
        
        // Using getTranslationByCooperativeAndLocale directly to avoid any confusion
        val translation = cooperativeTranslationService.getTranslationByCooperativeAndLocale(cooperativeId, locale)
        
        return ResponseEntity.ok(ApiResponse.success(translation))
    }

    override fun getAllTranslations(
        cooperativeId: UUID
    ): ResponseEntity<ApiResponse<List<CooperativeTranslationResponse>>> {
        logger.debug("Fetching all translations for cooperative $cooperativeId")
        
        val translations = cooperativeTranslationService.getAllTranslationsForCooperative(cooperativeId)
        
        return ResponseEntity.ok(ApiResponse.success(translations))
    }

    override fun deleteTranslation(
        cooperativeId: UUID,
        translationId: UUID
    ): ResponseEntity<ApiResponse<Void>> {
        logger.info("Deleting translation $translationId from cooperative $cooperativeId")
        
        // Fixed method call to match service interface
        cooperativeTranslationService.deleteTranslation(translationId)
        
        return ResponseEntity.ok(ApiResponse.success(null, "Translation deleted successfully"))
    }

    override fun updateTranslationStatus(
        cooperativeId: UUID,
        translationId: UUID,
        status: ContentStatus
    ): ResponseEntity<ApiResponse<CooperativeTranslationResponse>> {
        logger.info("Updating status of translation $translationId to $status")
        
        // Fixed method call to use correct service method
        val translation = cooperativeTranslationService.changeTranslationStatus(
            translationId, 
            status
        )
        
        return ResponseEntity.ok(ApiResponse.success(translation, "Translation status updated successfully"))
    }
}
