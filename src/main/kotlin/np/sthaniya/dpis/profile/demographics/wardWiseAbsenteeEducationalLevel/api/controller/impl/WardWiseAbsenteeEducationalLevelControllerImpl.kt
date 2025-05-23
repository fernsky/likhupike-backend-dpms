package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.api.controller.WardWiseAbsenteeEducationalLevelController
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.CreateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.UpdateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.WardWiseAbsenteeEducationalLevelFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.EducationalLevelPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardWiseAbsenteeEducationalLevelResponse
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.service.WardWiseAbsenteeEducationalLevelService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseAbsenteeEducationalLevelController interface.
 *
 * This controller provides REST endpoints for managing ward-wise absentee educational level data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-wise absentee educational level data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseAbsenteeEducationalLevelControllerImpl(
    private val service: WardWiseAbsenteeEducationalLevelService,
    private val i18nMessageService: I18nMessageService
) : WardWiseAbsenteeEducationalLevelController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise absentee educational level data.
     */
    override fun createWardWiseAbsenteeEducationalLevel(
        createDto: CreateWardWiseAbsenteeEducationalLevelDto
    ): ResponseEntity<ApiResponse<WardWiseAbsenteeEducationalLevelResponse>> {
        logger.info("Request received to create ward-wise absentee educational level data: $createDto")

        val createdData = service.createWardWiseAbsenteeEducationalLevel(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.absentee.education.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise absentee educational level data.
     */
    override fun updateWardWiseAbsenteeEducationalLevel(
        id: UUID,
        updateDto: UpdateWardWiseAbsenteeEducationalLevelDto
    ): ResponseEntity<ApiResponse<WardWiseAbsenteeEducationalLevelResponse>> {
        logger.info("Request received to update ward-wise absentee educational level data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseAbsenteeEducationalLevel(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise absentee educational level data.
     */
    override fun deleteWardWiseAbsenteeEducationalLevel(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise absentee educational level data with ID: $id")

        service.deleteWardWiseAbsenteeEducationalLevel(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise absentee educational level data by ID.
     */
    override fun getWardWiseAbsenteeEducationalLevelById(id: UUID): ResponseEntity<ApiResponse<WardWiseAbsenteeEducationalLevelResponse>> {
        logger.debug("Request received to get ward-wise absentee educational level data with ID: $id")

        val data = service.getWardWiseAbsenteeEducationalLevelById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise absentee educational level data with optional filtering.
     */
    override fun getAllWardWiseAbsenteeEducationalLevel(
        filter: WardWiseAbsenteeEducationalLevelFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>> {
        logger.debug("Request received to get all ward-wise absentee educational level data with filter: $filter")

        val data = service.getAllWardWiseAbsenteeEducationalLevel(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise absentee educational level data for a specific ward.
     */
    override fun getWardWiseAbsenteeEducationalLevelByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>> {
        logger.debug("Request received to get ward-wise absentee educational level data for ward: $wardNumber")

        val data = service.getWardWiseAbsenteeEducationalLevelByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise absentee educational level data for a specific educational level.
     */
    override fun getWardWiseAbsenteeEducationalLevelByEducationalLevel(
        educationalLevel: EducationalLevelType
    ): ResponseEntity<ApiResponse<List<WardWiseAbsenteeEducationalLevelResponse>>> {
        logger.debug("Request received to get ward-wise absentee educational level data for educational level: $educationalLevel")

        val data = service.getWardWiseAbsenteeEducationalLevelByEducationalLevel(educationalLevel)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.getByEducationalLevel.success")
            )
        )
    }

    /**
     * Gets summary of educational level population across all wards.
     */
    override fun getEducationalLevelPopulationSummary(): ResponseEntity<ApiResponse<List<EducationalLevelPopulationSummaryResponse>>> {
        logger.debug("Request received to get educational level population summary")

        val summaryData = service.getEducationalLevelPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.summaryByEducationalLevel.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all educational levels.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.education.summaryByWard.success")
            )
        )
    }
}
