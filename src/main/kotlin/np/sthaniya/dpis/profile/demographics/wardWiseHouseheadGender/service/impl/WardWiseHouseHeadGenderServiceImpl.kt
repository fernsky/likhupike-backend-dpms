package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.CreateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.UpdateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.WardWiseHouseHeadGenderFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardWiseHouseHeadGenderResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.mapper.WardWiseHouseHeadGenderMapper
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.model.WardWiseHouseHeadGender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.repository.WardWiseHouseHeadGenderRepository
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.service.WardWiseHouseHeadGenderService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseHouseHeadGenderService]. */
@Service
class WardWiseHouseHeadGenderServiceImpl(
        private val repository: WardWiseHouseHeadGenderRepository,
        private val mapper: WardWiseHouseHeadGenderMapper,
        private val securityService: SecurityService
) : WardWiseHouseHeadGenderService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise house head gender data. */
    @Transactional
    override fun createWardWiseHouseHeadGender(
            createDto: CreateWardWiseHouseHeadGenderDto
    ): WardWiseHouseHeadGenderResponse {
        logger.info(
                "Creating ward-wise house head gender data for ward: ${createDto.wardNumber}, gender: ${createDto.gender}"
        )

        // Check if data already exists for this ward and gender
        if (repository.existsByWardNumberAndGender(
                        createDto.wardNumber,
                        createDto.gender
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and gender ${createDto.gender}"
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
        logger.info("Created ward-wise house head gender data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise house head gender data. */
    @Transactional
    override fun updateWardWiseHouseHeadGender(
            id: UUID,
            updateDto: UpdateWardWiseHouseHeadGenderDto
    ): WardWiseHouseHeadGenderResponse {
        logger.info("Updating ward-wise house head gender data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or gender)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.gender != updateDto.gender) &&
                        repository.existsByWardNumberAndGender(
                                updateDto.wardNumber,
                                updateDto.gender
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and gender ${updateDto.gender}"
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
        logger.info("Updated ward-wise house head gender data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise house head gender data. */
    @Transactional
    override fun deleteWardWiseHouseHeadGender(id: UUID) {
        logger.info("Deleting ward-wise house head gender data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise house head gender data with ID: $id")
    }

    /** Get ward-wise house head gender data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseHeadGenderById(id: UUID): WardWiseHouseHeadGenderResponse {
        logger.debug("Getting ward-wise house head gender data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise house head gender data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseHouseHeadGender(
            filter: WardWiseHouseHeadGenderFilterDto?
    ): List<WardWiseHouseHeadGenderResponse> {
        logger.debug("Getting all ward-wise house head gender data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise house head gender data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseHeadGenderByWard(
            wardNumber: Int
    ): List<WardWiseHouseHeadGenderResponse> {
        logger.debug("Getting ward-wise house head gender data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise house head gender data for a specific gender. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseHeadGenderByGender(
            gender: Gender
    ): List<WardWiseHouseHeadGenderResponse> {
        logger.debug("Getting ward-wise house head gender data for gender: $gender")

        return repository.findByGender(gender).map(mapper::toResponse)
    }

    /** Get summary of gender population across all wards. */
    @Transactional(readOnly = true)
    override fun getGenderPopulationSummary(): List<GenderPopulationSummaryResponse> {
        logger.debug("Getting gender population summary")

        return repository.getGenderPopulationSummary().map(mapper::toGenderSummaryResponse)
    }

    /** Get summary of total population by ward across all genders. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseHouseHeadGender {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise house head gender data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseHouseHeadGenderFilterDto
    ): Specification<WardWiseHouseHeadGender> {
        var spec: Specification<WardWiseHouseHeadGender>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseHouseHeadGender> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.gender?.let { gender ->
            val genderSpec =
                    Specification<WardWiseHouseHeadGender> { root, _, cb ->
                        cb.equal(root.get<Gender>("gender"), gender)
                    }
            spec = spec?.and(genderSpec) ?: genderSpec
        }

        return spec ?: Specification.where(null)
    }
}
