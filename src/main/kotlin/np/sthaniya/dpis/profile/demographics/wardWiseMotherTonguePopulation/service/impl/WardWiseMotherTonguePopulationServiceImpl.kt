package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.CreateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.UpdateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.WardWiseMotherTonguePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.LanguagePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardWiseMotherTonguePopulationResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.mapper.WardWiseMotherTonguePopulationMapper
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.WardWiseMotherTonguePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.repository.WardWiseMotherTonguePopulationRepository
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.service.WardWiseMotherTonguePopulationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseMotherTonguePopulationService]. */
@Service
class WardWiseMotherTonguePopulationServiceImpl(
        private val repository: WardWiseMotherTonguePopulationRepository,
        private val mapper: WardWiseMotherTonguePopulationMapper,
        private val securityService: SecurityService
) : WardWiseMotherTonguePopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise mother tongue population data. */
    @Transactional
    override fun createWardWiseMotherTonguePopulation(
            createDto: CreateWardWiseMotherTonguePopulationDto
    ): WardWiseMotherTonguePopulationResponse {
        logger.info(
                "Creating ward-wise mother tongue population data for ward: ${createDto.wardNumber}, language: ${createDto.languageType}"
        )

        // Check if data already exists for this ward and language
        if (repository.existsByWardNumberAndLanguageType(
                        createDto.wardNumber,
                        createDto.languageType
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and language ${createDto.languageType}"
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
        logger.info("Created ward-wise mother tongue population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise mother tongue population data. */
    @Transactional
    override fun updateWardWiseMotherTonguePopulation(
            id: UUID,
            updateDto: UpdateWardWiseMotherTonguePopulationDto
    ): WardWiseMotherTonguePopulationResponse {
        logger.info("Updating ward-wise mother tongue population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or language)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.languageType != updateDto.languageType) &&
                        repository.existsByWardNumberAndLanguageType(
                                updateDto.wardNumber,
                                updateDto.languageType
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and language ${updateDto.languageType}"
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
        logger.info("Updated ward-wise mother tongue population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise mother tongue population data. */
    @Transactional
    override fun deleteWardWiseMotherTonguePopulation(id: UUID) {
        logger.info("Deleting ward-wise mother tongue population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise mother tongue population data with ID: $id")
    }

    /** Get ward-wise mother tongue population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseMotherTonguePopulationById(id: UUID): WardWiseMotherTonguePopulationResponse {
        logger.debug("Getting ward-wise mother tongue population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise mother tongue population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseMotherTonguePopulation(
            filter: WardWiseMotherTonguePopulationFilterDto?
    ): List<WardWiseMotherTonguePopulationResponse> {
        logger.debug("Getting all ward-wise mother tongue population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise mother tongue population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseMotherTonguePopulationByWard(
            wardNumber: Int
    ): List<WardWiseMotherTonguePopulationResponse> {
        logger.debug("Getting ward-wise mother tongue population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise mother tongue population data for a specific language type. */
    @Transactional(readOnly = true)
    override fun getWardWiseMotherTonguePopulationByLanguage(
            languageType: LanguageType
    ): List<WardWiseMotherTonguePopulationResponse> {
        logger.debug("Getting ward-wise mother tongue population data for language: $languageType")

        return repository.findByLanguageType(languageType).map(mapper::toResponse)
    }

    /** Get summary of language population across all wards. */
    @Transactional(readOnly = true)
    override fun getLanguagePopulationSummary(): List<LanguagePopulationSummaryResponse> {
        logger.debug("Getting language population summary")

        return repository.getLanguagePopulationSummary().map(mapper::toLanguageSummaryResponse)
    }

    /** Get summary of total population by ward across all languages. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseMotherTonguePopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise mother tongue population data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseMotherTonguePopulationFilterDto
    ): Specification<WardWiseMotherTonguePopulation> {
        var spec: Specification<WardWiseMotherTonguePopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseMotherTonguePopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.languageType?.let { language ->
            val languageSpec =
                    Specification<WardWiseMotherTonguePopulation> { root, _, cb ->
                        cb.equal(root.get<LanguageType>("languageType"), language)
                    }
            spec = spec?.and(languageSpec) ?: languageSpec
        }

        return spec ?: Specification.where(null)
    }
}
