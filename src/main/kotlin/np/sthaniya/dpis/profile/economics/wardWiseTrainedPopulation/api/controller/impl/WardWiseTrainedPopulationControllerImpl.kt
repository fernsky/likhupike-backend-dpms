package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.api.controller.WardWiseTrainedPopulationController
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.CreateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.UpdateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.WardWiseTrainedPopulationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.TrainedPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardWiseTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.service.WardWiseTrainedPopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseTrainedPopulationController interface.
 *
 * This controller provides REST endpoints for managing ward-wise trained population data.
 * Endpoints handle CRUD operations and statistics retrieval for economic data.
 *
 * @property service Service for managing ward-wise trained population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseTrainedPopulationControllerImpl(
    private val service: WardWiseTrainedPopulationService,
    private val i18nMessageService: I18nMessageService
) : WardWiseTrainedPopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise trained population data.
     */
    override fun createWardWiseTrainedPopulation(
        createDto: CreateWardWiseTrainedPopulationDto
    ): ResponseEntity<ApiResponse<WardWiseTrainedPopulationResponse>> {
        logger.info("Request received to create ward-wise trained population data: $createDto")

        val createdData = service.createWardWiseTrainedPopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("economics.ward.trained.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise trained population data.
     */
    override fun updateWardWiseTrainedPopulation(
        id: UUID,
        updateDto: UpdateWardWiseTrainedPopulationDto
    ): ResponseEntity<ApiResponse<WardWiseTrainedPopulationResponse>> {
        logger.info("Request received to update ward-wise trained population data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseTrainedPopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.trained.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise trained population data.
     */
    override fun deleteWardWiseTrainedPopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise trained population data with ID: $id")

        service.deleteWardWiseTrainedPopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.trained.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise trained population data by ID.
     */
    override fun getWardWiseTrainedPopulationById(id: UUID): ResponseEntity<ApiResponse<WardWiseTrainedPopulationResponse>> {
        logger.debug("Request received to get ward-wise trained population data with ID: $id")

        val data = service.getWardWiseTrainedPopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.trained.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise trained population data with optional filtering.
     */
    override fun getAllWardWiseTrainedPopulation(
        filter: WardWiseTrainedPopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseTrainedPopulationResponse>>> {
        logger.debug("Request received to get all ward-wise trained population data with filter: $filter")

        val data = service.getAllWardWiseTrainedPopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.trained.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise trained population data for a specific ward.
     */
    override fun getWardWiseTrainedPopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<WardWiseTrainedPopulationResponse?>> {
        logger.debug("Request received to get ward-wise trained population data for ward: $wardNumber")

        val data = service.getWardWiseTrainedPopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.trained.getByWard.success")
            )
        )
    }

    /**
     * Gets summary of total trained population across all wards.
     */
    override fun getTrainedPopulationSummary(): ResponseEntity<ApiResponse<TrainedPopulationSummaryResponse>> {
        logger.debug("Request received to get trained population summary")

        val summaryData = service.getTrainedPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.trained.summaryTotal.success")
            )
        )
    }

    /**
     * Gets summary of trained population by ward.
     */
    override fun getWardTrainedPopulationSummary(): ResponseEntity<ApiResponse<List<WardTrainedPopulationResponse>>> {
        logger.debug("Request received to get ward trained population summary")

        val summaryData = service.getWardTrainedPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.trained.summaryByWard.success")
            )
        )
    }
}
