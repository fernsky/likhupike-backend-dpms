package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.exception.CooperativeException
import np.sthaniya.dpis.profile.institutions.cooperatives.mapper.CooperativeTypeTranslationMapper
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTypeTranslation
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeTypeTranslationRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTypeTranslationService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Implementation of [CooperativeTypeTranslationService] for managing cooperative type translations.
 */
@Service
class CooperativeTypeTranslationServiceImpl(
    private val typeTranslationRepository: CooperativeTypeTranslationRepository,
    private val typeTranslationMapper: CooperativeTypeTranslationMapper
) : CooperativeTypeTranslationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates or updates a translation for a cooperative type.
     */
    @Transactional
    override fun createOrUpdateTypeTranslation(
        createDto: CooperativeTypeTranslationDto
    ): CooperativeTypeTranslationResponse {
        logger.info("Creating/updating translation for cooperative type ${createDto.cooperativeType} in locale ${createDto.locale}")
        
        // Check if translation already exists
        val existingTranslation = typeTranslationRepository.findByCooperativeTypeAndLocale(
            createDto.cooperativeType,
            createDto.locale
        )
        
        val typeTranslation = if (existingTranslation.isPresent) {
            // Update existing translation
            existingTranslation.get().apply {
                name = createDto.name
                description = createDto.description
            }
        } else {
            // Create new translation using the updated approach for the model
            // Since CooperativeTypeTranslation no longer has a secondary constructor,
            // we use the primary constructor and property initialization
            CooperativeTypeTranslation().apply {
                cooperativeType = createDto.cooperativeType
                locale = createDto.locale
                name = createDto.name
                description = createDto.description
            }
        }
        
        val savedTranslation = typeTranslationRepository.save(typeTranslation)
        logger.info("Translation saved for ${createDto.cooperativeType} in ${createDto.locale}")
        
        return typeTranslationMapper.toResponse(savedTranslation)
    }

    /**
     * Retrieves a cooperative type translation by ID.
     */
    @Transactional(readOnly = true)
    override fun getTypeTranslationById(translationId: UUID): CooperativeTypeTranslationResponse {
        logger.debug("Fetching translation with ID: $translationId")
        
        val translation = typeTranslationRepository.findById(translationId).orElseThrow {
            logger.warn("Translation not found with ID: $translationId")
            CooperativeException.TypeTranslationNotFoundException("id", "unknown")
        }
        
        return typeTranslationMapper.toResponse(translation)
    }

    /**
     * Retrieves a translation for a cooperative type and locale.
     */
    @Transactional(readOnly = true)
    override fun getTypeTranslationByTypeAndLocale(
        cooperativeType: CooperativeType,
        locale: String
    ): CooperativeTypeTranslationResponse {
        logger.debug("Fetching translation for type $cooperativeType in locale $locale")
        
        val translation = typeTranslationRepository.findByCooperativeTypeAndLocale(cooperativeType, locale)
            .orElseThrow {
                logger.warn("Translation not found for type $cooperativeType in locale $locale")
                CooperativeException.TypeTranslationNotFoundException(cooperativeType.name, locale)
            }
        
        return typeTranslationMapper.toResponse(translation)
    }

    /**
     * Retrieves all translations for a cooperative type.
     */
    @Transactional(readOnly = true)
    override fun getAllTranslationsForType(cooperativeType: CooperativeType): List<CooperativeTypeTranslationResponse> {
        logger.debug("Fetching all translations for type: $cooperativeType")
        
        val translations = typeTranslationRepository.findByCooperativeType(cooperativeType)
        return translations.map { typeTranslationMapper.toResponse(it) }
    }

    /**
     * Retrieves all translations for a specific locale with pagination.
     */
    @Transactional(readOnly = true)
    override fun getTypeTranslationsByLocale(locale: String, pageable: Pageable): Page<CooperativeTypeTranslationResponse> {
        logger.debug("Fetching translations for locale: $locale")
        
        return typeTranslationRepository.findByLocale(locale, pageable)
            .map { typeTranslationMapper.toResponse(it) }
    }

    /**
     * Deletes a translation for a cooperative type and locale.
     */
    @Transactional
    override fun deleteTypeTranslation(cooperativeType: CooperativeType, locale: String) {
        logger.info("Deleting translation for type $cooperativeType in locale $locale")
        
        if (!typeTranslationRepository.existsByCooperativeTypeAndLocale(cooperativeType, locale)) {
            logger.warn("Translation not found for deletion, type: $cooperativeType, locale: $locale")
            throw CooperativeException.TypeTranslationNotFoundException(cooperativeType.name, locale)
        }
        
        val rowsDeleted = typeTranslationRepository.deleteByCooperativeTypeAndLocale(cooperativeType, locale)
        logger.info("Deleted $rowsDeleted translation(s) for type $cooperativeType in locale $locale")
    }

    /**
     * Gets all available cooperative types with their translations for a specific locale.
     */
    @Transactional(readOnly = true)
    override fun getAllTypeTranslationsForLocale(locale: String): Map<CooperativeType, CooperativeTypeTranslationResponse> {
        logger.debug("Fetching all type translations for locale: $locale")
        
        val result = mutableMapOf<CooperativeType, CooperativeTypeTranslationResponse>()
        
        // Get all translations for the specified locale
        val translations = typeTranslationRepository.findByLocale(locale, Pageable.unpaged())
        
        // Map each translation to its type
        translations.forEach { translation ->
            translation.cooperativeType?.let { type ->
                result[type] = typeTranslationMapper.toResponse(translation)
            }
        }
        
        // Create fallback translations for missing types
        CooperativeType.values().forEach { type ->
            if (!result.containsKey(type)) {
                // Use the enum name as a fallback display name
                result[type] = CooperativeTypeTranslationResponse(
                    id = UUID.randomUUID(),  // Temporary ID for the fallback
                    cooperativeType = type,
                    locale = locale,
                    name = type.name.replace("_", " ").lowercase()
                        .split(" ")
                        .joinToString(" ") { it.capitalize() },
                    description = null
                )
            }
        }
        
        return result
    }
}
