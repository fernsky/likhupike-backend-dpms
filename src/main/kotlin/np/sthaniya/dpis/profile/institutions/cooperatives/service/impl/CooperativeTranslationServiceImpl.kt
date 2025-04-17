package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.exception.CooperativeException
import np.sthaniya.dpis.profile.institutions.cooperatives.mapper.CooperativeTranslationMapper
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeTranslationRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTranslationService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

/**
 * Implementation of [CooperativeTranslationService] for managing cooperative translations.
 */
@Service
class CooperativeTranslationServiceImpl(
    private val translationRepository: CooperativeTranslationRepository,
    private val cooperativeRepository: CooperativeRepository,
    private val translationMapper: CooperativeTranslationMapper,
    private val securityService: SecurityService
) : CooperativeTranslationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates a new translation for a cooperative.
     */
    @Transactional
    override fun createTranslation(
        cooperativeId: UUID,
        createDto: CreateCooperativeTranslationDto
    ): CooperativeTranslationResponse {
        logger.info("Creating new translation for cooperative $cooperativeId in locale ${createDto.locale}")
        
        // Get the cooperative
        val cooperative = cooperativeRepository.findById(cooperativeId).orElseThrow {
            logger.warn("Cooperative not found with ID: $cooperativeId")
            CooperativeException.CooperativeNotFoundException("id: $cooperativeId")
        }
        
        // Check for duplicate translation
        if (translationRepository.findByCooperativeIdAndLocale(cooperativeId, createDto.locale).isPresent) {
            logger.warn("Translation already exists for cooperative $cooperativeId in locale ${createDto.locale}")
            throw CooperativeException.DuplicateTranslationException(cooperativeId.toString(), createDto.locale)
        }
        
        // Check for duplicate slug URL if provided
        createDto.slugUrl?.let {
            if (translationRepository.existsBySlugUrl(it)) {
                logger.warn("Translation with slug URL $it already exists")
                throw CooperativeException.DuplicateSlugUrlException(it)
            }
        }
        
        // Create translation entity
        val translation = CooperativeTranslation().apply {
            this.cooperative = cooperative
            locale = createDto.locale
            name = createDto.name
            description = createDto.description
            location = createDto.location
            services = createDto.services
            achievements = createDto.achievements
            operatingHours = createDto.operatingHours
            seoTitle = createDto.seoTitle ?: createDto.name
            seoDescription = createDto.seoDescription
            seoKeywords = createDto.seoKeywords
            slugUrl = createDto.slugUrl ?: cooperative.code
            status = createDto.status
            structuredData = createDto.structuredData
            metaRobots = createDto.metaRobots
            createdBy = securityService.getCurrentUserId()
            updatedBy = createdBy
        }
        
        // Add translation to cooperative
        cooperative.addTranslation(translation)
        
        // Save the cooperative and its translations
        val savedCooperative = cooperativeRepository.save(cooperative)
        
        // Find the saved translation
        val savedTranslation = savedCooperative.translations.find { it.locale == createDto.locale }
            ?: throw CooperativeException.TranslationNotFoundException(cooperativeId.toString(), createDto.locale)
        
        logger.info("Translation created for cooperative $cooperativeId in locale ${createDto.locale}")
        return translationMapper.toResponse(savedTranslation)
    }

    /**
     * Updates an existing translation.
     */
    @Transactional
    override fun updateTranslation(
        translationId: UUID,
        updateDto: UpdateCooperativeTranslationDto
    ): CooperativeTranslationResponse {
        logger.info("Updating translation with ID: $translationId")
        
        val translation = getTranslationEntity(translationId)
        
        // Check for duplicate slug URL if being changed
        if (updateDto.slugUrl != null && updateDto.slugUrl != translation.slugUrl) {
            if (translationRepository.existsBySlugUrl(updateDto.slugUrl)) {
                logger.warn("Translation with slug URL ${updateDto.slugUrl} already exists")
                throw CooperativeException.DuplicateSlugUrlException(updateDto.slugUrl)
            }
        }
        
        // Update fields if provided
        updateDto.name?.let { translation.name = it }
        updateDto.description?.let { translation.description = it }
        updateDto.location?.let { translation.location = it }
        updateDto.services?.let { translation.services = it }
        updateDto.achievements?.let { translation.achievements = it }
        updateDto.operatingHours?.let { translation.operatingHours = it }
        updateDto.seoTitle?.let { translation.seoTitle = it }
        updateDto.seoDescription?.let { translation.seoDescription = it }
        updateDto.seoKeywords?.let { translation.seoKeywords = it }
        updateDto.slugUrl?.let { translation.slugUrl = it }
        updateDto.status?.let { translation.status = it }
        updateDto.structuredData?.let { translation.structuredData = it }
        updateDto.canonicalUrl?.let { translation.canonicalUrl = it }
        updateDto.hreflangTags?.let { translation.hreflangTags = it }
        updateDto.breadcrumbStructure?.let { translation.breadcrumbStructure = it }
        updateDto.faqItems?.let { translation.faqItems = it }
        updateDto.metaRobots?.let { translation.metaRobots = it }
        updateDto.socialShareImage?.let { translation.socialShareImage = it }
        
        translation.updatedBy = securityService.getCurrentUserId()
        translation.updatedAt = Instant.now()
        
        val savedTranslation = translationRepository.save(translation)
        logger.info("Translation updated: $translationId")
        
        return translationMapper.toResponse(savedTranslation)
    }

    /**
     * Retrieves a translation by ID.
     */
    @Transactional(readOnly = true)
    override fun getTranslationById(translationId: UUID): CooperativeTranslationResponse {
        logger.debug("Fetching translation with ID: $translationId")
        
        val translation = getTranslationEntity(translationId)
        return translationMapper.toResponse(translation)
    }

    /**
     * Retrieves a translation for a cooperative and locale.
     */
    @Transactional(readOnly = true)
    override fun getTranslationByCooperativeAndLocale(cooperativeId: UUID, locale: String): CooperativeTranslationResponse {
        logger.debug("Fetching translation for cooperative $cooperativeId in locale $locale")
        
        val translation = translationRepository.findByCooperativeIdAndLocale(cooperativeId, locale).orElseThrow {
            logger.warn("Translation not found for cooperative $cooperativeId in locale $locale")
            throw CooperativeException.TranslationNotFoundException(cooperativeId.toString(), locale)
        }
        
        return translationMapper.toResponse(translation)
    }

    /**
     * Retrieves all translations for a cooperative.
     */
    @Transactional(readOnly = true)
    override fun getAllTranslationsForCooperative(cooperativeId: UUID): List<CooperativeTranslationResponse> {
        logger.debug("Fetching all translations for cooperative: $cooperativeId")
        
        val translations = translationRepository.findByCooperativeId(cooperativeId)
        return translations.map { translationMapper.toResponse(it) }
    }

    /**
     * Deletes a translation.
     */
    @Transactional
    override fun deleteTranslation(translationId: UUID) {
        logger.info("Deleting translation with ID: $translationId")
        
        val translation = getTranslationEntity(translationId)
        
        // Check if this is the default locale translation
        val cooperative = translation.cooperative
        if (cooperative != null && cooperative.defaultLocale == translation.locale) {
            logger.warn("Cannot delete default locale translation")
            throw CooperativeException.DefaultTranslationRequiredException(cooperative.defaultLocale)
        }
        
        translationRepository.delete(translation)
        logger.info("Translation deleted: $translationId")
    }

    /**
     * Changes the status of a translation.
     */
    @Transactional
    override fun changeTranslationStatus(translationId: UUID, status: ContentStatus): CooperativeTranslationResponse {
        logger.info("Changing status of translation $translationId to $status")
        
        val translation = getTranslationEntity(translationId)
        
        // Check for valid status transitions
        validateStatusTransition(translation.status, status)
        
        translation.status = status
        translation.updatedBy = securityService.getCurrentUserId()
        translation.updatedAt = Instant.now()
        
        val savedTranslation = translationRepository.save(translation)
        logger.info("Translation status updated to $status for ID: $translationId")
        
        return translationMapper.toResponse(savedTranslation)
    }

    /**
     * Searches translations by content.
     */
    @Transactional(readOnly = true)
    override fun searchTranslations(query: String, pageable: Pageable): Page<CooperativeTranslationResponse> {
        logger.debug("Searching translations with query: $query")
        
        return translationRepository.search(query, pageable)
            .map { translationMapper.toResponse(it) }
    }

    /**
     * Retrieves a translation by slug URL.
     */
    @Transactional(readOnly = true)
    override fun getTranslationBySlugUrl(slugUrl: String): CooperativeTranslationResponse {
        logger.debug("Fetching translation with slug URL: $slugUrl")
        
        val translation = translationRepository.findBySlugUrl(slugUrl).orElseThrow {
            logger.warn("Translation not found with slug URL: $slugUrl")
            CooperativeException.TranslationNotFoundException("slugUrl", slugUrl)
        }
        
        return translationMapper.toResponse(translation)
    }

    /**
     * Marks a translation as reviewed.
     */
    @Transactional
    override fun markTranslationAsReviewed(translationId: UUID): CooperativeTranslationResponse {
        logger.info("Marking translation as reviewed: $translationId")
        
        val translation = getTranslationEntity(translationId)
        translation.contentLastReviewed = Instant.now()
        translation.updatedBy = securityService.getCurrentUserId()
        translation.updatedAt = Instant.now()
        
        val savedTranslation = translationRepository.save(translation)
        logger.info("Translation marked as reviewed: $translationId")
        
        return translationMapper.toResponse(savedTranslation)
    }

    /**
     * Finds translations that need review.
     */
    @Transactional(readOnly = true)
    override fun findTranslationsNeedingReview(pageable: Pageable): Page<CooperativeTranslationResponse> {
        logger.debug("Finding translations that need review")
        
        // Translations that haven't been reviewed in the last 3 months
        val reviewThreshold = Instant.now().minus(90, ChronoUnit.DAYS)
        
        return translationRepository
            .findByContentLastReviewedBeforeOrContentLastReviewedIsNull(reviewThreshold, pageable)
            .map { translationMapper.toResponse(it) }
    }
    
    /**
     * Helper method to get a translation entity by ID.
     */
    private fun getTranslationEntity(id: UUID): CooperativeTranslation {
        return translationRepository.findById(id).orElseThrow {
            logger.warn("Translation not found with ID: $id")
            CooperativeException.TranslationNotFoundException("id", id.toString())
        }
    }
    
    /**
     * Helper method to validate status transitions.
     */
    private fun validateStatusTransition(currentStatus: ContentStatus, newStatus: ContentStatus) {
        // Implement status transition validation logic
        // For example, can't move from ARCHIVED directly to PUBLISHED
        if (currentStatus == ContentStatus.ARCHIVED && newStatus == ContentStatus.PUBLISHED) {
            throw CooperativeException.InvalidTranslationStatusException(
                "Cannot move directly from ARCHIVED to PUBLISHED status"
            )
        }
    }
}
