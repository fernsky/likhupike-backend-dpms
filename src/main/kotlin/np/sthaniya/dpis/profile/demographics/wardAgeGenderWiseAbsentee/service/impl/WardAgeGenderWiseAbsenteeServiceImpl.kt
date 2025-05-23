package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.CreateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.UpdateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.WardAgeGenderWiseAbsenteeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAbsenteePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAgeGenderWiseAbsenteeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.mapper.WardAgeGenderWiseAbsenteeMapper
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.WardAgeGenderWiseAbsentee
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.repository.WardAgeGenderWiseAbsenteeRepository
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.service.WardAgeGenderWiseAbsenteeService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardAgeGenderWiseAbsenteeService]. */
@Service
class WardAgeGenderWiseAbsenteeServiceImpl(
        private val repository: WardAgeGenderWiseAbsenteeRepository,
        private val mapper: WardAgeGenderWiseAbsenteeMapper,
        private val securityService: SecurityService
) : WardAgeGenderWiseAbsenteeService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-age-gender-wise absentee data. */
    @Transactional
    override fun createWardAgeGenderWiseAbsentee(
            createDto: CreateWardAgeGenderWiseAbsenteeDto
    ): WardAgeGenderWiseAbsenteeResponse {
        logger.info(
                "Creating ward-age-gender-wise absentee data for ward: ${createDto.wardNumber}, " +
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
        logger.info("Created ward-age-gender-wise absentee data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-age-gender-wise absentee data. */
    @Transactional
    override fun updateWardAgeGenderWiseAbsentee(
            id: UUID,
            updateDto: UpdateWardAgeGenderWiseAbsenteeDto
    ): WardAgeGenderWiseAbsenteeResponse {
        logger.info("Updating ward-age-gender-wise absentee data with ID: $id")

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
        logger.info("Updated ward-age-gender-wise absentee data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-age-gender-wise absentee data. */
    @Transactional
    override fun deleteWardAgeGenderWiseAbsentee(id: UUID) {
        logger.info("Deleting ward-age-gender-wise absentee data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-age-gender-wise absentee data with ID: $id")
    }

    /** Get ward-age-gender-wise absentee data by ID. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseAbsenteeById(id: UUID): WardAgeGenderWiseAbsenteeResponse {
        logger.debug("Getting ward-age-gender-wise absentee data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-age-gender-wise absentee data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardAgeGenderWiseAbsentee(
            filter: WardAgeGenderWiseAbsenteeFilterDto?
    ): List<WardAgeGenderWiseAbsenteeResponse> {
        logger.debug("Getting all ward-age-gender-wise absentee data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise absentee data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseAbsenteeByWard(
            wardNumber: Int
    ): List<WardAgeGenderWiseAbsenteeResponse> {
        logger.debug("Getting ward-age-gender-wise absentee data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise absentee data for a specific age group. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseAbsenteeByAgeGroup(
            ageGroup: AbsenteeAgeGroup
    ): List<WardAgeGenderWiseAbsenteeResponse> {
        logger.debug("Getting ward-age-gender-wise absentee data for age group: $ageGroup")

        return repository.findByAgeGroup(ageGroup).map(mapper::toResponse)
    }

    /** Get all ward-age-gender-wise absentee data for a specific gender. */
    @Transactional(readOnly = true)
    override fun getWardAgeGenderWiseAbsenteeByGender(
            gender: Gender
    ): List<WardAgeGenderWiseAbsenteeResponse> {
        logger.debug("Getting ward-age-gender-wise absentee data for gender: $gender")

        return repository.findByGender(gender).map(mapper::toResponse)
    }

    /** Get summary of absentee population across all wards grouped by age group and gender. */
    @Transactional(readOnly = true)
    override fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummaryResponse> {
        logger.debug("Getting age group and gender population summary")

        return repository.getAgeGroupGenderPopulationSummary().map(mapper::toAgeGroupGenderSummaryResponse)
    }

    /** Get summary of total absentee population by ward across all age groups and genders. */
    @Transactional(readOnly = true)
    override fun getWardAbsenteePopulationSummary(): List<WardAbsenteePopulationSummaryResponse> {
        logger.debug("Getting ward absentee population summary")

        return repository.getWardAbsenteePopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardAgeGenderWiseAbsentee {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-age-gender-wise absentee data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardAgeGenderWiseAbsenteeFilterDto
    ): Specification<WardAgeGenderWiseAbsentee> {
        var spec: Specification<WardAgeGenderWiseAbsentee>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardAgeGenderWiseAbsentee> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.ageGroup?.let { ageGroup ->
            val ageGroupSpec =
                    Specification<WardAgeGenderWiseAbsentee> { root, _, cb ->
                        cb.equal(root.get<AbsenteeAgeGroup>("ageGroup"), ageGroup)
                    }
            spec = spec?.and(ageGroupSpec) ?: ageGroupSpec
        }

        filter.gender?.let { gender ->
            val genderSpec =
                    Specification<WardAgeGenderWiseAbsentee> { root, _, cb ->
                        cb.equal(root.get<Gender>("gender"), gender)
                    }
            spec = spec?.and(genderSpec) ?: genderSpec
        }

        return spec ?: Specification.where(null)
    }
}
