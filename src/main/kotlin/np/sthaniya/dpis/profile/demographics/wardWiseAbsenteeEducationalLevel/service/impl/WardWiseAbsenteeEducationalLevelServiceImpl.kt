package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.CreateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.UpdateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.WardWiseAbsenteeEducationalLevelFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.EducationalLevelPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardWiseAbsenteeEducationalLevelResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.mapper.WardWiseAbsenteeEducationalLevelMapper
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.model.WardWiseAbsenteeEducationalLevel
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.repository.WardWiseAbsenteeEducationalLevelRepository
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.service.WardWiseAbsenteeEducationalLevelService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseAbsenteeEducationalLevelService]. */
@Service
class WardWiseAbsenteeEducationalLevelServiceImpl(
        private val repository: WardWiseAbsenteeEducationalLevelRepository,
        private val mapper: WardWiseAbsenteeEducationalLevelMapper,
        private val securityService: SecurityService
) : WardWiseAbsenteeEducationalLevelService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise absentee educational level data. */
    @Transactional
    override fun createWardWiseAbsenteeEducationalLevel(
            createDto: CreateWardWiseAbsenteeEducationalLevelDto
    ): WardWiseAbsenteeEducationalLevelResponse {
        logger.info(
                "Creating ward-wise absentee educational level data for ward: ${createDto.wardNumber}, " +
                        "educational level: ${createDto.educationalLevel}"
        )

        // Check if data already exists for this ward and educational level
        if (repository.existsByWardNumberAndEducationalLevel(
                        createDto.wardNumber,
                        createDto.educationalLevel
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and educational level ${createDto.educationalLevel}"
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
        logger.info("Created ward-wise absentee educational level data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise absentee educational level data. */
    @Transactional
    override fun updateWardWiseAbsenteeEducationalLevel(
            id: UUID,
            updateDto: UpdateWardWiseAbsenteeEducationalLevelDto
    ): WardWiseAbsenteeEducationalLevelResponse {
        logger.info("Updating ward-wise absentee educational level data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or educational level)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.educationalLevel != updateDto.educationalLevel) &&
                        repository.existsByWardNumberAndEducationalLevel(
                                updateDto.wardNumber,
                                updateDto.educationalLevel
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and educational level ${updateDto.educationalLevel}"
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
        logger.info("Updated ward-wise absentee educational level data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise absentee educational level data. */
    @Transactional
    override fun deleteWardWiseAbsenteeEducationalLevel(id: UUID) {
        logger.info("Deleting ward-wise absentee educational level data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise absentee educational level data with ID: $id")
    }

    /** Get ward-wise absentee educational level data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseAbsenteeEducationalLevelById(id: UUID): WardWiseAbsenteeEducationalLevelResponse {
        logger.debug("Getting ward-wise absentee educational level data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise absentee educational level data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseAbsenteeEducationalLevel(
            filter: WardWiseAbsenteeEducationalLevelFilterDto?
    ): List<WardWiseAbsenteeEducationalLevelResponse> {
        logger.debug("Getting all ward-wise absentee educational level data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise absentee educational level data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseAbsenteeEducationalLevelByWard(
            wardNumber: Int
    ): List<WardWiseAbsenteeEducationalLevelResponse> {
        logger.debug("Getting ward-wise absentee educational level data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise absentee educational level data for a specific educational level. */
    @Transactional(readOnly = true)
    override fun getWardWiseAbsenteeEducationalLevelByEducationalLevel(
            educationalLevel: EducationalLevelType
    ): List<WardWiseAbsenteeEducationalLevelResponse> {
        logger.debug("Getting ward-wise absentee educational level data for educational level: $educationalLevel")

        return repository.findByEducationalLevel(educationalLevel).map(mapper::toResponse)
    }

    /** Get summary of educational level population across all wards. */
    @Transactional(readOnly = true)
    override fun getEducationalLevelPopulationSummary(): List<EducationalLevelPopulationSummaryResponse> {
        logger.debug("Getting educational level population summary")

        return repository.getEducationalLevelPopulationSummary().map(mapper::toEducationalLevelSummaryResponse)
    }

    /** Get summary of total population by ward across all educational levels. */
    @Transactional(readOnly = true)
    override fun getWardPopulationSummary(): List<WardPopulationSummaryResponse> {
        logger.debug("Getting ward population summary")

        return repository.getWardPopulationSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseAbsenteeEducationalLevel {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise absentee educational level data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseAbsenteeEducationalLevelFilterDto
    ): Specification<WardWiseAbsenteeEducationalLevel> {
        var spec: Specification<WardWiseAbsenteeEducationalLevel>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseAbsenteeEducationalLevel> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.educationalLevel?.let { level ->
            val levelSpec =
                    Specification<WardWiseAbsenteeEducationalLevel> { root, _, cb ->
                        cb.equal(root.get<EducationalLevelType>("educationalLevel"), level)
                    }
            spec = spec?.and(levelSpec) ?: levelSpec
        }

        return spec ?: Specification.where(null)
    }
}
