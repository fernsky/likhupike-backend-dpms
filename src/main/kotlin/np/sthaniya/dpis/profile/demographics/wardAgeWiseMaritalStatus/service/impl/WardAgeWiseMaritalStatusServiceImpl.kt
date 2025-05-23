package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.CreateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.UpdateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.WardAgeWiseMaritalStatusFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.AgeGroupSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.MaritalStatusSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.WardAgeWiseMaritalStatusResponse
import np.sthaniya.dpis.profile.demographics.common.exception.DemographicsException
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.mapper.WardAgeWiseMaritalStatusMapper
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.WardWiseMaritalStatus
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.repository.WardAgeWiseMaritalStatusRepository
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.service.WardAgeWiseMaritalStatusService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardAgeWiseMaritalStatusService]. */
@Service
class WardAgeWiseMaritalStatusServiceImpl(
    private val repository: WardAgeWiseMaritalStatusRepository,
    private val mapper: WardAgeWiseMaritalStatusMapper,
    private val securityService: SecurityService
) : WardAgeWiseMaritalStatusService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward age-wise marital status data. */
    @Transactional
    override fun createWardAgeWiseMaritalStatus(
        createDto: CreateWardAgeWiseMaritalStatusDto
    ): WardAgeWiseMaritalStatusResponse {
        logger.info(
            "Creating ward age-wise marital status data for ward: ${createDto.wardNumber}, " +
                "age group: ${createDto.ageGroup}, marital status: ${createDto.maritalStatus}"
        )

        // Check if data already exists for this ward, age group, and marital status
        if (repository.existsByWardNumberAndAgeGroupAndMaritalStatus(
                createDto.wardNumber,
                createDto.ageGroup,
                createDto.maritalStatus
            )
        ) {
            val msg = "Data already exists for ward ${createDto.wardNumber}, " +
                "age group ${createDto.ageGroup}, and marital status ${createDto.maritalStatus}"
            logger.warn(msg)
            throw DemographicsException.DemographicDataAlreadyExistsException(msg)
        }

        // Create and save entity
        val entity = mapper.toEntity(createDto).apply {
            createdBy = securityService.getCurrentUser().id
            updatedBy = createdBy
        }

        val savedEntity = repository.save(entity)
        logger.info("Created ward age-wise marital status data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward age-wise marital status data. */
    @Transactional
    override fun updateWardAgeWiseMaritalStatus(
        id: UUID,
        updateDto: UpdateWardAgeWiseMaritalStatusDto
    ): WardAgeWiseMaritalStatusResponse {
        logger.info("Updating ward age-wise marital status data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward, age group, or marital status)
        if ((entity.wardNumber != updateDto.wardNumber ||
                entity.ageGroup != updateDto.ageGroup ||
                entity.maritalStatus != updateDto.maritalStatus) &&
            repository.existsByWardNumberAndAgeGroupAndMaritalStatus(
                updateDto.wardNumber,
                updateDto.ageGroup,
                updateDto.maritalStatus
            )
        ) {
            val msg = "Data already exists for ward ${updateDto.wardNumber}, " +
                "age group ${updateDto.ageGroup}, and marital status ${updateDto.maritalStatus}"
            logger.warn(msg)
            throw DemographicsException.DemographicDataAlreadyExistsException(msg)
        }

        // Update entity
        val updatedEntity = mapper.updateEntity(entity, updateDto).apply {
            updatedBy = securityService.getCurrentUser().id
            updatedAt = Instant.now()
        }

        val savedEntity = repository.save(updatedEntity)
        logger.info("Updated ward age-wise marital status data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward age-wise marital status data. */
    @Transactional
    override fun deleteWardAgeWiseMaritalStatus(id: UUID) {
        logger.info("Deleting ward age-wise marital status data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward age-wise marital status data with ID: $id")
    }

    /** Get ward age-wise marital status data by ID. */
    @Transactional(readOnly = true)
    override fun getWardAgeWiseMaritalStatusById(id: UUID): WardAgeWiseMaritalStatusResponse {
        logger.debug("Getting ward age-wise marital status data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward age-wise marital status data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardAgeWiseMaritalStatus(
        filter: WardAgeWiseMaritalStatusFilterDto?
    ): List<WardAgeWiseMaritalStatusResponse> {
        logger.debug("Getting all ward age-wise marital status data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result = if (spec != null) {
            repository.findAll(spec)
        } else {
            repository.findAll()
        }

        return result.map(mapper::toResponse)
    }

    /** Get all ward age-wise marital status data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardAgeWiseMaritalStatusByWard(
        wardNumber: Int
    ): List<WardAgeWiseMaritalStatusResponse> {
        logger.debug("Getting ward age-wise marital status data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward age-wise marital status data for a specific age group. */
    @Transactional(readOnly = true)
    override fun getWardAgeWiseMaritalStatusByAgeGroup(
        ageGroup: MaritalAgeGroup
    ): List<WardAgeWiseMaritalStatusResponse> {
        logger.debug("Getting ward age-wise marital status data for age group: $ageGroup")

        return repository.findByAgeGroup(ageGroup).map(mapper::toResponse)
    }

    /** Get all ward age-wise marital status data for a specific marital status. */
    @Transactional(readOnly = true)
    override fun getWardAgeWiseMaritalStatusByMaritalStatus(
        maritalStatus: MaritalStatusGroup
    ): List<WardAgeWiseMaritalStatusResponse> {
        logger.debug("Getting ward age-wise marital status data for marital status: $maritalStatus")

        return repository.findByMaritalStatus(maritalStatus).map(mapper::toResponse)
    }

    /** Get summary of marital status population across all wards and age groups. */
    @Transactional(readOnly = true)
    override fun getMaritalStatusSummary(): List<MaritalStatusSummaryResponse> {
        logger.debug("Getting marital status population summary")

        return repository.getMaritalStatusSummary().map(mapper::toMaritalStatusSummaryResponse)
    }

    /** Get summary of age group population across all wards and marital statuses. */
    @Transactional(readOnly = true)
    override fun getAgeGroupSummary(): List<AgeGroupSummaryResponse> {
        logger.debug("Getting age group population summary")

        return repository.getAgeGroupSummary().map(mapper::toAgeGroupSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseMaritalStatus {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward age-wise marital status data not found with ID: $id")
            DemographicsException.DemographicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
        filter: WardAgeWiseMaritalStatusFilterDto
    ): Specification<WardWiseMaritalStatus> {
        var spec: Specification<WardWiseMaritalStatus>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec = Specification<WardWiseMaritalStatus> { root, _, cb ->
                cb.equal(root.get<Int>("wardNumber"), ward)
            }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.ageGroup?.let { ageGroup ->
            val ageGroupSpec = Specification<WardWiseMaritalStatus> { root, _, cb ->
                cb.equal(root.get<MaritalAgeGroup>("ageGroup"), ageGroup)
            }
            spec = spec?.and(ageGroupSpec) ?: ageGroupSpec
        }

        filter.maritalStatus?.let { maritalStatus ->
            val maritalStatusSpec = Specification<WardWiseMaritalStatus> { root, _, cb ->
                cb.equal(root.get<MaritalStatusGroup>("maritalStatus"), maritalStatus)
            }
            spec = spec?.and(maritalStatusSpec) ?: maritalStatusSpec
        }

        return spec ?: Specification.where(null)
    }
}
