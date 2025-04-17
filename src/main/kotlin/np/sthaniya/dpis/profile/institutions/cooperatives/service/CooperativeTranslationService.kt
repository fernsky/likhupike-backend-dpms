package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

/**
 * Service for managing cooperative translations.
 */
interface CooperativeTranslationService {

    /**
     * Creates a new translation for a cooperative.
     *
     * @param cooperativeId ID of the cooperative
     * @param createDto DTO containing translation data
     * @return The created translation
     */
    fun createTranslation(
        cooperativeId: UUID,
        createDto: CreateCooperativeTranslationDto
    ): CooperativeTranslationResponse

    /**
     * Updates an existing translation.
     *
     * @param translationId ID of the translation to update
     * @param updateDto DTO containing translation data to update
     * @return The updated translation
     */
    fun updateTranslation(
        translationId: UUID,
        updateDto: UpdateCooperativeTranslationDto
    ): CooperativeTranslationResponse

    /**
     * Retrieves a translation by ID.
     *
     * @param translationId ID of the translation to retrieve
     * @return The translation
     */
    fun getTranslationById(translationId: UUID): CooperativeTranslationResponse

    /**
     * Retrieves a translation for a cooperative and locale.
     * 
     * @param cooperativeId ID of the cooperative
     * @param locale Locale code
     * @return The translation for the cooperative in the specified locale
     */
    fun getTranslationByCooperativeAndLocale(
        cooperativeId: UUID, 
        locale: String
    ): CooperativeTranslationResponse
    
    /**
     * Retrieves a translation for a cooperative and locale (alias for getTranslationByCooperativeAndLocale).
     *
     * @param cooperativeId ID of the cooperative
     * @param locale Locale code
     * @return The translation for the cooperative in the specified locale
     */
    fun getTranslationForCooperativeAndLocale(
        cooperativeId: UUID,
        locale: String
    ): CooperativeTranslationResponse = getTranslationByCooperativeAndLocale(cooperativeId, locale)

    /**
     * Retrieves all translations for a cooperative.
     *
     * @param cooperativeId ID of the cooperative
     * @return List of translations for the cooperative
     */
    fun getAllTranslationsForCooperative(cooperativeId: UUID): List<CooperativeTranslationResponse>

    /**
     * Deletes a translation.
     *
     * @param translationId ID of the translation to delete
     */
    fun deleteTranslation(translationId: UUID)

    /**
     * Changes the status of a translation.
     *
     * @param translationId ID of the translation
     * @param status New status
     * @return The updated translation
     */
    fun changeTranslationStatus(
        translationId: UUID,
        status: ContentStatus
    ): CooperativeTranslationResponse

    /**
     * Searches translations by content.
     *
     * @param query Search query
     * @param pageable Pagination information
     * @return Page of translations matching the query
     */
    fun searchTranslations(query: String, pageable: Pageable): Page<CooperativeTranslationResponse>

    /**
     * Retrieves a translation by slug URL.
     *
     * @param slugUrl Slug URL
     * @return The translation
     */
    fun getTranslationBySlugUrl(slugUrl: String): CooperativeTranslationResponse

    /**
     * Marks a translation as reviewed.
     *
     * @param translationId ID of the translation
     * @return The updated translation
     */
    fun markTranslationAsReviewed(translationId: UUID): CooperativeTranslationResponse

    /**
     * Finds translations that need review.
     *
     * @param pageable Pagination information
     * @return Page of translations needing review
     */
    fun findTranslationsNeedingReview(pageable: Pageable): Page<CooperativeTranslationResponse>
}
