package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.CreateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.UpdateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.WardWiseMajorOccupationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.OccupationPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardOccupationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardWiseMajorOccupationResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.mapper.WardWiseMajorOccupationMapper
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.model.WardWiseMajorOccupation
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.repository.WardWiseMajorOccupationRepository
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.service.WardWiseMajorOccupationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseMajorOccupationService]. */
@Service
class WardWiseMajorOccupationServiceImpl(
        private val repository: WardWiseMajorOccupationRepository,
        private val mapper: WardWiseMajorOccupationMapper,
        private val securityService: SecurityService
) : WardWiseMajorOccupationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise major occupation data. */
    @Transactional
    override fun createWardWiseMajorOccupation(
            createDto: CreateWardWiseMajorOccupationDto
    ): WardWiseMajorOccupationResponse {
        logger.info(
                "Creating ward-wise major occupation data for ward: ${createDto.wardNumber}, occupation: ${createDto.occupation}"
        )

        // Check if data already exists for this ward and occupation
        if (repository.existsByWardNumberAndOccupation(
                        createDto.wardNumber,
                        createDto.occupation
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and occupation ${createDto.occupation}"
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
        logger.info("Created ward-wise major occupation data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise major occupation data. */
    @Transactional
    override fun updateWardWiseMajorOccupation(
            id: UUID,
            updateDto: UpdateWardWiseMajorOccupationDto
    ): WardWiseMajorOccupationResponse {
        logger.info("Updating ward-wise major occupation data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or occupation)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.occupation != updateDto.occupation) &&
                        repository.existsByWardNumberAndOccupation(
                                updateDto.wardNumber,
                                updateDto.occupation
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and occupation ${updateDto.occupation}"
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
        logger.info("Updated ward-wise major occupation data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise major occupation data. */
    @Transactional
    override fun deleteWardWiseMajorOccupation(id: UUID) {
        logger.info("Deleting ward-wise major occupation data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise major occupation data with ID: $id")
    }

    /** Get ward-wise major occupation data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorOccupationById(id: UUID): WardWiseMajorOccupationResponse {
        logger.debug("Getting ward-wise major occupation data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise major occupation data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseMajorOccupation(
            filter: WardWiseMajorOccupationFilterDto?
    ): List<WardWiseMajorOccupationResponse> {
        logger.debug("Getting all ward-wise major occupation data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise major occupation data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorOccupationByWard(
            wardNumber: Int
    ): List<WardWiseMajorOccupationResponse> {
        logger.debug("Getting ward-wise major occupation data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise major occupation data for a specific occupation type. */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorOccupationByOccupation(
            occupation: OccupationType
    ): List<WardWiseMajorOccupationResponse> {
        logger.debug("Getting ward-wise major occupation data for occupation: $occupation")

        return repository.findByOccupation(occupation).map(mapper::toResponse)
    }

    /** Get summary of occupation population across all wards. */
    @Transactional(readOnly = true)
    override fun getOccupationPopulationSummary(): List<OccupationPopulationSummaryResponse> {
        logger.debug("Getting occupation population summary")

        return repository.getOccupationPopulationSummary().map(mapper::toOccupationSummaryResponse)
    }

    /** Get summary of total population by ward across all occupations. */
    @Transactional(readOnly = true)
    override fun getWardOccupationSummary(): List<WardOccupationSummaryResponse> {
        logger.debug("Getting ward occupation summary")

        return repository.getWardOccupationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseMajorOccupation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise major occupation data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseMajorOccupationFilterDto
    ): Specification<WardWiseMajorOccupation> {
        var spec: Specification<WardWiseMajorOccupation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseMajorOccupation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.occupation?.let { occupation ->
            val occupationSpec =
                    Specification<WardWiseMajorOccupation> { root, _, cb ->
                        cb.equal(root.get<OccupationType>("occupation"), occupation)
                    }
            spec = spec?.and(occupationSpec) ?: occupationSpec
        }

        return spec ?: Specification.where(null)
    }
}
