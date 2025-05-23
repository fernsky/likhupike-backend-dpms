package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.CreateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.UpdateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.WardTimeSeriesPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardTimeSeriesPopulationResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.YearPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.mapper.WardTimeSeriesPopulationMapper
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.model.WardTimeSeriesPopulation
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.repository.WardTimeSeriesPopulationRepository
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.service.WardTimeSeriesPopulationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardTimeSeriesPopulationService]. */
@Service
class WardTimeSeriesPopulationServiceImpl(
        private val repository: WardTimeSeriesPopulationRepository,
        private val mapper: WardTimeSeriesPopulationMapper,
        private val securityService: SecurityService
) : WardTimeSeriesPopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward time series population data. */
    @Transactional
    override fun createWardTimeSeriesPopulation(
            createDto: CreateWardTimeSeriesPopulationDto
    ): WardTimeSeriesPopulationResponse {
        logger.info(
                "Creating ward time series population data for ward: ${createDto.wardNumber}, year: ${createDto.year}"
        )

        // Check if data already exists for this ward and year
        if (repository.existsByWardNumberAndYear(createDto.wardNumber, createDto.year)) {
            val msg = "Data already exists for ward ${createDto.wardNumber} and year ${createDto.year}"
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
        logger.info("Created ward time series population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward time series population data. */
    @Transactional
    override fun updateWardTimeSeriesPopulation(
            id: UUID,
            updateDto: UpdateWardTimeSeriesPopulationDto
    ): WardTimeSeriesPopulationResponse {
        logger.info("Updating ward time series population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or year)
        if ((entity.wardNumber != updateDto.wardNumber || entity.year != updateDto.year) &&
                repository.existsByWardNumberAndYear(updateDto.wardNumber, updateDto.year)) {
            val msg = "Data already exists for ward ${updateDto.wardNumber} and year ${updateDto.year}"
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
        logger.info("Updated ward time series population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward time series population data. */
    @Transactional
    override fun deleteWardTimeSeriesPopulation(id: UUID) {
        logger.info("Deleting ward time series population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward time series population data with ID: $id")
    }

    /** Get ward time series population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardTimeSeriesPopulationById(id: UUID): WardTimeSeriesPopulationResponse {
        logger.debug("Getting ward time series population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward time series population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardTimeSeriesPopulation(
            filter: WardTimeSeriesPopulationFilterDto?
    ): List<WardTimeSeriesPopulationResponse> {
        logger.debug("Getting all ward time series population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward time series population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardTimeSeriesPopulationByWard(
            wardNumber: Int
    ): List<WardTimeSeriesPopulationResponse> {
        logger.debug("Getting ward time series population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward time series population data for a specific year. */
    @Transactional(readOnly = true)
    override fun getWardTimeSeriesPopulationByYear(
            year: Int
    ): List<WardTimeSeriesPopulationResponse> {
        logger.debug("Getting ward time series population data for year: $year")

        return repository.findByYear(year).map(mapper::toResponse)
    }

    /** Get latest population data for each ward. */
    @Transactional(readOnly = true)
    override fun getLatestWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting latest ward population summary")

        return repository.findLatestForEachWard().map(mapper::toWardSummaryResponse)
    }

    /** Get summary of total population by year across all wards. */
    @Transactional(readOnly = true)
    override fun getYearPopulationSummary(): List<YearPopulationSummaryResponse> {
        logger.debug("Getting year population summary")

        return repository.getYearPopulationSummary().map(mapper::toYearSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardTimeSeriesPopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward time series population data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardTimeSeriesPopulationFilterDto
    ): Specification<WardTimeSeriesPopulation> {
        var spec: Specification<WardTimeSeriesPopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardTimeSeriesPopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.year?.let { year ->
            val yearSpec =
                    Specification<WardTimeSeriesPopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("year"), year)
                    }
            spec = spec?.and(yearSpec) ?: yearSpec
        }

        return spec ?: Specification.where(null)
    }
}
