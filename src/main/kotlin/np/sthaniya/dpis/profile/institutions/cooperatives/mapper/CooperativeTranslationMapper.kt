package np.sthaniya.dpis.profile.institutions.cooperatives.mapper

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import org.springframework.stereotype.Component

/**
 * Mapper class for converting CooperativeTranslation entities to DTOs.
 */
@Component
class CooperativeTranslationMapper {

    /**
     * Converts a CooperativeTranslation entity to a CooperativeTranslationResponse DTO.
     *
     * @param translation The translation entity
     * @return The translation response DTO
     */
    fun toResponse(translation: CooperativeTranslation): CooperativeTranslationResponse {
        return CooperativeTranslationResponse(
            id = translation.id ?: throw IllegalArgumentException("Translation ID cannot be null"),
            locale = translation.locale ?: "",
            name = translation.name ?: "",
            description = translation.description,
            location = translation.location,
            services = translation.services,
            achievements = translation.achievements,
            operatingHours = translation.operatingHours,
            seoTitle = translation.seoTitle,
            seoDescription = translation.seoDescription,
            seoKeywords = translation.seoKeywords,
            slugUrl = translation.slugUrl,
            status = translation.status,
            structuredData = translation.structuredData,
            canonicalUrl = translation.canonicalUrl,
            hreflangTags = translation.hreflangTags,
            breadcrumbStructure = translation.breadcrumbStructure,
            faqItems = translation.faqItems,
            metaRobots = translation.metaRobots,
            socialShareImage = translation.socialShareImage,
            contentLastReviewed = translation.contentLastReviewed,
            version = translation.version ?: 0L
        )
    }

    /**
     * Converts a list of CooperativeTranslation entities to a list of CooperativeTranslationResponse DTOs.
     *
     * @param translations List of translation entities
     * @return List of translation response DTOs
     */
    fun toResponseList(translations: List<CooperativeTranslation>): List<CooperativeTranslationResponse> {
        return translations.map { toResponse(it) }
    }
}
