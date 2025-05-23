package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.CreateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.UpdateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.WardWiseCastePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.CastePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardWiseCastePopulationResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.mapper.WardWiseCastePopulationMapper
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.WardWiseCastePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.repository.WardWiseCastePopulationRepository
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.service.WardWiseCastePopulationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseCastePopulationService]. */
@Service
class WardWiseCastePopulationServiceImpl(
        private val repository: WardWiseCastePopulationRepository,
        private val mapper: WardWiseCastePopulationMapper,
        private val securityService: SecurityService
) : WardWiseCastePopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise caste population data. */
    @Transactional
    override fun createWardWiseCastePopulation(
            createDto: CreateWardWiseCastePopulationDto
    ): WardWiseCastePopulationResponse {
        logger.info(
                "Creating ward-wise caste population data for ward: ${createDto.wardNumber}, caste: ${createDto.casteType}"
        )

        // Check if data already exists for this ward and caste
        if (repository.existsByWardNumberAndCasteType(
                        createDto.wardNumber,
                        createDto.casteType
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and caste ${createDto.casteType}"
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
        logger.info("Created ward-wise caste population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise caste population data. */
    @Transactional
    override fun updateWardWiseCastePopulation(
            id: UUID,
            updateDto: UpdateWardWiseCastePopulationDto
    ): WardWiseCastePopulationResponse {
        logger.info("Updating ward-wise caste population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or caste)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.casteType != updateDto.casteType) &&
                        repository.existsByWardNumberAndCasteType(
                                updateDto.wardNumber,
                                updateDto.casteType
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and caste ${updateDto.casteType}"
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
        logger.info("Updated ward-wise caste population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise caste population data. */
    @Transactional
    override fun deleteWardWiseCastePopulation(id: UUID) {
        logger.info("Deleting ward-wise caste population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise caste population data with ID: $id")
    }

    /** Get ward-wise caste population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseCastePopulationById(id: UUID): WardWiseCastePopulationResponse {
        logger.debug("Getting ward-wise caste population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise caste population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseCastePopulation(
            filter: WardWiseCastePopulationFilterDto?
    ): List<WardWiseCastePopulationResponse> {
        logger.debug("Getting all ward-wise caste population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise caste population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseCastePopulationByWard(
            wardNumber: Int
    ): List<WardWiseCastePopulationResponse> {
        logger.debug("Getting ward-wise caste population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise caste population data for a specific caste type. */
    @Transactional(readOnly = true)
    override fun getWardWiseCastePopulationByCaste(
            casteType: CasteType
    ): List<WardWiseCastePopulationResponse> {
        logger.debug("Getting ward-wise caste population data for caste: $casteType")

        return repository.findByCasteType(casteType).map(mapper::toResponse)
    }

    /** Get summary of caste population across all wards. */
    @Transactional(readOnly = true)
    override fun getCastePopulationSummary(): List<CastePopulationSummaryResponse> {
        logger.debug("Getting caste population summary")

        return repository.getCastePopulationSummary().map(mapper::toCasteSummaryResponse)
    }

    /** Get summary of total population by ward across all castes. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseCastePopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise caste population data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseCastePopulationFilterDto
    ): Specification<WardWiseCastePopulation> {
        var spec: Specification<WardWiseCastePopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseCastePopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.casteType?.let { caste ->
            val casteSpec =
                    Specification<WardWiseCastePopulation> { root, _, cb ->
                        cb.equal(root.get<CasteType>("casteType"), caste)
                    }
            spec = spec?.and(casteSpec) ?: casteSpec
        }

        return spec ?: Specification.where(null)
    }
}
