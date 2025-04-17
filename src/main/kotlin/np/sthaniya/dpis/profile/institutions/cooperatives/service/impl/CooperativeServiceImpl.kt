package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.exception.CooperativeException
import np.sthaniya.dpis.profile.institutions.cooperatives.mapper.CooperativeMapper
import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeTypeCount
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeWardCount
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeMediaService
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTranslationService
import np.sthaniya.dpis.profile.institutions.cooperatives.util.GeoUtils
import org.locationtech.jts.geom.Point
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

/**
 * Implementation of [CooperativeService] for managing cooperatives.
 */
@Service
class CooperativeServiceImpl(
    private val cooperativeRepository: CooperativeRepository,
    private val cooperativeMapper: CooperativeMapper,
    private val cooperativeMediaService: CooperativeMediaService,
    private val translationService: CooperativeTranslationService,
    private val securityService: SecurityService,
    private val geoUtils: GeoUtils
) : CooperativeService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates a new cooperative with initial translation.
     */
    @Transactional
    override fun createCooperative(createDto: CreateCooperativeDto): CooperativeResponse {
        logger.info("Creating new cooperative with code: ${createDto.code}")
        
        // Check if a cooperative with the same code already exists
        if (cooperativeRepository.existsByCode(createDto.code)) {
            logger.warn("Cooperative with code ${createDto.code} already exists")
            throw CooperativeException.CooperativeAlreadyExistsException(createDto.code)
        }

        // Create the cooperative entity
        val cooperative = Cooperative().apply {
            code = createDto.code
            defaultLocale = createDto.defaultLocale
            establishedDate = createDto.establishedDate
            ward = createDto.ward
            type = createDto.type
            status = createDto.status
            registrationNumber = createDto.registrationNumber
            contactEmail = createDto.contactEmail
            contactPhone = createDto.contactPhone
            websiteUrl = createDto.websiteUrl
            
            // Set point if provided
            createDto.point?.let {
                try {
                    point = geoUtils.createPoint(it.longitude, it.latitude)
                } catch (ex: Exception) {
                    logger.error("Failed to create point geometry", ex)
                    throw CooperativeException.InvalidGeolocationDataException("Invalid point coordinates")
                }
            }
            
            createdBy = securityService.getCurrentUserId()
            updatedBy = createdBy
        }

        // Save the cooperative
        val savedCooperative = cooperativeRepository.save(cooperative)
        logger.info("Cooperative created with ID: ${savedCooperative.id}")
        
        // Create initial translation
        val initialTranslation = createInitialTranslation(savedCooperative, createDto.translation)
        
        // Return the response with the initial translation
        return cooperativeMapper.toResponse(
            savedCooperative,
            listOf(cooperativeMapper.toTranslationResponse(initialTranslation)),
            emptyMap()
        )
    }
    
    /**
     * Helper method to create the initial translation.
     */
    private fun createInitialTranslation(
        cooperative: Cooperative,
        translationDto: CreateCooperativeTranslationDto
    ): CooperativeTranslation {
        // Create translation entity
        val translation = CooperativeTranslation().apply {
            this.cooperative = cooperative
            locale = translationDto.locale
            name = translationDto.name
            description = translationDto.description
            location = translationDto.location
            services = translationDto.services
            achievements = translationDto.achievements
            operatingHours = translationDto.operatingHours
            seoTitle = translationDto.seoTitle ?: translationDto.name
            seoDescription = translationDto.seoDescription
            seoKeywords = translationDto.seoKeywords
            slugUrl = translationDto.slugUrl ?: cooperative.code
            status = translationDto.status
            structuredData = translationDto.structuredData
            metaRobots = translationDto.metaRobots
            createdBy = securityService.getCurrentUserId()
            updatedBy = createdBy
        }
        
        cooperative.addTranslation(translation)
        return translation
    }

    /**
     * Updates an existing cooperative.
     */
    @Transactional
    override fun updateCooperative(id: UUID, updateDto: UpdateCooperativeDto): CooperativeResponse {
        logger.info("Updating cooperative with ID: $id")
        
        val cooperative = getCooperativeEntity(id)
        
        // Check if code is being changed and if the new code already exists
        if (updateDto.code != null && updateDto.code != cooperative.code) {
            if (cooperativeRepository.existsByCode(updateDto.code)) {
                logger.warn("Cannot update cooperative: Code ${updateDto.code} already exists")
                throw CooperativeException.CooperativeAlreadyExistsException(updateDto.code)
            }
            cooperative.code = updateDto.code
        }

        // Update ward if provided
        updateDto.ward?.let { cooperative.ward = it }

        // Update other fields if provided
        updateDto.defaultLocale?.let { cooperative.defaultLocale = it }
        updateDto.establishedDate?.let { cooperative.establishedDate = it }
        updateDto.type?.let { cooperative.type = it }
        updateDto.status?.let { cooperative.status = it }
        updateDto.registrationNumber?.let { cooperative.registrationNumber = it }
        updateDto.contactEmail?.let { cooperative.contactEmail = it }
        updateDto.contactPhone?.let { cooperative.contactPhone = it }
        updateDto.websiteUrl?.let { cooperative.websiteUrl = it }
        
        // Update point if provided
        updateDto.point?.let {
            try {
                cooperative.point = geoUtils.createPoint(it.longitude, it.latitude)
            } catch (ex: Exception) {
                logger.error("Failed to create point geometry", ex)
                throw CooperativeException.InvalidGeolocationDataException("Invalid point coordinates")
            }
        }
        
        cooperative.updatedBy = securityService.getCurrentUserId()
        cooperative.updatedAt = Instant.now()
        
        // Save the updated cooperative
        val savedCooperative = cooperativeRepository.save(cooperative)
        logger.info("Cooperative updated: ${savedCooperative.id}")
        
        // Get translations - use non-null assertion to handle the nullable UUID
        val cooperativeId = savedCooperative.id ?: throw IllegalStateException("Saved cooperative has null ID")
        val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
        
        // Get primary media items
        val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
        
        return cooperativeMapper.toResponse(savedCooperative, translations, primaryMedia)
    }

    /**
     * Retrieves a cooperative by ID.
     */
    @Transactional(readOnly = true)
    override fun getCooperativeById(id: UUID): CooperativeResponse {
        logger.debug("Fetching cooperative with ID: $id")
        
        val cooperative = getCooperativeEntity(id)
        
        // Get the non-null cooperative ID
        val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
        
        // Get translations
        val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
        
        // Get primary media items
        val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
        
        return cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
    }

    /**
     * Retrieves a cooperative by its code.
     */
    @Transactional(readOnly = true)
    override fun getCooperativeByCode(code: String): CooperativeResponse {
        logger.debug("Fetching cooperative with code: $code")
        
        val cooperative = cooperativeRepository.findByCode(code).orElseThrow {
            logger.warn("Cooperative not found with code: $code")
            CooperativeException.CooperativeNotFoundException("code: $code")
        }
        
        // Get the non-null cooperative ID
        val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
        
        // Get translations
        val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
        
        // Get primary media items
        val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
        
        return cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
    }

    /**
     * Deletes a cooperative.
     */
    @Transactional
    override fun deleteCooperative(id: UUID) {
        logger.info("Deleting cooperative with ID: $id")
        
        val cooperative = getCooperativeEntity(id)
        cooperativeRepository.delete(cooperative)
        logger.info("Cooperative deleted: $id")
    }

    /**
     * Retrieves all cooperatives with pagination.
     */
    @Transactional(readOnly = true)
    override fun getAllCooperatives(pageable: Pageable): Page<CooperativeResponse> {
        logger.debug("Fetching all cooperatives with pagination")
        return cooperativeRepository.findAll(pageable).map { cooperative ->
            val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
            val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
            val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
            cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
        }
    }

    /**
     * Retrieves all cooperatives by status with pagination.
     */
    @Transactional(readOnly = true)
    override fun getCooperativesByStatus(status: CooperativeStatus, pageable: Pageable): Page<CooperativeResponse> {
        logger.debug("Fetching cooperatives with status: $status")
        return cooperativeRepository.findByStatus(status, pageable).map { cooperative ->
            val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
            val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
            val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
            cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
        }
    }

    /**
     * Retrieves all cooperatives by type with pagination.
     */
    @Transactional(readOnly = true)
    override fun getCooperativesByType(type: CooperativeType, pageable: Pageable): Page<CooperativeResponse> {
        logger.debug("Fetching cooperatives of type: $type")
        return cooperativeRepository.findByType(type, pageable).map { cooperative ->
            val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
            val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
            val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
            cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
        }
    }

    /**
     * Retrieves all cooperatives in a specific ward with pagination.
     */
    @Transactional(readOnly = true)
    override fun getCooperativesByWard(ward: Int, pageable: Pageable): Page<CooperativeResponse> {
        logger.debug("Fetching cooperatives in ward: $ward")
        
        return cooperativeRepository.findByWard(ward, pageable).map { cooperative ->
            val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
            val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
            val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
            cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
        }
    }

    /**
     * Searches cooperatives by name in any available translation.
     */
    @Transactional(readOnly = true)
    override fun searchCooperativesByName(nameQuery: String, pageable: Pageable): Page<CooperativeResponse> {
        logger.debug("Searching cooperatives with name query: $nameQuery")
        return cooperativeRepository.searchByName(nameQuery, pageable).map { cooperative ->
            val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
            val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
            val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
            cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
        }
    }

    /**
     * Changes the status of a cooperative.
     */
    @Transactional
    override fun changeCooperativeStatus(id: UUID, status: CooperativeStatus): CooperativeResponse {
        logger.info("Changing status of cooperative $id to $status")
        
        val cooperative = getCooperativeEntity(id)
        cooperative.status = status
        cooperative.updatedBy = securityService.getCurrentUserId()
        cooperative.updatedAt = Instant.now()
        
        val savedCooperative = cooperativeRepository.save(cooperative)
        
        // Get the non-null cooperative ID
        val cooperativeId = savedCooperative.id ?: throw IllegalStateException("Saved cooperative has null ID")
        
        // Get translations
        val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
        
        // Get primary media items
        val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
        
        return cooperativeMapper.toResponse(savedCooperative, translations, primaryMedia)
    }

    /**
     * Finds cooperatives within a specified distance of a geographic point.
     */
    @Transactional(readOnly = true)
    override fun findCooperativesNear(
        longitude: Double,
        latitude: Double,
        distanceInMeters: Double,
        pageable: Pageable
    ): Page<CooperativeResponse> {
        logger.debug("Finding cooperatives near coordinates ($longitude, $latitude) within $distanceInMeters meters")
        
        try {
            return cooperativeRepository.findWithinDistance(longitude, latitude, distanceInMeters, pageable)
                .map { cooperative ->
                    val cooperativeId = cooperative.id ?: throw IllegalStateException("Cooperative has null ID")
                    val translations = translationService.getAllTranslationsForCooperative(cooperativeId)
                    val primaryMedia = cooperativeMediaService.getAllPrimaryMediaForCooperative(cooperativeId)
                    cooperativeMapper.toResponse(cooperative, translations, primaryMedia)
                }
        } catch (ex: Exception) {
            logger.error("Error performing proximity search", ex)
            throw CooperativeException.ProximitySearchFailedException("Error during proximity search", ex)
        }
    }

    /**
     * Gets cooperative statistics by type.
     */
    @Transactional(readOnly = true)
    override fun getCooperativeStatisticsByType(): Map<CooperativeType, Long> {
        logger.debug("Getting cooperative statistics by type")
        
        val statistics = cooperativeRepository.countByType()
        return statistics.associate { it.type to it.count }
    }

    /**
     * Gets cooperative statistics by ward.
     */
    @Transactional(readOnly = true)
    override fun getCooperativeStatisticsByWard(): Map<Int, Long> {
        logger.debug("Getting cooperative statistics by ward")
        
        val statistics = cooperativeRepository.countByWard()
        return statistics.associate { it.ward to it.count }
    }
    
    /**
     * Helper method to get a cooperative entity by ID.
     */
    private fun getCooperativeEntity(id: UUID): Cooperative {
        return cooperativeRepository.findById(id).orElseThrow {
            logger.warn("Cooperative not found with ID: $id")
            CooperativeException.CooperativeNotFoundException("id: $id")
        }
    }
}
