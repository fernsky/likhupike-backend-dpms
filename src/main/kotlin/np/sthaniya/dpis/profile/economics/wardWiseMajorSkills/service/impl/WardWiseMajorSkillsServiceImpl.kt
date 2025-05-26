package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import np.sthaniya.dpis.profile.economics.common.model.SkillType
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.CreateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.UpdateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.WardWiseMajorSkillsFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.SkillPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardSkillsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardWiseMajorSkillsResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.mapper.WardWiseMajorSkillsMapper
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.model.WardWiseMajorSkills
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.repository.WardWiseMajorSkillsRepository
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.service.WardWiseMajorSkillsService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of [WardWiseMajorSkillsService].
 */
@Service
class WardWiseMajorSkillsServiceImpl(
    private val repository: WardWiseMajorSkillsRepository,
    private val mapper: WardWiseMajorSkillsMapper,
    private val securityService: SecurityService
) : WardWiseMajorSkillsService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Create new ward-wise major skills data.
     */
    @Transactional
    override fun createWardWiseMajorSkills(
        createDto: CreateWardWiseMajorSkillsDto
    ): WardWiseMajorSkillsResponse {
        logger.info(
            "Creating ward-wise major skills data for ward: ${createDto.wardNumber}, skill: ${createDto.skill}"
        )

        // Check if data already exists for this ward and skill
        if (repository.existsByWardNumberAndSkill(
                createDto.wardNumber,
                createDto.skill
            )
        ) {
            val msg =
                "Data already exists for ward ${createDto.wardNumber} and skill ${createDto.skill}"
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
        logger.info("Created ward-wise major skills data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /**
     * Update existing ward-wise major skills data.
     */
    @Transactional
    override fun updateWardWiseMajorSkills(
        id: UUID,
        updateDto: UpdateWardWiseMajorSkillsDto
    ): WardWiseMajorSkillsResponse {
        logger.info("Updating ward-wise major skills data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or skill)
        if ((entity.wardNumber != updateDto.wardNumber ||
                entity.skill != updateDto.skill) &&
            repository.existsByWardNumberAndSkill(
                updateDto.wardNumber,
                updateDto.skill
            )
        ) {
            val msg =
                "Data already exists for ward ${updateDto.wardNumber} and skill ${updateDto.skill}"
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
        logger.info("Updated ward-wise major skills data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /**
     * Delete ward-wise major skills data.
     */
    @Transactional
    override fun deleteWardWiseMajorSkills(id: UUID) {
        logger.info("Deleting ward-wise major skills data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise major skills data with ID: $id")
    }

    /**
     * Get ward-wise major skills data by ID.
     */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorSkillsById(id: UUID): WardWiseMajorSkillsResponse {
        logger.debug("Getting ward-wise major skills data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /**
     * Get all ward-wise major skills data with optional filtering.
     */
    @Transactional(readOnly = true)
    override fun getAllWardWiseMajorSkills(
        filter: WardWiseMajorSkillsFilterDto?
    ): List<WardWiseMajorSkillsResponse> {
        logger.debug("Getting all ward-wise major skills data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
            if (spec != null) {
                repository.findAll(spec)
            } else {
                repository.findAll()
            }

        return result.map(mapper::toResponse)
    }

    /**
     * Get all ward-wise major skills data for a specific ward.
     */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorSkillsByWard(
        wardNumber: Int
    ): List<WardWiseMajorSkillsResponse> {
        logger.debug("Getting ward-wise major skills data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /**
     * Get all ward-wise major skills data for a specific skill type.
     */
    @Transactional(readOnly = true)
    override fun getWardWiseMajorSkillsBySkill(
        skill: SkillType
    ): List<WardWiseMajorSkillsResponse> {
        logger.debug("Getting ward-wise major skills data for skill: $skill")

        return repository.findBySkill(skill).map(mapper::toResponse)
    }

    /**
     * Get summary of skill population across all wards.
     */
    @Transactional(readOnly = true)
    override fun getSkillPopulationSummary(): List<SkillPopulationSummaryResponse> {
        logger.debug("Getting skill population summary")

        return repository.getSkillPopulationSummary().map(mapper::toSkillSummaryResponse)
    }

    /**
     * Get summary of total population with skills by ward.
     */
    @Transactional(readOnly = true)
    override fun getWardSkillsSummary(): List<WardSkillsSummaryResponse> {
        logger.debug("Getting ward skills summary")

        return repository.getWardSkillsSummary().map(mapper::toWardSummaryResponse)
    }

    /**
     * Find an entity by ID or throw an exception.
     */
    private fun findEntityById(id: UUID): WardWiseMajorSkills {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise major skills data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /**
     * Create a specification based on filter criteria.
     */
    private fun createSpecification(
        filter: WardWiseMajorSkillsFilterDto
    ): Specification<WardWiseMajorSkills> {
        var spec: Specification<WardWiseMajorSkills>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                Specification<WardWiseMajorSkills> { root, _, cb ->
                    cb.equal(root.get<Int>("wardNumber"), ward)
                }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.skill?.let { skill ->
            val skillSpec =
                Specification<WardWiseMajorSkills> { root, _, cb ->
                    cb.equal(root.get<SkillType>("skill"), skill)
                }
            spec = spec?.and(skillSpec) ?: skillSpec
        }

        return spec ?: Specification.where(null)
    }
}
