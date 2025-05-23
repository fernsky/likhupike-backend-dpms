package np.sthaniya.dpis.profile.demographics.demographicSummary.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.demographicSummary.api.controller.DemographicSummaryController
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateSingleFieldDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.demographicSummary.service.DemographicSummaryService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implementation of the DemographicSummaryController interface.
 *
 * This controller provides REST endpoints for managing demographic summary data.
 * Endpoints handle getting and updating demographic summary data for the municipality.
 *
 * @property service Service for managing demographic summary data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class DemographicSummaryControllerImpl(
    private val service: DemographicSummaryService,
    private val i18nMessageService: I18nMessageService
) : DemographicSummaryController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Gets the demographic summary data.
     */
    override fun getDemographicSummary(): ResponseEntity<ApiResponse<DemographicSummaryResponse>> {
        logger.debug("Request received to get demographic summary")

        val data = service.getDemographicSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.summary.get.success")
            )
        )
    }

    /**
     * Updates the demographic summary data.
     */
    override fun updateDemographicSummary(
        request: UpdateDemographicSummaryDto
    ): ResponseEntity<ApiResponse<DemographicSummaryResponse>> {
        logger.info("Request received to update demographic summary")

        val updatedData = service.updateDemographicSummary(request)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.summary.update.success")
            )
        )
    }

    /**
     * Updates a single field in the demographic summary data.
     */
    override fun updateSingleField(
        request: UpdateSingleFieldDto
    ): ResponseEntity<ApiResponse<DemographicSummaryResponse>> {
        logger.info("Request received to update demographic summary field: ${request.field}")

        val updatedData = service.updateSingleField(request.field, request.value)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.summary.updateField.success")
            )
        )
    }
}
