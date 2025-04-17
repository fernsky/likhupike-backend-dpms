package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeTypeTranslationController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTypeTranslationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the CooperativeTypeTranslationController interface.
 * Provides endpoints for managing cooperative type translations.
 */
@RestController
class CooperativeTypeTranslationControllerImpl(
    private val cooperativeTypeTranslationService: CooperativeTypeTranslationService
) : CooperativeTypeTranslationController {

    override fun createOrUpdateTypeTranslation(
        createDto: CooperativeTypeTranslationDto
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        val translation = cooperativeTypeTranslationService.createOrUpdateTypeTranslation(createDto)
        return ResponseEntity.ok(ApiResponse.success(translation))
    }

    override fun getTypeTranslationById(
        translationId: UUID
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        val translation = cooperativeTypeTranslationService.getTypeTranslationById(translationId)
        return ResponseEntity.ok(ApiResponse.success(translation))
    }

    override fun getTypeTranslationByTypeAndLocale(
        type: CooperativeType,
        locale: String
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        val translation = cooperativeTypeTranslationService.getTypeTranslationByTypeAndLocale(type, locale)
        return ResponseEntity.ok(ApiResponse.success(translation))
    }

    override fun getAllTranslationsForType(
        type: CooperativeType
    ): ResponseEntity<ApiResponse<List<CooperativeTypeTranslationResponse>>> {
        val translations = cooperativeTypeTranslationService.getAllTranslationsForType(type)
        return ResponseEntity.ok(ApiResponse.success(translations))
    }

    override fun getTypeTranslationsByLocale(
        locale: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeTypeTranslationResponse>>> {
        val translations = cooperativeTypeTranslationService.getTypeTranslationsByLocale(locale, pageable)
        return ResponseEntity.ok(ApiResponse.success(translations))
    }

    override fun deleteTypeTranslation(
        type: CooperativeType,
        locale: String
    ): ResponseEntity<ApiResponse<Void>> {
        cooperativeTypeTranslationService.deleteTypeTranslation(type, locale)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    override fun getAllTypeTranslationsForLocale(
        locale: String
    ): ResponseEntity<ApiResponse<Map<CooperativeType, CooperativeTypeTranslationResponse>>> {
        val translations = cooperativeTypeTranslationService.getAllTypeTranslationsForLocale(locale)
        return ResponseEntity.ok(ApiResponse.success(translations))
    }
}
