package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.CreateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.UpdateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.WardWiseHouseholdsLoanUseFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.LoanUseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardHouseholdsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardWiseHouseholdsLoanUseResponse
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.mapper.WardWiseHouseholdsLoanUseMapper
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.model.WardWiseHouseholdsLoanUse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.repository.WardWiseHouseholdsLoanUseRepository
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.service.WardWiseHouseholdsLoanUseService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseHouseholdsLoanUseService]. */
@Service
class WardWiseHouseholdsLoanUseServiceImpl(
        private val repository: WardWiseHouseholdsLoanUseRepository,
        private val mapper: WardWiseHouseholdsLoanUseMapper,
        private val securityService: SecurityService
) : WardWiseHouseholdsLoanUseService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise households loan use data. */
    @Transactional
    override fun createWardWiseHouseholdsLoanUse(
            createDto: CreateWardWiseHouseholdsLoanUseDto
    ): WardWiseHouseholdsLoanUseResponse {
        logger.info(
                "Creating ward-wise households loan use data for ward: ${createDto.wardNumber}, loan use: ${createDto.loanUse}"
        )

        // Check if data already exists for this ward and loan use type
        if (repository.existsByWardNumberAndLoanUse(
                        createDto.wardNumber,
                        createDto.loanUse
                )
        ) {
            val msg =
                    "Data already exists for ward ${createDto.wardNumber} and loan use ${createDto.loanUse}"
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
        logger.info("Created ward-wise households loan use data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise households loan use data. */
    @Transactional
    override fun updateWardWiseHouseholdsLoanUse(
            id: UUID,
            updateDto: UpdateWardWiseHouseholdsLoanUseDto
    ): WardWiseHouseholdsLoanUseResponse {
        logger.info("Updating ward-wise households loan use data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or loan use)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.loanUse != updateDto.loanUse) &&
                        repository.existsByWardNumberAndLoanUse(
                                updateDto.wardNumber,
                                updateDto.loanUse
                        )
        ) {
            val msg =
                    "Data already exists for ward ${updateDto.wardNumber} and loan use ${updateDto.loanUse}"
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
        logger.info("Updated ward-wise households loan use data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise households loan use data. */
    @Transactional
    override fun deleteWardWiseHouseholdsLoanUse(id: UUID) {
        logger.info("Deleting ward-wise households loan use data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise households loan use data with ID: $id")
    }

    /** Get ward-wise households loan use data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseholdsLoanUseById(id: UUID): WardWiseHouseholdsLoanUseResponse {
        logger.debug("Getting ward-wise households loan use data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise households loan use data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseHouseholdsLoanUse(
            filter: WardWiseHouseholdsLoanUseFilterDto?
    ): List<WardWiseHouseholdsLoanUseResponse> {
        logger.debug("Getting all ward-wise households loan use data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise households loan use data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseholdsLoanUseByWard(
            wardNumber: Int
    ): List<WardWiseHouseholdsLoanUseResponse> {
        logger.debug("Getting ward-wise households loan use data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise households loan use data for a specific loan use type. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseholdsLoanUseByLoanUse(
            loanUse: LoanUseType
    ): List<WardWiseHouseholdsLoanUseResponse> {
        logger.debug("Getting ward-wise households loan use data for loan use: $loanUse")

        return repository.findByLoanUse(loanUse).map(mapper::toResponse)
    }

    /** Get summary of households by loan use type across all wards. */
    @Transactional(readOnly = true)
    override fun getLoanUseSummary(): List<LoanUseSummaryResponse> {
        logger.debug("Getting loan use summary")

        return repository.getLoanUseSummary().map(mapper::toLoanUseSummaryResponse)
    }

    /** Get summary of total households by ward across all loan use types. */
    @Transactional(readOnly = true)
    override fun getWardHouseholdsSummary(): List<WardHouseholdsSummaryResponse> {
        logger.debug("Getting ward households summary")

        return repository.getWardHouseholdsSummary().map(mapper::toWardSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseHouseholdsLoanUse {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise households loan use data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseHouseholdsLoanUseFilterDto
    ): Specification<WardWiseHouseholdsLoanUse> {
        var spec: Specification<WardWiseHouseholdsLoanUse>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseHouseholdsLoanUse> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.loanUse?.let { loanUse ->
            val loanUseSpec =
                    Specification<WardWiseHouseholdsLoanUse> { root, _, cb ->
                        cb.equal(root.get<LoanUseType>("loanUse"), loanUse)
                    }
            spec = spec?.and(loanUseSpec) ?: loanUseSpec
        }

        return spec ?: Specification.where(null)
    }
}
