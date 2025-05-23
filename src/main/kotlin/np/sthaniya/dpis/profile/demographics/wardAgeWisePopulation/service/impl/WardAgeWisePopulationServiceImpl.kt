package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.CreateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.UpdateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.WardAgeWisePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.AgeGroupPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeGenderSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeWisePopulationResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.mapper.WardAgeWisePopulationMapper
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.WardAgeWisePopulation
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository.WardAgeWisePopulationRepository
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.service.WardAgeWisePopulationService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardAgeWisePopulationService]. */
@Service
class WardAgeWisePopulationServiceImpl(
        private val repository: WardAgeWisePopulationRepository,
        private val mapper: WardAgeWisePopulationMapper,
        private val securityService: SecurityService
) : WardAgeWisePopulationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-age-wise population data. */
    @Transactional
    override fun createWardAgeWisePopulation(
            createDto: CreateWardAgeWisePopulationDto
    ): WardAgeWisePopulationResponse {
        logger.info(
                "Creating ward-age-wise population data for ward: ${createDto.wardNumber}, age group: ${createDto.ageGroup}, gender: ${createDto.gender}"
        )

        // Check if data already exists for this ward, age group, and gender
        if (repository.existsByWardNumberAndAgeGroupAndGender(
                        createDto.wardNumber,
                        createDto.ageGroup,
                        createDto.gender
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber}, age group ${createDto.ageGroup}, and gender ${createDto.gender}"
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
        logger.info("Created ward-age-wise population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-age-wise population data. */
    @Transactional
    override fun updateWardAgeWisePopulation(
            id: UUID,
            updateDto: UpdateWardAgeWisePopulationDto
    ): WardAgeWisePopulationResponse {
        logger.info("Updating ward-age-wise population data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward, age group, or gender)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.ageGroup != updateDto.ageGroup ||
                        entity.gender != updateDto.gender) &&
                        repository.existsByWardNumberAndAgeGroupAndGender(
                                updateDto.wardNumber,
                                updateDto.ageGroup,
                                updateDto.gender
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber}, age group ${updateDto.ageGroup}, and gender ${updateDto.gender}"
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
        logger.info("Updated ward-age-wise population data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-age-wise population data. */
    @Transactional
    override fun deleteWardAgeWisePopulation(id: UUID) {
        logger.info("Deleting ward-age-wise population data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-age-wise population data with ID: $id")
    }

    /** Get ward-age-wise population data by ID. */
    @Transactional(readOnly = true)
    override fun getWardAgeWisePopulationById(id: UUID): WardAgeWisePopulationResponse {
        logger.debug("Getting ward-age-wise population data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-age-wise population data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardAgeWisePopulation(
            filter: WardAgeWisePopulationFilterDto?
    ): List<WardAgeWisePopulationResponse> {
        logger.debug("Getting all ward-age-wise population data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-age-wise population data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardAgeWisePopulationByWard(
            wardNumber: Int
    ): List<WardAgeWisePopulationResponse> {
        logger.debug("Getting ward-age-wise population data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-age-wise population data for a specific age group. */
    @Transactional(readOnly = true)
    override fun getWardAgeWisePopulationByAgeGroup(
            ageGroup: AgeGroup
    ): List<WardAgeWisePopulationResponse> {
        logger.debug("Getting ward-age-wise population data for age group: $ageGroup")

        return repository.findByAgeGroup(ageGroup).map(mapper::toResponse)
    }

    /** Get all ward-age-wise population data for a specific gender. */
    @Transactional(readOnly = true)
    override fun getWardAgeWisePopulationByGender(
            gender: Gender
    ): List<WardAgeWisePopulationResponse> {
        logger.debug("Getting ward-age-wise population data for gender: $gender")

        return repository.findByGender(gender).map(mapper::toResponse)
    }

    /** Get summary of population by age group across all wards. */
    @Transactional(readOnly = true)
    override fun getAgeGroupPopulationSummary(): List<AgeGroupPopulationSummaryResponse> {
        logger.debug("Getting age group population summary")

        return repository.getAgeGroupPopulationSummary().map(mapper::toAgeGroupSummaryResponse)
    }

    /** Get summary of population by gender across all wards. */
    @Transactional(readOnly = true)
    override fun getGenderPopulationSummary(): List<GenderPopulationSummaryResponse> {
        logger.debug("Getting gender population summary")

        return repository.getGenderPopulationSummary().map(mapper::toGenderSummaryResponse)
    }

    /** Get summary of population by ward, age group, and gender. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderSummary(): List<WardAgeGenderSummaryResponse> {
        logger.debug("Getting ward-age-gender population summary")

        return repository.getWardAgeGenderSummary().map(mapper::toWardAgeGenderSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardAgeWisePopulation {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-age-wise population data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardAgeWisePopulationFilterDto
    ): Specification<WardAgeWisePopulation> {
        var spec: Specification<WardAgeWisePopulation>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardAgeWisePopulation> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.ageGroup?.let { ageGroup ->
            val ageGroupSpec =
                    Specification<WardAgeWisePopulation> { root, _, cb ->
                        cb.equal(root.get<AgeGroup>("ageGroup"), ageGroup)
                    }
            spec = spec?.and(ageGroupSpec) ?: ageGroupSpec
        }

        filter.gender?.let { gender ->
            val genderSpec =
                    Specification<WardAgeWisePopulation> { root, _, cb ->
                        cb.equal(root.get<Gender>("gender"), gender)
                    }
            spec = spec?.and(genderSpec) ?: genderSpec
        }

        return spec ?: Specification.where(null)
    }
}
