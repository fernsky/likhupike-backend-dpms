package np.sthaniya.dpis.profile.demographics.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.api.controller.WardWiseReligionPopulationController
import np.sthaniya.dpis.profile.demographics.dto.request.CreateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.UpdateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.WardWiseReligionPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.dto.response.ReligionPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardWiseReligionPopulationResponse
import np.sthaniya.dpis.profile.demographics.model.ReligionType
import np.sthaniya.dpis.profile.demographics.service.WardWiseReligionPopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseReligionPopulationController interface.
 *
 * This controller provides REST endpoints for managing ward-wise religion population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-wise religion population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseReligionPopulationControllerImpl(
    private val service: WardWiseReligionPopulationService,
    private val i18nMessageService: I18nMessageService
) : WardWiseReligionPopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise religion population data.
     */
    override fun createWardWiseReligionPopulation(
        createDto: CreateWardWiseReligionPopulationDto
    ): ResponseEntity<ApiResponse<WardWiseReligionPopulationResponse>> {
        logger.info("Request received to create ward-wise religion population data: $createDto")

        val createdData = service.createWardWiseReligionPopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.religion.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise religion population data.
     */
    override fun updateWardWiseReligionPopulation(
        id: UUID,
        updateDto: UpdateWardWiseReligionPopulationDto
    ): ResponseEntity<ApiResponse<WardWiseReligionPopulationResponse>> {
        logger.info("Request received to update ward-wise religion population data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseReligionPopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.religion.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise religion population data.
     */
    override fun deleteWardWiseReligionPopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise religion population data with ID: $id")

        service.deleteWardWiseReligionPopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.religion.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise religion population data by ID.
     */
    override fun getWardWiseReligionPopulationById(id: UUID): ResponseEntity<ApiResponse<WardWiseReligionPopulationResponse>> {
        logger.debug("Request received to get ward-wise religion population data with ID: $id")

        val data = service.getWardWiseReligionPopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.religion.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise religion population data with optional filtering.
     */
    override fun getAllWardWiseReligionPopulation(
        filter: WardWiseReligionPopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseReligionPopulationResponse>>> {
        logger.debug("Request received to get all ward-wise religion population data with filter: $filter")

        val data = service.getAllWardWiseReligionPopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.religion.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise religion population data for a specific ward.
     */
    override fun getWardWiseReligionPopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseReligionPopulationResponse>>> {
        logger.debug("Request received to get ward-wise religion population data for ward: $wardNumber")

        val data = service.getWardWiseReligionPopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.religion.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise religion population data for a specific religion type.
     */
    override fun getWardWiseReligionPopulationByReligion(
        religionType: ReligionType
    ): ResponseEntity<ApiResponse<List<WardWiseReligionPopulationResponse>>> {
        logger.debug("Request received to get ward-wise religion population data for religion: $religionType")

        val data = service.getWardWiseReligionPopulationByReligion(religionType)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.religion.getByReligion.success")
            )
        )
    }

    /**
     * Gets summary of religion population across all wards.
     */
    override fun getReligionPopulationSummary(): ResponseEntity<ApiResponse<List<ReligionPopulationSummaryResponse>>> {
        logger.debug("Request received to get religion population summary")

        val summaryData = service.getReligionPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.religion.summaryByReligion.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all religions.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.religion.summaryByWard.success")
            )
        )
    }
}
