package np.sthaniya.dpis.profile.institutions.cooperatives.mapper

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.GeoPointResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Mapper class for converting Cooperative entities to DTOs.
 */
@Component
class CooperativeMapper {

    /**
     * Converts a Cooperative entity to a CooperativeResponse DTO.
     *
     * @param cooperative The cooperative entity
     * @param translations List of translation responses for the cooperative
     * @param primaryMedia Map of media type to media response for primary media items
     * @return The cooperative response DTO
     */
    fun toResponse(
        cooperative: Cooperative,
        translations: List<CooperativeTranslationResponse>,
        primaryMedia: Map<CooperativeMediaType, CooperativeMediaResponse>
    ): CooperativeResponse {
        return CooperativeResponse(
            id = cooperative.id ?: throw IllegalStateException("Cooperative ID cannot be null"),
            code = cooperative.code ?: "",
            defaultLocale = cooperative.defaultLocale,
            establishedDate = cooperative.establishedDate,
            ward = cooperative.ward,
            type = cooperative.type ?: throw IllegalStateException("Cooperative type cannot be null"),
            status = cooperative.status,
            registrationNumber = cooperative.registrationNumber,
            point = cooperative.point?.let { point ->
                GeoPointResponse(
                    longitude = point.x,
                    latitude = point.y
                )
            },
            contactEmail = cooperative.contactEmail,
            contactPhone = cooperative.contactPhone,
            websiteUrl = cooperative.websiteUrl,
            createdAt = cooperative.createdAt,
            updatedAt = cooperative.updatedAt,
            translations = translations,
            // Convert from CooperativeMediaType enum to string in the map keys
            primaryMedia = primaryMedia.mapKeys { it.key.name }
        )
    }

    /**
     * Converts a list of Cooperative entities to a list of CooperativeResponse DTOs.
     *
     * @param cooperatives List of cooperative entities
     * @param translationsByCooperativeId Map of cooperative ID to translations list
     * @param primaryMediaByCooperativeId Map of cooperative ID to primary media map
     * @return List of cooperative response DTOs
     */
    fun toResponseList(
        cooperatives: List<Cooperative>,
        translationsByCooperativeId: Map<UUID, List<CooperativeTranslationResponse>>,
        primaryMediaByCooperativeId: Map<UUID, Map<CooperativeMediaType, CooperativeMediaResponse>>
    ): List<CooperativeResponse> {
        return cooperatives.map { cooperative ->
            toResponse(
                cooperative = cooperative,
                translations = translationsByCooperativeId[cooperative.id] ?: emptyList(),
                primaryMedia = primaryMediaByCooperativeId[cooperative.id] ?: emptyMap()
            )
        }
    }

    /**
     * Converts a CooperativeTranslation entity to a CooperativeTranslationResponse DTO.
     *
     * @param translation The translation entity
     * @return The translation response DTO
     */
    fun toTranslationResponse(translation: CooperativeTranslation): CooperativeTranslationResponse {
        return CooperativeTranslationResponse(
            id = translation.id ?: throw IllegalStateException("Translation ID cannot be null"),
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
    fun toTranslationResponseList(translations: List<CooperativeTranslation>): List<CooperativeTranslationResponse> {
        return translations.map { toTranslationResponse(it) }
    }
}
