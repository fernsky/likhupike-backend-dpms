package np.sthaniya.dpis.profile.demographics.demographicSummary.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.demographicSummary.mapper.DemographicSummaryMapper
import np.sthaniya.dpis.profile.demographics.demographicSummary.model.DemographicSummary
import np.sthaniya.dpis.profile.demographics.demographicSummary.repository.DemographicSummaryRepository
import np.sthaniya.dpis.profile.demographics.demographicSummary.service.DemographicSummaryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of [DemographicSummaryService].
 */
@Service
class DemographicSummaryServiceImpl(
    private val repository: DemographicSummaryRepository,
    private val mapper: DemographicSummaryMapper,
    private val securityService: SecurityService
) : DemographicSummaryService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Get the demographic summary.
     */
    @Transactional(readOnly = true)
    override fun getDemographicSummary(): DemographicSummaryResponse {
        logger.debug("Getting demographic summary")

        val entity = repository.findFirstBy()
            ?: throw DemographicsException.DemographicDataNotFoundException("demographic-summary")

        return mapper.toResponse(entity)
    }

    /**
     * Update the demographic summary with new data.
     */
    @Transactional
    override fun updateDemographicSummary(updateDto: UpdateDemographicSummaryDto): DemographicSummaryResponse {
        logger.info("Updating demographic summary")

        // Get existing entity or create a new one
        val entity = repository.findFirstBy() ?: DemographicSummary()

        // Get current user for audit
        val currentUserId = securityService.getCurrentUser().id

        // Update the entity with DTO values
        val updatedEntity = mapper.updateEntity(entity, updateDto).apply {
            if (id == null) {
                // New entity - set created by
                id = UUID.randomUUID()
                createdBy = currentUserId
                createdAt = Instant.now()
            }
            // Set updated by fields
            updatedBy = currentUserId
            updatedAt = Instant.now()
        }

        val savedEntity = repository.save(updatedEntity)
        logger.info("Updated demographic summary with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /**
     * Update a single field in the demographic summary.
     */
    @Transactional
    override fun updateSingleField(field: String, value: Any?): DemographicSummaryResponse {
        logger.info("Updating demographic summary field: $field")

        // Validate field name
        val validFields = listOf(
            "totalPopulation", "populationMale", "populationFemale", "populationOther",
            "populationAbsenteeTotal", "populationMaleAbsentee", "populationFemaleAbsentee",
            "populationOtherAbsentee", "sexRatio", "totalHouseholds", "averageHouseholdSize",
            "populationDensity", "population0To14", "population15To59", "population60AndAbove",
            "growthRate", "literacyRateAbove15", "literacyRate15To24"
        )

        if (!validFields.contains(field)) {
            val message = "Invalid field name: $field"
            logger.warn(message)
            throw DemographicsException.InvalidDemographicDataFieldException(message)
        }

        // Get existing entity or create a new one
        val entity = repository.findFirstBy() ?: DemographicSummary()

        // Get current user for audit
        val currentUserId = securityService.getCurrentUser().id

        // Update the single field in the entity
        val updatedEntity = mapper.updateSingleField(entity, field, value).apply {
            if (id == null) {
                // New entity - set created by
                id = UUID.randomUUID()
                createdBy = currentUserId
                createdAt = Instant.now()
            }
            // Set updated by fields
            updatedBy = currentUserId
            updatedAt = Instant.now()
        }

        val savedEntity = repository.save(updatedEntity)
        logger.info("Updated demographic summary field $field with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }
}
