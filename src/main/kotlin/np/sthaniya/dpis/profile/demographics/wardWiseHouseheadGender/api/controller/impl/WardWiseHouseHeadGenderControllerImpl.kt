package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.api.controller.WardWiseHouseHeadGenderController
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.CreateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.UpdateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.WardWiseHouseHeadGenderFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardWiseHouseHeadGenderResponse
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.service.WardWiseHouseHeadGenderService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseHouseHeadGenderController interface.
 *
 * This controller provides REST endpoints for managing ward-wise house head gender data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-wise house head gender data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseHouseHeadGenderControllerImpl(
    private val service: WardWiseHouseHeadGenderService,
    private val i18nMessageService: I18nMessageService
) : WardWiseHouseHeadGenderController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise house head gender data.
     */
    override fun createWardWiseHouseHeadGender(
        createDto: CreateWardWiseHouseHeadGenderDto
    ): ResponseEntity<ApiResponse<WardWiseHouseHeadGenderResponse>> {
        logger.info("Request received to create ward-wise house head gender data: $createDto")

        val createdData = service.createWardWiseHouseHeadGender(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.househead.gender.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise house head gender data.
     */
    override fun updateWardWiseHouseHeadGender(
        id: UUID,
        updateDto: UpdateWardWiseHouseHeadGenderDto
    ): ResponseEntity<ApiResponse<WardWiseHouseHeadGenderResponse>> {
        logger.info("Request received to update ward-wise house head gender data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseHouseHeadGender(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise house head gender data.
     */
    override fun deleteWardWiseHouseHeadGender(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise house head gender data with ID: $id")

        service.deleteWardWiseHouseHeadGender(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise house head gender data by ID.
     */
    override fun getWardWiseHouseHeadGenderById(id: UUID): ResponseEntity<ApiResponse<WardWiseHouseHeadGenderResponse>> {
        logger.debug("Request received to get ward-wise house head gender data with ID: $id")

        val data = service.getWardWiseHouseHeadGenderById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise house head gender data with optional filtering.
     */
    override fun getAllWardWiseHouseHeadGender(
        filter: WardWiseHouseHeadGenderFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseHouseHeadGenderResponse>>> {
        logger.debug("Request received to get all ward-wise house head gender data with filter: $filter")

        val data = service.getAllWardWiseHouseHeadGender(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise house head gender data for a specific ward.
     */
    override fun getWardWiseHouseHeadGenderByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseHouseHeadGenderResponse>>> {
        logger.debug("Request received to get ward-wise house head gender data for ward: $wardNumber")

        val data = service.getWardWiseHouseHeadGenderByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise house head gender data for a specific gender.
     */
    override fun getWardWiseHouseHeadGenderByGender(
        gender: Gender
    ): ResponseEntity<ApiResponse<List<WardWiseHouseHeadGenderResponse>>> {
        logger.debug("Request received to get ward-wise house head gender data for gender: $gender")

        val data = service.getWardWiseHouseHeadGenderByGender(gender)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.getByGender.success")
            )
        )
    }

    /**
     * Gets summary of gender distribution across all wards.
     */
    override fun getGenderPopulationSummary(): ResponseEntity<ApiResponse<List<GenderPopulationSummaryResponse>>> {
        logger.debug("Request received to get gender population summary")

        val summaryData = service.getGenderPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.summaryByGender.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all genders.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.househead.gender.summaryByWard.success")
            )
        )
    }
}
