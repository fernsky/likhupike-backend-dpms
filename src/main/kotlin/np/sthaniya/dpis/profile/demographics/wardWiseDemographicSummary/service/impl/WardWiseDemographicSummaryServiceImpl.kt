package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.CreateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.UpdateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.WardWiseDemographicSummaryFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response.WardWiseDemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.mapper.WardWiseDemographicSummaryMapper
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.model.WardWiseDemographicSummary
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.repository.WardWiseDemographicSummaryRepository
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.service.WardWiseDemographicSummaryService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseDemographicSummaryService]. */
@Service
class WardWiseDemographicSummaryServiceImpl(
        private val repository: WardWiseDemographicSummaryRepository,
        private val mapper: WardWiseDemographicSummaryMapper,
        private val securityService: SecurityService
) : WardWiseDemographicSummaryService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise demographic summary data. */
    @Transactional
    override fun createWardWiseDemographicSummary(
            createDto: CreateWardWiseDemographicSummaryDto
    ): WardWiseDemographicSummaryResponse {
        logger.info(
                "Creating ward-wise demographic summary data for ward: ${createDto.wardNumber}"
        )

        // Check if data already exists for this ward
        if (repository.existsByWardNumber(createDto.wardNumber)) {
            val msg = "Data already exists for ward ${createDto.wardNumber}"
            logger.warn(msg)
            throw DemographicsException.DemographicDataAlreadyExistsException(msg)
        }

        // Create and save entity
        val entity =
                mapper.toEntity(createDto).apply {
                    createdBy = securityService.getCurrentUser().id
                    updatedBy = createdBy
                }

        val savedEntity = repository.save(entity)
        logger.info("Created ward-wise demographic summary data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise demographic summary data. */
    @Transactional
    override fun updateWardWiseDemographicSummary(
            id: UUID,
            updateDto: UpdateWardWiseDemographicSummaryDto
    ): WardWiseDemographicSummaryResponse {
        logger.info("Updating ward-wise demographic summary data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a ward that already exists (if changing ward number)
        if (entity.wardNumber != updateDto.wardNumber &&
                repository.existsByWardNumber(updateDto.wardNumber)
        ) {
            val msg = "Data already exists for ward ${updateDto.wardNumber}"
            logger.warn(msg)
            throw DemographicsException.DemographicDataAlreadyExistsException(msg)
        }

        // Update entity
        val updatedEntity =
                mapper.updateEntity(entity, updateDto).apply {
                    updatedBy = securityService.getCurrentUser().id
                    updatedAt = Instant.now()
                }

        val savedEntity = repository.save(updatedEntity)
        logger.info("Updated ward-wise demographic summary data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise demographic summary data. */
    @Transactional
    override fun deleteWardWiseDemographicSummary(id: UUID) {
        logger.info("Deleting ward-wise demographic summary data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise demographic summary data with ID: $id")
    }

    /** Get ward-wise demographic summary data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseDemographicSummaryById(id: UUID): WardWiseDemographicSummaryResponse {
        logger.debug("Getting ward-wise demographic summary data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise demographic summary data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseDemographicSummary(
            filter: WardWiseDemographicSummaryFilterDto?
    ): List<WardWiseDemographicSummaryResponse> {
        logger.debug("Getting all ward-wise demographic summary data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get ward-wise demographic summary data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseDemographicSummaryByWard(
            wardNumber: Int
    ): WardWiseDemographicSummaryResponse? {
        logger.debug("Getting ward-wise demographic summary data for ward: $wardNumber")

        val entity = repository.findByWardNumber(wardNumber)
        return entity?.let { mapper.toResponse(it) }
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseDemographicSummary {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise demographic summary data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseDemographicSummaryFilterDto
    ): Specification<WardWiseDemographicSummary>? {
        var spec: Specification<WardWiseDemographicSummary>? = null

        filter.wardNumber?.let { ward ->
            spec = Specification<WardWiseDemographicSummary> { root, _, cb ->
                cb.equal(root.get<Int>("wardNumber"), ward)
            }
        }

        return spec
    }
}
