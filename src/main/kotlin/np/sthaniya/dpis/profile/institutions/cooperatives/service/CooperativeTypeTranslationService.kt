package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

/**
 * Service for managing translations of cooperative types.
 */
interface CooperativeTypeTranslationService {

    /**
     * Creates or updates a translation for a cooperative type.
     *
     * @param createDto DTO containing the translation data
     * @return The created or updated translation response
     */
    fun createOrUpdateTypeTranslation(
        createDto: CooperativeTypeTranslationDto
    ): CooperativeTypeTranslationResponse

    /**
     * Retrieves a cooperative type translation by ID.
     *
     * @param translationId Translation ID
     * @return The translation response
     */
    fun getTypeTranslationById(translationId: UUID): CooperativeTypeTranslationResponse

    /**
     * Retrieves a translation for a cooperative type and locale.
     *
     * @param cooperativeType Cooperative type
     * @param locale Locale code
     * @return The translation response
     */
    fun getTypeTranslationByTypeAndLocale(
        cooperativeType: CooperativeType, 
        locale: String
    ): CooperativeTypeTranslationResponse

    /**
     * Retrieves all translations for a cooperative type.
     *
     * @param cooperativeType Cooperative type
     * @return List of translation responses
     */
    fun getAllTranslationsForType(cooperativeType: CooperativeType): List<CooperativeTypeTranslationResponse>

    /**
     * Retrieves all translations for a specific locale with pagination.
     *
     * @param locale Locale code
     * @param pageable Pagination information
     * @return Page of translation responses
     */
    fun getTypeTranslationsByLocale(locale: String, pageable: Pageable): Page<CooperativeTypeTranslationResponse>

    /**
     * Deletes a translation for a cooperative type and locale.
     *
     * @param cooperativeType Cooperative type
     * @param locale Locale code
     */
    fun deleteTypeTranslation(cooperativeType: CooperativeType, locale: String)

    /**
     * Gets all available cooperative types with their translations for a specific locale.
     *
     * @param locale Locale code
     * @return Map of cooperative type to its translation
     */
    fun getAllTypeTranslationsForLocale(locale: String): Map<CooperativeType, CooperativeTypeTranslationResponse>
}
