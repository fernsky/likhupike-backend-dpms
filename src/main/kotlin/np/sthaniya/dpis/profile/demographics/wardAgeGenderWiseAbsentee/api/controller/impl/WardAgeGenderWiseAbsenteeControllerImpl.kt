package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.api.controller.WardAgeGenderWiseAbsenteeController
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.CreateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.UpdateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.WardAgeGenderWiseAbsenteeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAbsenteePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAgeGenderWiseAbsenteeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.service.WardAgeGenderWiseAbsenteeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardAgeGenderWiseAbsenteeController interface.
 *
 * This controller provides REST endpoints for managing ward-age-gender-wise absentee population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-age-gender-wise absentee data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardAgeGenderWiseAbsenteeControllerImpl(
    private val service: WardAgeGenderWiseAbsenteeService,
    private val i18nMessageService: I18nMessageService
) : WardAgeGenderWiseAbsenteeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-age-gender-wise absentee data.
     */
    override fun createWardAgeGenderWiseAbsentee(
        createDto: CreateWardAgeGenderWiseAbsenteeDto
    ): ResponseEntity<ApiResponse<WardAgeGenderWiseAbsenteeResponse>> {
        logger.info("Request received to create ward-age-gender-wise absentee data: $createDto")

        val createdData = service.createWardAgeGenderWiseAbsentee(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.absentee.create.success")
                )
            )
    }

    /**
     * Updates existing ward-age-gender-wise absentee data.
     */
    override fun updateWardAgeGenderWiseAbsentee(
        id: UUID,
        updateDto: UpdateWardAgeGenderWiseAbsenteeDto
    ): ResponseEntity<ApiResponse<WardAgeGenderWiseAbsenteeResponse>> {
        logger.info("Request received to update ward-age-gender-wise absentee data with ID $id: $updateDto")

        val updatedData = service.updateWardAgeGenderWiseAbsentee(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.update.success")
            )
        )
    }

    /**
     * Deletes ward-age-gender-wise absentee data.
     */
    override fun deleteWardAgeGenderWiseAbsentee(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-age-gender-wise absentee data with ID: $id")

        service.deleteWardAgeGenderWiseAbsentee(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.absentee.delete.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise absentee data by ID.
     */
    override fun getWardAgeGenderWiseAbsenteeById(id: UUID): ResponseEntity<ApiResponse<WardAgeGenderWiseAbsenteeResponse>> {
        logger.debug("Request received to get ward-age-gender-wise absentee data with ID: $id")

        val data = service.getWardAgeGenderWiseAbsenteeById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.get.success")
            )
        )
    }

    /**
     * Gets all ward-age-gender-wise absentee data with optional filtering.
     */
    override fun getAllWardAgeGenderWiseAbsentee(
        filter: WardAgeGenderWiseAbsenteeFilterDto?
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseAbsenteeResponse>>> {
        logger.debug("Request received to get all ward-age-gender-wise absentee data with filter: $filter")

        val data = service.getAllWardAgeGenderWiseAbsentee(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.getAll.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise absentee data for a specific ward.
     */
    override fun getWardAgeGenderWiseAbsenteeByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseAbsenteeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise absentee data for ward: $wardNumber")

        val data = service.getWardAgeGenderWiseAbsenteeByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise absentee data for a specific age group.
     */
    override fun getWardAgeGenderWiseAbsenteeByAgeGroup(
        ageGroup: AbsenteeAgeGroup
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseAbsenteeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise absentee data for age group: $ageGroup")

        val data = service.getWardAgeGenderWiseAbsenteeByAgeGroup(ageGroup)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.getByAgeGroup.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise absentee data for a specific gender.
     */
    override fun getWardAgeGenderWiseAbsenteeByGender(
        gender: Gender
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseAbsenteeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise absentee data for gender: $gender")

        val data = service.getWardAgeGenderWiseAbsenteeByGender(gender)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.absentee.getByGender.success")
            )
        )
    }

    /**
     * Gets summary of absentee population by age group and gender across all wards.
     */
    override fun getAgeGroupGenderPopulationSummary(): ResponseEntity<ApiResponse<List<AgeGroupGenderPopulationSummaryResponse>>> {
        logger.debug("Request received to get age group and gender population summary")

        val summaryData = service.getAgeGroupGenderPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.summaryByAgeGender.success")
            )
        )
    }

    /**
     * Gets summary of total absentee population by ward across all age groups and genders.
     */
    override fun getWardAbsenteePopulationSummary(): ResponseEntity<ApiResponse<List<WardAbsenteePopulationSummaryResponse>>> {
        logger.debug("Request received to get ward absentee population summary")

        val summaryData = service.getWardAbsenteePopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.absentee.summaryByWard.success")
            )
        )
    }
}
