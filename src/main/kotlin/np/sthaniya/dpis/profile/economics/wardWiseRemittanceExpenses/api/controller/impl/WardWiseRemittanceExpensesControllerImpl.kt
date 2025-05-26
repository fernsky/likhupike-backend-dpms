package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.api.controller.WardWiseRemittanceExpensesController
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.CreateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.UpdateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.WardWiseRemittanceExpensesFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.RemittanceExpenseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardRemittanceSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardWiseRemittanceExpensesResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.service.WardWiseRemittanceExpensesService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseRemittanceExpensesController interface.
 *
 * This controller provides REST endpoints for managing ward-wise remittance expenses data.
 * Endpoints handle CRUD operations and statistics retrieval for economic data.
 *
 * @property service Service for managing ward-wise remittance expenses data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseRemittanceExpensesControllerImpl(
    private val service: WardWiseRemittanceExpensesService,
    private val i18nMessageService: I18nMessageService
) : WardWiseRemittanceExpensesController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise remittance expenses data.
     */
    override fun createWardWiseRemittanceExpenses(
        createDto: CreateWardWiseRemittanceExpensesDto
    ): ResponseEntity<ApiResponse<WardWiseRemittanceExpensesResponse>> {
        logger.info("Request received to create ward-wise remittance expenses data: $createDto")

        val createdData = service.createWardWiseRemittanceExpenses(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("economics.ward.remittance.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise remittance expenses data.
     */
    override fun updateWardWiseRemittanceExpenses(
        id: UUID,
        updateDto: UpdateWardWiseRemittanceExpensesDto
    ): ResponseEntity<ApiResponse<WardWiseRemittanceExpensesResponse>> {
        logger.info("Request received to update ward-wise remittance expenses data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseRemittanceExpenses(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.remittance.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise remittance expenses data.
     */
    override fun deleteWardWiseRemittanceExpenses(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise remittance expenses data with ID: $id")

        service.deleteWardWiseRemittanceExpenses(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.remittance.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise remittance expenses data by ID.
     */
    override fun getWardWiseRemittanceExpensesById(id: UUID): ResponseEntity<ApiResponse<WardWiseRemittanceExpensesResponse>> {
        logger.debug("Request received to get ward-wise remittance expenses data with ID: $id")

        val data = service.getWardWiseRemittanceExpensesById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.remittance.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise remittance expenses data with optional filtering.
     */
    override fun getAllWardWiseRemittanceExpenses(
        filter: WardWiseRemittanceExpensesFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseRemittanceExpensesResponse>>> {
        logger.debug("Request received to get all ward-wise remittance expenses data with filter: $filter")

        val data = service.getAllWardWiseRemittanceExpenses(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.remittance.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise remittance expenses data for a specific ward.
     */
    override fun getWardWiseRemittanceExpensesByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseRemittanceExpensesResponse>>> {
        logger.debug("Request received to get ward-wise remittance expenses data for ward: $wardNumber")

        val data = service.getWardWiseRemittanceExpensesByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.remittance.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise remittance expenses data for a specific remittance expense type.
     */
    override fun getWardWiseRemittanceExpensesByRemittanceExpense(
        remittanceExpense: RemittanceExpenseType
    ): ResponseEntity<ApiResponse<List<WardWiseRemittanceExpensesResponse>>> {
        logger.debug("Request received to get ward-wise remittance expenses data for remittance expense: $remittanceExpense")

        val data = service.getWardWiseRemittanceExpensesByRemittanceExpense(remittanceExpense)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.remittance.getByExpense.success")
            )
        )
    }

    /**
     * Gets summary of remittance expenses across all wards.
     */
    override fun getRemittanceExpenseSummary(): ResponseEntity<ApiResponse<List<RemittanceExpenseSummaryResponse>>> {
        logger.debug("Request received to get remittance expense summary")

        val summaryData = service.getRemittanceExpenseSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.remittance.summaryByExpense.success")
            )
        )
    }

    /**
     * Gets summary of total households by ward across all remittance expense types.
     */
    override fun getWardRemittanceSummary(): ResponseEntity<ApiResponse<List<WardRemittanceSummaryResponse>>> {
        logger.debug("Request received to get ward remittance summary")

        val summaryData = service.getWardRemittanceSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.remittance.summaryByWard.success")
            )
        )
    }
}
