package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.api.controller.impl

import java.util.UUID
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.api.controller.WardWiseMajorOccupationController
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.CreateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.UpdateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.WardWiseMajorOccupationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.OccupationPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardOccupationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardWiseMajorOccupationResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.service.WardWiseMajorOccupationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implementation of the WardWiseMajorOccupationController interface.
 *
 * This controller provides REST endpoints for managing ward-wise major occupation data.
 * Endpoints handle CRUD operations and statistics retrieval for economic occupation data.
 *
 * @property service Service for managing ward-wise major occupation data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseMajorOccupationControllerImpl(
    private val service: WardWiseMajorOccupationService,
    private val i18nMessageService: I18nMessageService
) : WardWiseMajorOccupationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise major occupation data.
     */
    override fun createWardWiseMajorOccupation(
        createDto: CreateWardWiseMajorOccupationDto
    ): ResponseEntity<ApiResponse<WardWiseMajorOccupationResponse>> {
        logger.info("Request received to create ward-wise major occupation data: $createDto")

        val createdData = service.createWardWiseMajorOccupation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("economics.ward.occupation.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise major occupation data.
     */
    override fun updateWardWiseMajorOccupation(
        id: UUID,
        updateDto: UpdateWardWiseMajorOccupationDto
    ): ResponseEntity<ApiResponse<WardWiseMajorOccupationResponse>> {
        logger.info("Request received to update ward-wise major occupation data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseMajorOccupation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.occupation.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise major occupation data.
     */
    override fun deleteWardWiseMajorOccupation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise major occupation data with ID: $id")

        service.deleteWardWiseMajorOccupation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.occupation.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise major occupation data by ID.
     */
    override fun getWardWiseMajorOccupationById(id: UUID): ResponseEntity<ApiResponse<WardWiseMajorOccupationResponse>> {
        logger.debug("Request received to get ward-wise major occupation data with ID: $id")

        val data = service.getWardWiseMajorOccupationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.occupation.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise major occupation data with optional filtering.
     */
    override fun getAllWardWiseMajorOccupation(
        filter: WardWiseMajorOccupationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseMajorOccupationResponse>>> {
        logger.debug("Request received to get all ward-wise major occupation data with filter: $filter")

        val data = service.getAllWardWiseMajorOccupation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.occupation.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise major occupation data for a specific ward.
     */
    override fun getWardWiseMajorOccupationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseMajorOccupationResponse>>> {
        logger.debug("Request received to get ward-wise major occupation data for ward: $wardNumber")

        val data = service.getWardWiseMajorOccupationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.occupation.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise major occupation data for a specific occupation type.
     */
    override fun getWardWiseMajorOccupationByOccupation(
        occupation: OccupationType
    ): ResponseEntity<ApiResponse<List<WardWiseMajorOccupationResponse>>> {
        logger.debug("Request received to get ward-wise major occupation data for occupation: $occupation")

        val data = service.getWardWiseMajorOccupationByOccupation(occupation)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.occupation.getByOccupation.success")
            )
        )
    }

    /**
     * Gets summary of occupation population across all wards.
     */
    override fun getOccupationPopulationSummary(): ResponseEntity<ApiResponse<List<OccupationPopulationSummaryResponse>>> {
        logger.debug("Request received to get occupation population summary")

        val summaryData = service.getOccupationPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.occupation.summaryByOccupation.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all occupations.
     */
    override fun getWardOccupationSummary(): ResponseEntity<ApiResponse<List<WardOccupationSummaryResponse>>> {
        logger.debug("Request received to get ward occupation summary")

        val summaryData = service.getWardOccupationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.occupation.summaryByWard.success")
            )
        )
    }
}
