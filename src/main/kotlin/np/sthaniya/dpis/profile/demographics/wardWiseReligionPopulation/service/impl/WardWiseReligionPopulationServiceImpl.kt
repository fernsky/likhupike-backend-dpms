package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.request.CreateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.request.UpdateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.request.WardWiseReligionPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.response.ReligionPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.response.WardWiseReligionPopulationResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.mapper.WardWiseReligionPopulationMapper
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.WardWiseReligionPopulation
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.repository.WardWiseReligionPopulationRepository
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.service.WardWiseReligionPopulationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseReligionPopulationService]. */
@Service
class WardWiseReligionPopulationServiceImpl(
        private val repository: WardWiseReligionPopulationRepository,
        private val mapper: WardWiseReligionPopulationMapper,
        private val securityService: SecurityService
) : WardWiseReligionPopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise religion population data. */
    @Transactional
    override fun createWardWiseReligionPopulation(
            createDto: CreateWardWiseReligionPopulationDto
    ): WardWiseReligionPopulationResponse {
        logger.info(
                "Creating ward-wise religion population data for ward: ${createDto.wardNumber}, religion: ${createDto.religionType}"
        )

        // Check if data already exists for this ward and religion
        if (repository.existsByWardNumberAndReligionType(
                        createDto.wardNumber,
                        createDto.religionType
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and religion ${createDto.religionType}"
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
        logger.info("Created ward-wise religion population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise religion population data. */
    @Transactional
    override fun updateWardWiseReligionPopulation(
            id: UUID,
            updateDto: UpdateWardWiseReligionPopulationDto
    ): WardWiseReligionPopulationResponse {
        logger.info("Updating ward-wise religion population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or religion)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.religionType != updateDto.religionType) &&
                        repository.existsByWardNumberAndReligionType(
                                updateDto.wardNumber,
                                updateDto.religionType
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and religion ${updateDto.religionType}"
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
        logger.info("Updated ward-wise religion population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise religion population data. */
    @Transactional
    override fun deleteWardWiseReligionPopulation(id: UUID) {
        logger.info("Deleting ward-wise religion population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise religion population data with ID: $id")
    }

    /** Get ward-wise religion population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseReligionPopulationById(id: UUID): WardWiseReligionPopulationResponse {
        logger.debug("Getting ward-wise religion population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise religion population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseReligionPopulation(
            filter: WardWiseReligionPopulationFilterDto?
    ): List<WardWiseReligionPopulationResponse> {
        logger.debug("Getting all ward-wise religion population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise religion population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseReligionPopulationByWard(
            wardNumber: Int
    ): List<WardWiseReligionPopulationResponse> {
        logger.debug("Getting ward-wise religion population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise religion population data for a specific religion type. */
    @Transactional(readOnly = true)
    override fun getWardWiseReligionPopulationByReligion(
            religionType: ReligionType
    ): List<WardWiseReligionPopulationResponse> {
        logger.debug("Getting ward-wise religion population data for religion: $religionType")

        return repository.findByReligionType(religionType).map(mapper::toResponse)
    }

    /** Get summary of religion population across all wards. */
    @Transactional(readOnly = true)
    override fun getReligionPopulationSummary(): List<ReligionPopulationSummaryResponse> {
        logger.debug("Getting religion population summary")

        return repository.getReligionPopulationSummary().map(mapper::toReligionSummaryResponse)
    }

    /** Get summary of total population by ward across all religions. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseReligionPopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise religion population data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseReligionPopulationFilterDto
    ): Specification<WardWiseReligionPopulation> {
        var spec: Specification<WardWiseReligionPopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseReligionPopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.religionType?.let { religion ->
            val religionSpec =
                    Specification<WardWiseReligionPopulation> { root, _, cb ->
                        cb.equal(root.get<ReligionType>("religionType"), religion)
                    }
            spec = spec?.and(religionSpec) ?: religionSpec
        }

        return spec ?: Specification.where(null)
    }
}
