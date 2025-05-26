package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.CreateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.UpdateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.WardWiseTrainedPopulationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.TrainedPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardWiseTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.mapper.WardWiseTrainedPopulationMapper
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.model.WardWiseTrainedPopulation
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.repository.WardWiseTrainedPopulationRepository
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.service.WardWiseTrainedPopulationService
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseTrainedPopulationService]. */
@Service
class WardWiseTrainedPopulationServiceImpl(
        private val repository: WardWiseTrainedPopulationRepository,
        private val mapper: WardWiseTrainedPopulationMapper,
        private val securityService: SecurityService
) : WardWiseTrainedPopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise trained population data. */
    @Transactional
    override fun createWardWiseTrainedPopulation(
            createDto: CreateWardWiseTrainedPopulationDto
    ): WardWiseTrainedPopulationResponse {
        logger.info("Creating ward-wise trained population data for ward: ${createDto.wardNumber}")

        // Check if data already exists for this ward
        if (repository.existsByWardNumber(createDto.wardNumber)) {
            val msg = "Data already exists for ward ${createDto.wardNumber}"
            logger.warn(msg)
            throw EconomicsException.EconomicDataAlreadyExistsException(msg)
        }

        // Create and save entity
        val entity =
                mapper.toEntity(createDto).apply {
                    createdBy = securityService.getCurrentUser().id
                    updatedBy = createdBy
                }

        val savedEntity = repository.save(entity)
        logger.info("Created ward-wise trained population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise trained population data. */
    @Transactional
    override fun updateWardWiseTrainedPopulation(
            id: UUID,
            updateDto: UpdateWardWiseTrainedPopulationDto
    ): WardWiseTrainedPopulationResponse {
        logger.info("Updating ward-wise trained population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a ward number that already exists (if changing ward)
        if (entity.wardNumber != updateDto.wardNumber &&
                repository.existsByWardNumber(updateDto.wardNumber)
        ) {
            val msg = "Data already exists for ward ${updateDto.wardNumber}"
            logger.warn(msg)
            throw EconomicsException.EconomicDataAlreadyExistsException(msg)
        }

        // Update entity
        val updatedEntity =
                mapper.updateEntity(entity, updateDto).apply {
                    updatedBy = securityService.getCurrentUser().id
                    updatedAt = Instant.now()
                }

        val savedEntity = repository.save(updatedEntity)
        logger.info("Updated ward-wise trained population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise trained population data. */
    @Transactional
    override fun deleteWardWiseTrainedPopulation(id: UUID) {
        logger.info("Deleting ward-wise trained population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise trained population data with ID: $id")
    }

    /** Get ward-wise trained population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseTrainedPopulationById(id: UUID): WardWiseTrainedPopulationResponse {
        logger.debug("Getting ward-wise trained population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise trained population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseTrainedPopulation(
            filter: WardWiseTrainedPopulationFilterDto?
    ): List<WardWiseTrainedPopulationResponse> {
        logger.debug("Getting all ward-wise trained population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get ward-wise trained population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseTrainedPopulationByWard(
            wardNumber: Int
    ): WardWiseTrainedPopulationResponse? {
        logger.debug("Getting ward-wise trained population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber)?.let { mapper.toResponse(it) }
    }

    /** Get summary of total trained population across all wards. */
    @Transactional(readOnly = true)
    override fun getTrainedPopulationSummary(): TrainedPopulationSummaryResponse {
        logger.debug("Getting trained population summary")

        val totalTrainedPopulation = repository.getTotalTrainedPopulation()
        return mapper.toTotalSummaryResponse(totalTrainedPopulation)
    }

    /** Get summary of trained population by ward. */
    @Transactional(readOnly = true)
    override fun getWardTrainedPopulationSummary(): List<WardTrainedPopulationResponse> {
        logger.debug("Getting ward trained population summary")

        return repository.getWardWiseTrainedPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseTrainedPopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise trained population data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseTrainedPopulationFilterDto
    ): Specification<WardWiseTrainedPopulation>? {
        var spec: Specification<WardWiseTrainedPopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseTrainedPopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        return spec
    }
}
