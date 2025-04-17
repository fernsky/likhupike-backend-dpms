package np.sthaniya.dpis.profile.institutions.cooperatives.repository

import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTypeTranslation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * Repository interface for managing [CooperativeTypeTranslation] entities.
 */
@Repository
interface CooperativeTypeTranslationRepository : JpaRepository<CooperativeTypeTranslation, UUID> {

    /**
     * Find a cooperative type translation by type and locale.
     *
     * @param cooperativeType The cooperative type
     * @param locale The locale code
     * @return Optional containing the translation if found
     */
    fun findByCooperativeTypeAndLocale(cooperativeType: CooperativeType, locale: String): Optional<CooperativeTypeTranslation>

    /**
     * Find all translations for a specific cooperative type.
     *
     * @param cooperativeType The cooperative type
     * @return List of translations for the cooperative type
     */
    fun findByCooperativeType(cooperativeType: CooperativeType): List<CooperativeTypeTranslation>

    /**
     * Find all translations for a specific locale.
     *
     * @param locale The locale code
     * @param pageable Pagination information
     * @return A page of translations for the locale
     */
    fun findByLocale(locale: String, pageable: Pageable): Page<CooperativeTypeTranslation>
    
    /**
     * Delete translation by cooperative type and locale.
     *
     * @param cooperativeType The cooperative type
     * @param locale The locale code
     * @return Number of rows affected
     */
    fun deleteByCooperativeTypeAndLocale(cooperativeType: CooperativeType, locale: String): Int
    
    /**
     * Check if a translation exists for a cooperative type and locale.
     *
     * @param cooperativeType The cooperative type
     * @param locale The locale code
     * @return true if a translation exists
     */
    fun existsByCooperativeTypeAndLocale(cooperativeType: CooperativeType, locale: String): Boolean
}
