package np.sthaniya.dpis.profile.institutions.cooperatives.mapper

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTypeTranslation
import org.springframework.stereotype.Component

/**
 * Mapper for converting between CooperativeTypeTranslation entities and DTOs.
 */
@Component
class CooperativeTypeTranslationMapper {

    /**
     * Convert a CooperativeTypeTranslation entity to a CooperativeTypeTranslationResponse DTO.
     *
     * @param translation The CooperativeTypeTranslation entity to convert
     * @return The CooperativeTypeTranslationResponse DTO
     */
    fun toResponse(translation: CooperativeTypeTranslation): CooperativeTypeTranslationResponse {
        return CooperativeTypeTranslationResponse(
            id = translation.id ?: throw IllegalStateException("Translation ID cannot be null"),
            cooperativeType = translation.cooperativeType ?: throw IllegalStateException("Cooperative type cannot be null"),
            locale = translation.locale ?: "",
            name = translation.name ?: "",
            description = translation.description
        )
    }
}
