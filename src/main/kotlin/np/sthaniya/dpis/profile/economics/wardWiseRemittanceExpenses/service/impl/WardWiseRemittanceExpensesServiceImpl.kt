package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.CreateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.UpdateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.WardWiseRemittanceExpensesFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.RemittanceExpenseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardRemittanceSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardWiseRemittanceExpensesResponse
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.mapper.WardWiseRemittanceExpensesMapper
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.model.WardWiseRemittanceExpenses
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.repository.WardWiseRemittanceExpensesRepository
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.service.WardWiseRemittanceExpensesService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseRemittanceExpensesService]. */
@Service
class WardWiseRemittanceExpensesServiceImpl(
        private val repository: WardWiseRemittanceExpensesRepository,
        private val mapper: WardWiseRemittanceExpensesMapper,
        private val securityService: SecurityService
) : WardWiseRemittanceExpensesService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise remittance expenses data. */
    @Transactional
    override fun createWardWiseRemittanceExpenses(
            createDto: CreateWardWiseRemittanceExpensesDto
    ): WardWiseRemittanceExpensesResponse {
        logger.info(
                "Creating ward-wise remittance expenses data for ward: ${createDto.wardNumber}, " +
                        "remittance expense: ${createDto.remittanceExpense}"
        )

        // Check if data already exists for this ward and remittance expense
        if (repository.existsByWardNumberAndRemittanceExpense(
                        createDto.wardNumber,
                        createDto.remittanceExpense
                )
        ) {
            val msg = "Data already exists for ward ${createDto.wardNumber} and remittance expense ${createDto.remittanceExpense}"
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
        logger.info("Created ward-wise remittance expenses data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise remittance expenses data. */
    @Transactional
    override fun updateWardWiseRemittanceExpenses(
            id: UUID,
            updateDto: UpdateWardWiseRemittanceExpensesDto
    ): WardWiseRemittanceExpensesResponse {
        logger.info("Updating ward-wise remittance expenses data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a combination that already exists (if changing ward or remittance expense)
        if ((entity.wardNumber != updateDto.wardNumber ||
                        entity.remittanceExpense != updateDto.remittanceExpense) &&
                repository.existsByWardNumberAndRemittanceExpense(
                        updateDto.wardNumber,
                        updateDto.remittanceExpense
                )
        ) {
            val msg = "Data already exists for ward ${updateDto.wardNumber} and remittance expense ${updateDto.remittanceExpense}"
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
        logger.info("Updated ward-wise remittance expenses data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise remittance expenses data. */
    @Transactional
    override fun deleteWardWiseRemittanceExpenses(id: UUID) {
        logger.info("Deleting ward-wise remittance expenses data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise remittance expenses data with ID: $id")
    }

    /** Get ward-wise remittance expenses data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseRemittanceExpensesById(id: UUID): WardWiseRemittanceExpensesResponse {
        logger.debug("Getting ward-wise remittance expenses data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise remittance expenses data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseRemittanceExpenses(
            filter: WardWiseRemittanceExpensesFilterDto?
    ): List<WardWiseRemittanceExpensesResponse> {
        logger.debug("Getting all ward-wise remittance expenses data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get all ward-wise remittance expenses data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseRemittanceExpensesByWard(
            wardNumber: Int
    ): List<WardWiseRemittanceExpensesResponse> {
        logger.debug("Getting ward-wise remittance expenses data for ward: $wardNumber")

        return repository.findByWardNumber(wardNumber).map(mapper::toResponse)
    }

    /** Get all ward-wise remittance expenses data for a specific remittance expense type. */
    @Transactional(readOnly = true)
    override fun getWardWiseRemittanceExpensesByRemittanceExpense(
            remittanceExpense: RemittanceExpenseType
    ): List<WardWiseRemittanceExpensesResponse> {
        logger.debug("Getting ward-wise remittance expenses data for remittance expense: $remittanceExpense")

        return repository.findByRemittanceExpense(remittanceExpense).map(mapper::toResponse)
    }

    /** Get summary of remittance expenses across all wards. */
    @Transactional(readOnly = true)
    override fun getRemittanceExpenseSummary(): List<RemittanceExpenseSummaryResponse> {
        logger.debug("Getting remittance expense summary")

        return repository.getRemittanceExpenseSummary().map(mapper::toRemittanceExpenseSummaryResponse)
    }

    /** Get summary of total households by ward across all remittance expense types. */
    @Transactional(readOnly = true)
    override fun getWardRemittanceSummary(): List<WardRemittanceSummaryResponse> {
        logger.debug("Getting ward remittance summary")

        return repository.getWardRemittanceSummary().map(mapper::toWardRemittanceSummaryResponse)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseRemittanceExpenses {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise remittance expenses data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseRemittanceExpensesFilterDto
    ): Specification<WardWiseRemittanceExpenses> {
        var spec: Specification<WardWiseRemittanceExpenses>? = null

        filter.wardNumber?.let { ward ->
            val wardSpec =
                    Specification<WardWiseRemittanceExpenses> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
            spec = spec?.and(wardSpec) ?: wardSpec
        }

        filter.remittanceExpense?.let { remittanceExpense ->
            val expenseSpec =
                    Specification<WardWiseRemittanceExpenses> { root, _, cb ->
                        cb.equal(root.get<RemittanceExpenseType>("remittanceExpense"), remittanceExpense)
                    }
            spec = spec?.and(expenseSpec) ?: expenseSpec
        }

        return spec ?: Specification.where(null)
    }
}
