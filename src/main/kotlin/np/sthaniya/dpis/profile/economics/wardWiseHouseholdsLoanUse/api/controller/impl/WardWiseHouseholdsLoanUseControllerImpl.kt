package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.api.controller.WardWiseHouseholdsLoanUseController
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.CreateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.UpdateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.WardWiseHouseholdsLoanUseFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.LoanUseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardHouseholdsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardWiseHouseholdsLoanUseResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.service.WardWiseHouseholdsLoanUseService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseHouseholdsLoanUseController interface.
 *
 * This controller provides REST endpoints for managing ward-wise households loan use data.
 * Endpoints handle CRUD operations and statistics retrieval for economic data.
 *
 * @property service Service for managing ward-wise households loan use data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseHouseholdsLoanUseControllerImpl(
    private val service: WardWiseHouseholdsLoanUseService,
    private val i18nMessageService: I18nMessageService
) : WardWiseHouseholdsLoanUseController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise households loan use data.
     */
    override fun createWardWiseHouseholdsLoanUse(
        createDto: CreateWardWiseHouseholdsLoanUseDto
    ): ResponseEntity<ApiResponse<WardWiseHouseholdsLoanUseResponse>> {
        logger.info("Request received to create ward-wise households loan use data: $createDto")

        val createdData = service.createWardWiseHouseholdsLoanUse(createDto)

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
     * Updates existing ward-wise households loan use data.
     */
    override fun updateWardWiseHouseholdsLoanUse(
        id: UUID,
        updateDto: UpdateWardWiseHouseholdsLoanUseDto
    ): ResponseEntity<ApiResponse<WardWiseHouseholdsLoanUseResponse>> {
        logger.info("Request received to update ward-wise households loan use data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseHouseholdsLoanUse(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.households.loan.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise households loan use data.
     */
    override fun deleteWardWiseHouseholdsLoanUse(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise households loan use data with ID: $id")

        service.deleteWardWiseHouseholdsLoanUse(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.households.loan.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise households loan use data by ID.
     */
    override fun getWardWiseHouseholdsLoanUseById(id: UUID): ResponseEntity<ApiResponse<WardWiseHouseholdsLoanUseResponse>> {
        logger.debug("Request received to get ward-wise households loan use data with ID: $id")

        val data = service.getWardWiseHouseholdsLoanUseById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise households loan use data with optional filtering.
     */
    override fun getAllWardWiseHouseholdsLoanUse(
        filter: WardWiseHouseholdsLoanUseFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseHouseholdsLoanUseResponse>>> {
        logger.debug("Request received to get all ward-wise households loan use data with filter: $filter")

        val data = service.getAllWardWiseHouseholdsLoanUse(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise households loan use data for a specific ward.
     */
    override fun getWardWiseHouseholdsLoanUseByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseHouseholdsLoanUseResponse>>> {
        logger.debug("Request received to get ward-wise households loan use data for ward: $wardNumber")

        val data = service.getWardWiseHouseholdsLoanUseByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise households loan use data for a specific loan use type.
     */
    override fun getWardWiseHouseholdsLoanUseByLoanUse(
        loanUse: LoanUseType
    ): ResponseEntity<ApiResponse<List<WardWiseHouseholdsLoanUseResponse>>> {
        logger.debug("Request received to get ward-wise households loan use data for loan use: $loanUse")

        val data = service.getWardWiseHouseholdsLoanUseByLoanUse(loanUse)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.households.loan.getByLoanUse.success")
            )
        )
    }

    /**
     * Gets summary of households by loan use type across all wards.
     */
    override fun getLoanUseSummary(): ResponseEntity<ApiResponse<List<LoanUseSummaryResponse>>> {
        logger.debug("Request received to get loan use summary")

        val summaryData = service.getLoanUseSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.households.loan.summaryByLoanUse.success")
            )
        )
    }

    /**
     * Gets summary of total households by ward across all loan use types.
     */
    override fun getWardHouseholdsSummary(): ResponseEntity<ApiResponse<List<WardHouseholdsSummaryResponse>>> {
        logger.debug("Request received to get ward households summary")

        val summaryData = service.getWardHouseholdsSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.households.loan.summaryByWard.success")
            )
        )
    }
}
