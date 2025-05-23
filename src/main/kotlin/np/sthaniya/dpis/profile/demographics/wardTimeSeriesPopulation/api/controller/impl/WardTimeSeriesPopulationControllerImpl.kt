package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.api.controller.WardTimeSeriesPopulationController
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.CreateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.UpdateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.WardTimeSeriesPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardTimeSeriesPopulationResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.YearPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.service.WardTimeSeriesPopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardTimeSeriesPopulationController interface.
 *
 * This controller provides REST endpoints for managing ward time series population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward time series population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardTimeSeriesPopulationControllerImpl(
    private val service: WardTimeSeriesPopulationService,
    private val i18nMessageService: I18nMessageService
) : WardTimeSeriesPopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward time series population data.
     */
    override fun createWardTimeSeriesPopulation(
        createDto: CreateWardTimeSeriesPopulationDto
    ): ResponseEntity<ApiResponse<WardTimeSeriesPopulationResponse>> {
        logger.info("Request received to create ward time series population data: $createDto")

        val createdData = service.createWardTimeSeriesPopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.timeseries.create.success")
                )
            )
    }

    /**
     * Updates existing ward time series population data.
     */
    override fun updateWardTimeSeriesPopulation(
        id: UUID,
        updateDto: UpdateWardTimeSeriesPopulationDto
    ): ResponseEntity<ApiResponse<WardTimeSeriesPopulationResponse>> {
        logger.info("Request received to update ward time series population data with ID $id: $updateDto")

        val updatedData = service.updateWardTimeSeriesPopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.update.success")
            )
        )
    }

    /**
     * Deletes ward time series population data.
     */
    override fun deleteWardTimeSeriesPopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward time series population data with ID: $id")

        service.deleteWardTimeSeriesPopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.timeseries.delete.success")
            )
        )
    }

    /**
     * Gets ward time series population data by ID.
     */
    override fun getWardTimeSeriesPopulationById(id: UUID): ResponseEntity<ApiResponse<WardTimeSeriesPopulationResponse>> {
        logger.debug("Request received to get ward time series population data with ID: $id")

        val data = service.getWardTimeSeriesPopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.get.success")
            )
        )
    }

    /**
     * Gets all ward time series population data with optional filtering.
     */
    override fun getAllWardTimeSeriesPopulation(
        filter: WardTimeSeriesPopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardTimeSeriesPopulationResponse>>> {
        logger.debug("Request received to get all ward time series population data with filter: $filter")

        val data = service.getAllWardTimeSeriesPopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.getAll.success")
            )
        )
    }

    /**
     * Gets ward time series population data for a specific ward.
     */
    override fun getWardTimeSeriesPopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardTimeSeriesPopulationResponse>>> {
        logger.debug("Request received to get ward time series population data for ward: $wardNumber")

        val data = service.getWardTimeSeriesPopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.getByWard.success")
            )
        )
    }

    /**
     * Gets ward time series population data for a specific year.
     */
    override fun getWardTimeSeriesPopulationByYear(
        year: Int
    ): ResponseEntity<ApiResponse<List<WardTimeSeriesPopulationResponse>>> {
        logger.debug("Request received to get ward time series population data for year: $year")

        val data = service.getWardTimeSeriesPopulationByYear(year)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.getByYear.success")
            )
        )
    }

    /**
     * Gets latest population data for each ward.
     */
    override fun getLatestWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get latest ward population summary")

        val summaryData = service.getLatestWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.summaryLatest.success")
            )
        )
    }

    /**
     * Gets summary of total population by year across all wards.
     */
    override fun getYearPopulationSummary(): ResponseEntity<ApiResponse<List<YearPopulationSummaryResponse>>> {
        logger.debug("Request received to get year population summary")

        val summaryData = service.getYearPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.timeseries.summaryByYear.success")
            )
        )
    }
}
