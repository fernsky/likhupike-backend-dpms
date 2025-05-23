package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.CreateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.UpdateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.WardAgeGenderWiseMarriedAgeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardAgeGenderWiseMarriedAgeResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.mapper.WardAgeGenderWiseMarriedAgeMapper
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.WardAgeGenderWiseMarriedAge
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.repository.WardAgeGenderWiseMarriedAgeRepository
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.service.WardAgeGenderWiseMarriedAgeService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardAgeGenderWiseMarriedAgeService]. */
@Service
class WardAgeGenderWiseMarriedAgeServiceImpl(
        private val repository: WardAgeGenderWiseMarriedAgeRepository,
        private val mapper: WardAgeGenderWiseMarriedAgeMapper,
        private val securityService: SecurityService
) : WardAgeGenderWiseMarriedAgeService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-age-gender-wise married age data. */
    @Transactional
    override fun createWardAgeGenderWiseMarriedAge(
            createDto: CreateWardAgeGenderWiseMarriedAgeDto
    ): WardAgeGenderWiseMarriedAgeResponse {
        logger.info(
                "Creating ward-age-gender-wise married age data for ward: ${createDto.wardNumber}, " +
                        "age group: ${createDto.ageGroup}, gender: ${createDto.gender}"
        )

        // Check if data already exists for this ward, age group, and gender
        if (repository.existsByWardNumberAndAgeGroupAndGender(
                        createDto.wardNumber,
                        createDto.ageGroup,
                        createDto.gender
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber}, age group ${createDto.ageGroup}, " +
                            "and gender ${createDto.gender}"
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
        logger.info("Created ward-age-gender-wise married age data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-age-gender-wise married age data. */
    @Transactional
    override fun updateWardAgeGenderWiseMarriedAge(
            id: UUID,
            updateDto: UpdateWardAgeGenderWiseMarriedAgeDto
    ): WardAgeGenderWiseMarriedAgeResponse {
        logger.info("Updating ward-age-gender-wise married age data with ID: $id")

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
                    "Data already exists for ward ${updateDto.wardNumber}, age group ${updateDto.ageGroup}, " +
                            "and gender ${updateDto.gender}"
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
        logger.info("Updated ward-age-gender-wise married age data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-age-gender-wise married age data. */
    @Transactional
    override fun deleteWardAgeGenderWiseMarriedAge(id: UUID) {
        logger.info("Deleting ward-age-gender-wise married age data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-age-gender-wise married age data with ID: $id")
    }

    /** Get ward-age-gender-wise married age data by ID. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseMarriedAgeById(id: UUID): WardAgeGenderWiseMarriedAgeResponse {
        logger.debug("Getting ward-age-gender-wise married age data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-age-gender-wise married age data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardAgeGenderWiseMarriedAge(
            filter: WardAgeGenderWiseMarriedAgeFilterDto?
    ): List<WardAgeGenderWiseMarriedAgeResponse> {
        logger.debug("Getting all ward-age-gender-wise married age data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise married age data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseMarriedAgeByWard(
            wardNumber: Int
    ): List<WardAgeGenderWiseMarriedAgeResponse> {
        logger.debug("Getting ward-age-gender-wise married age data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise married age data for a specific age group. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseMarriedAgeByAgeGroup(
            ageGroup: MarriedAgeGroup
    ): List<WardAgeGenderWiseMarriedAgeResponse> {
        logger.debug("Getting ward-age-gender-wise married age data for age group: $ageGroup")

        return repository.findByAgeGroup(ageGroup).map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise married age data for a specific gender. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseMarriedAgeByGender(
            gender: Gender
    ): List<WardAgeGenderWiseMarriedAgeResponse> {
        logger.debug("Getting ward-age-gender-wise married age data for gender: $gender")

        return repository.findByGender(gender).map(mapper::toResponse)
    }

    /** Get summary of age group and gender population across all wards. */
    @Transactional(readOnly = true)
    override fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummaryResponse> {
        logger.debug("Getting age group and gender population summary")

        return repository.getAgeGroupGenderPopulationSummary().map(mapper::toAgeGroupGenderSummaryResponse)
    }

    /** Get summary of total population by ward across all age groups and genders. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardAgeGenderWiseMarriedAge {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-age-gender-wise married age data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardAgeGenderWiseMarriedAgeFilterDto
    ): Specification<WardAgeGenderWiseMarriedAge> {
        var spec: Specification<WardAgeGenderWiseMarriedAge>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardAgeGenderWiseMarriedAge> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.ageGroup?.let { ageGroup ->
            val ageGroupSpec =
                    Specification<WardAgeGenderWiseMarriedAge> { root, _, cb ->
                        cb.equal(root.get<MarriedAgeGroup>("ageGroup"), ageGroup)
                    }
            spec = spec?.and(ageGroupSpec) ?: ageGroupSpec
        }

        filter.gender?.let { gender ->
            val genderSpec =
                    Specification<WardAgeGenderWiseMarriedAge> { root, _, cb ->
                        cb.equal(root.get<Gender>("gender"), gender)
                    }
            spec = spec?.and(genderSpec) ?: genderSpec
        }

        return spec ?: Specification.where(null)
    }
}
