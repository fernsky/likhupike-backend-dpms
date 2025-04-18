package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import java.util.UUID
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeTypeTranslationController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTypeTranslationService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implementation of the CooperativeTypeTranslationController interface. Provides endpoints for
 * managing cooperative type translations.
 */
@RestController
class CooperativeTypeTranslationControllerImpl(
        private val cooperativeTypeTranslationService: CooperativeTypeTranslationService,
        private val i18nMessageService: I18nMessageService
) : CooperativeTypeTranslationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createOrUpdateTypeTranslation(
            createDto: CooperativeTypeTranslationDto
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        logger.info(
                "Creating/updating translation for type ${createDto.cooperativeType} in locale ${createDto.locale}"
        )

        val translation = cooperativeTypeTranslationService.createOrUpdateTypeTranslation(createDto)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = translation,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.create-update.success"
                                )
                )
        )
    }

    override fun getTypeTranslationById(
            translationId: UUID
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        logger.debug("Fetching type translation with ID: $translationId")

        val translation = cooperativeTypeTranslationService.getTypeTranslationById(translationId)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = translation,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.get.success"
                                )
                )
        )
    }

    override fun getTypeTranslationByTypeAndLocale(
            type: CooperativeType,
            locale: String
    ): ResponseEntity<ApiResponse<CooperativeTypeTranslationResponse>> {
        logger.debug("Fetching translation for type $type in locale $locale")

        val translation =
                cooperativeTypeTranslationService.getTypeTranslationByTypeAndLocale(type, locale)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = translation,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.get.success"
                                )
                )
        )
    }

    override fun getAllTranslationsForType(
            type: CooperativeType
    ): ResponseEntity<ApiResponse<List<CooperativeTypeTranslationResponse>>> {
        logger.debug("Fetching all translations for type $type")

        val translations = cooperativeTypeTranslationService.getAllTranslationsForType(type)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = translations,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.list.success"
                                )
                )
        )
    }

    override fun getTypeTranslationsByLocale(
            locale: String,
            pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeTypeTranslationResponse>>> {
        logger.debug("Fetching translations in locale $locale")

        val translations =
                cooperativeTypeTranslationService.getTypeTranslationsByLocale(locale, pageable)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = translations,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.list.locale.success",
                                        arrayOf(locale)
                                )
                )
        )
    }

    override fun getAllTypeTranslationsForLocale(
            locale: String
    ): ResponseEntity<ApiResponse<Map<CooperativeType, CooperativeTypeTranslationResponse>>> {
        logger.debug("Fetching all type translations for locale $locale")

        val typeMap = cooperativeTypeTranslationService.getAllTypeTranslationsForLocale(locale)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = typeMap,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.map.locale.success",
                                        arrayOf(locale)
                                )
                )
        )
    }

    override fun deleteTypeTranslation(
            type: CooperativeType,
            locale: String
    ): ResponseEntity<ApiResponse<Void>> {
        logger.info("Deleting translation for type $type in locale $locale")

        cooperativeTypeTranslationService.deleteTypeTranslation(type, locale)

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = null,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.type.translation.delete.success"
                                )
                )
        )
    }
}
