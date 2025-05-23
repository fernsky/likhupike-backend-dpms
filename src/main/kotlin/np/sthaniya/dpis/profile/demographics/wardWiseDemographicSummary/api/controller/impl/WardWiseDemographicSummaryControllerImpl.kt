package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.api.controller.WardWiseDemographicSummaryController
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.CreateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.UpdateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.WardWiseDemographicSummaryFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response.WardWiseDemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.service.WardWiseDemographicSummaryService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseDemographicSummaryController interface.
 *
 * This controller provides REST endpoints for managing ward-wise demographic summary data.
 * Endpoints handle CRUD operations for demographic data.
 *
 * @property service Service for managing ward-wise demographic summary data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseDemographicSummaryControllerImpl(
    private val service: WardWiseDemographicSummaryService,
    private val i18nMessageService: I18nMessageService
) : WardWiseDemographicSummaryController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise demographic summary data.
     */
    override fun createWardWiseDemographicSummary(
        createDto: CreateWardWiseDemographicSummaryDto
    ): ResponseEntity<ApiResponse<WardWiseDemographicSummaryResponse>> {
        logger.info("Request received to create ward-wise demographic summary data: $createDto")

        val createdData = service.createWardWiseDemographicSummary(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.summary.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise demographic summary data.
     */
    override fun updateWardWiseDemographicSummary(
        id: UUID,
        updateDto: UpdateWardWiseDemographicSummaryDto
    ): ResponseEntity<ApiResponse<WardWiseDemographicSummaryResponse>> {
        logger.info("Request received to update ward-wise demographic summary data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseDemographicSummary(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.summary.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise demographic summary data.
     */
    override fun deleteWardWiseDemographicSummary(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise demographic summary data with ID: $id")

        service.deleteWardWiseDemographicSummary(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.summary.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise demographic summary data by ID.
     */
    override fun getWardWiseDemographicSummaryById(id: UUID): ResponseEntity<ApiResponse<WardWiseDemographicSummaryResponse>> {
        logger.debug("Request received to get ward-wise demographic summary data with ID: $id")

        val data = service.getWardWiseDemographicSummaryById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.summary.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise demographic summary data with optional filtering.
     */
    override fun getAllWardWiseDemographicSummary(
        filter: WardWiseDemographicSummaryFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseDemographicSummaryResponse>>> {
        logger.debug("Request received to get all ward-wise demographic summary data with filter: $filter")

        val data = service.getAllWardWiseDemographicSummary(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.summary.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise demographic summary data for a specific ward.
     */
    override fun getWardWiseDemographicSummaryByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<WardWiseDemographicSummaryResponse?>> {
        logger.debug("Request received to get ward-wise demographic summary data for ward: $wardNumber")

        val data = service.getWardWiseDemographicSummaryByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.summary.getByWard.success")
            )
        )
    }
}
