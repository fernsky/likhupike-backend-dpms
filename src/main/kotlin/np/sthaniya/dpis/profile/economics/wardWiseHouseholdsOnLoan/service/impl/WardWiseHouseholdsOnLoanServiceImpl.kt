package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.service.impl

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.profile.economics.common.exception.EconomicsException
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.CreateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.UpdateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.WardWiseHouseholdsOnLoanFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.mapper.WardWiseHouseholdsOnLoanMapper
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.model.WardWiseHouseholdsOnLoan
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.repository.WardWiseHouseholdsOnLoanRepository
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.service.WardWiseHouseholdsOnLoanService
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of [WardWiseHouseholdsOnLoanService]. */
@Service
class WardWiseHouseholdsOnLoanServiceImpl(
        private val repository: WardWiseHouseholdsOnLoanRepository,
        private val mapper: WardWiseHouseholdsOnLoanMapper,
        private val securityService: SecurityService
) : WardWiseHouseholdsOnLoanService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Create new ward-wise households on loan data. */
    @Transactional
    override fun createWardWiseHouseholdsOnLoan(
            createDto: CreateWardWiseHouseholdsOnLoanDto
    ): WardWiseHouseholdsOnLoanResponse {
        logger.info(
                "Creating ward-wise households on loan data for ward: ${createDto.wardNumber}"
        )

        // Check if data already exists for this ward
        if (repository.existsByWardNumber(createDto.wardNumber)) {
            val msg = "Data already exists for ward ${createDto.wardNumber}"
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
        logger.info("Created ward-wise households on loan data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Update existing ward-wise households on loan data. */
    @Transactional
    override fun updateWardWiseHouseholdsOnLoan(
            id: UUID,
            updateDto: UpdateWardWiseHouseholdsOnLoanDto
    ): WardWiseHouseholdsOnLoanResponse {
        logger.info("Updating ward-wise households on loan data with ID: $id")

        val entity = findEntityById(id)

        // Check if updating to a ward that already exists (if changing ward)
        if ((entity.wardNumber != updateDto.wardNumber) &&
                repository.existsByWardNumber(updateDto.wardNumber)
        ) {
            val msg = "Data already exists for ward ${updateDto.wardNumber}"
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
        logger.info("Updated ward-wise households on loan data with ID: ${savedEntity.id}")

        return mapper.toResponse(savedEntity)
    }

    /** Delete ward-wise households on loan data. */
    @Transactional
    override fun deleteWardWiseHouseholdsOnLoan(id: UUID) {
        logger.info("Deleting ward-wise households on loan data with ID: $id")

        val entity = findEntityById(id)
        repository.delete(entity)

        logger.info("Deleted ward-wise households on loan data with ID: $id")
    }

    /** Get ward-wise households on loan data by ID. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseholdsOnLoanById(id: UUID): WardWiseHouseholdsOnLoanResponse {
        logger.debug("Getting ward-wise households on loan data with ID: $id")

        val entity = findEntityById(id)
        return mapper.toResponse(entity)
    }

    /** Get all ward-wise households on loan data with optional filtering. */
    @Transactional(readOnly = true)
    override fun getAllWardWiseHouseholdsOnLoan(
            filter: WardWiseHouseholdsOnLoanFilterDto?
    ): List<WardWiseHouseholdsOnLoanResponse> {
        logger.debug("Getting all ward-wise households on loan data with filter: $filter")

        val spec = filter?.let { createSpecification(it) }
        val result =
                if (spec != null) {
                    repository.findAll(spec)
                } else {
                    repository.findAll()
                }

        return result.map(mapper::toResponse)
    }

    /** Get ward-wise households on loan data for a specific ward. */
    @Transactional(readOnly = true)
    override fun getWardWiseHouseholdsOnLoanByWard(
            wardNumber: Int
    ): WardWiseHouseholdsOnLoanResponse? {
        logger.debug("Getting ward-wise households on loan data for ward: $wardNumber")

        val entity = repository.findByWardNumber(wardNumber)
        return entity?.let { mapper.toResponse(it) }
    }

    /** Get summary of households on loan across all wards. */
    @Transactional(readOnly = true)
    override fun getHouseholdsOnLoanSummary(): WardWiseHouseholdsOnLoanSummaryResponse {
        logger.debug("Getting households on loan summary")

        val summary = repository.getHouseholdsOnLoanSummary()
        return mapper.toSummaryResponse(summary)
    }

    /** Find an entity by ID or throw an exception. */
    private fun findEntityById(id: UUID): WardWiseHouseholdsOnLoan {
        return repository.findById(id).orElseThrow {
            logger.warn("Ward-wise households on loan data not found with ID: $id")
            EconomicsException.EconomicDataNotFoundException(id.toString())
        }
    }

    /** Create a specification based on filter criteria. */
    private fun createSpecification(
            filter: WardWiseHouseholdsOnLoanFilterDto
    ): Specification<WardWiseHouseholdsOnLoan>? {
        var spec: Specification<WardWiseHouseholdsOnLoan>? = null

        filter.wardNumber?.let { ward ->
            spec =
                    Specification<WardWiseHouseholdsOnLoan> { root, _, cb ->
                        cb.equal(root.get<Int>("wardNumber"), ward)
                    }
        }

        return spec
    }
}
