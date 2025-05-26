package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.api.controller.WardWiseHouseholdsOnLoanController
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.CreateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.UpdateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.WardWiseHouseholdsOnLoanFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.service.WardWiseHouseholdsOnLoanService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseHouseholdsOnLoanController interface.
 *
 * This controller provides REST endpoints for managing ward-wise households on loan data.
 * Endpoints handle CRUD operations and statistics retrieval for economic data.
 *
 * @property service Service for managing ward-wise households on loan data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseHouseholdsOnLoanControllerImpl(
    private val service: WardWiseHouseholdsOnLoanService,
    private val i18nMessageService: I18nMessageService
) : WardWiseHouseholdsOnLoanController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise households on loan data.
     */
    override fun createWardWiseHouseholdsOnLoan(
        createDto: CreateWardWiseHouseholdsOnLoanDto
    ): ResponseEntity<ApiResponse<WardWiseHouseholdsOnLoanResponse>> {
        logger.info("Request received to create ward-wise households on loan data: $createDto")

        val createdData = service.createWardWiseHouseholdsOnLoan(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("economics.ward.households.loan.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise households on loan data.
     */
    override fun updateWardWiseHouseholdsOnLoan(
        id: UUID,
        updateDto: UpdateWardWiseHouseholdsOnLoanDto
    ): ResponseEntity<ApiResponse<WardWiseHouseholdsOnLoanResponse>> {
        logger.info("Request received to update ward-wise households on loan data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseHouseholdsOnLoan(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.households.loan.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise households on loan data.
     */
    override fun deleteWardWiseHouseholdsOnLoan(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise households on loan data with ID: $id")

        service.deleteWardWiseHouseholdsOnLoan(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.households.loan.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise households on loan data by ID.
     */
    override fun getWardWiseHouseholdsOnLoanById(id: UUID): ResponseEntity<ApiResponse<WardWiseHouseholdsOnLoanResponse>> {
        logger.debug("Request received to get ward-wise households on loan data with ID: $id")

        val data = service.getWardWiseHouseholdsOnLoanById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise households on loan data with optional filtering.
     */
    override fun getAllWardWiseHouseholdsOnLoan(
        filter: WardWiseHouseholdsOnLoanFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseHouseholdsOnLoanResponse>>> {
        logger.debug("Request received to get all ward-wise households on loan data with filter: $filter")

        val data = service.getAllWardWiseHouseholdsOnLoan(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise households on loan data for a specific ward.
     */
    override fun getWardWiseHouseholdsOnLoanByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<WardWiseHouseholdsOnLoanResponse?>> {
        logger.debug("Request received to get ward-wise households on loan data for ward: $wardNumber")

        val data = service.getWardWiseHouseholdsOnLoanByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.getByWard.success")
            )
        )
    }

    /**
     * Gets summary of households on loan across all wards.
     */
    override fun getHouseholdsOnLoanSummary(): ResponseEntity<ApiResponse<WardWiseHouseholdsOnLoanSummaryResponse>> {
        logger.debug("Request received to get households on loan summary")

        val summaryData = service.getHouseholdsOnLoanSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.households.loan.summary.success")
            )
        )
    }
}
