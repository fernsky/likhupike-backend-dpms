package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.api.controller.WardAgeGenderWiseMarriedAgeController
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.CreateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.UpdateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.WardAgeGenderWiseMarriedAgeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardAgeGenderWiseMarriedAgeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.service.WardAgeGenderWiseMarriedAgeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardAgeGenderWiseMarriedAgeController interface.
 *
 * This controller provides REST endpoints for managing ward-age-gender-wise married age data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-age-gender-wise married age data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardAgeGenderWiseMarriedAgeControllerImpl(
    private val service: WardAgeGenderWiseMarriedAgeService,
    private val i18nMessageService: I18nMessageService
) : WardAgeGenderWiseMarriedAgeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-age-gender-wise married age data.
     */
    override fun createWardAgeGenderWiseMarriedAge(
        createDto: CreateWardAgeGenderWiseMarriedAgeDto
    ): ResponseEntity<ApiResponse<WardAgeGenderWiseMarriedAgeResponse>> {
        logger.info("Request received to create ward-age-gender-wise married age data: $createDto")

        val createdData = service.createWardAgeGenderWiseMarriedAge(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.age.gender.married.create.success")
                )
            )
    }

    /**
     * Updates existing ward-age-gender-wise married age data.
     */
    override fun updateWardAgeGenderWiseMarriedAge(
        id: UUID,
        updateDto: UpdateWardAgeGenderWiseMarriedAgeDto
    ): ResponseEntity<ApiResponse<WardAgeGenderWiseMarriedAgeResponse>> {
        logger.info("Request received to update ward-age-gender-wise married age data with ID $id: $updateDto")

        val updatedData = service.updateWardAgeGenderWiseMarriedAge(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.update.success")
            )
        )
    }

    /**
     * Deletes ward-age-gender-wise married age data.
     */
    override fun deleteWardAgeGenderWiseMarriedAge(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-age-gender-wise married age data with ID: $id")

        service.deleteWardAgeGenderWiseMarriedAge(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.delete.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise married age data by ID.
     */
    override fun getWardAgeGenderWiseMarriedAgeById(id: UUID): ResponseEntity<ApiResponse<WardAgeGenderWiseMarriedAgeResponse>> {
        logger.debug("Request received to get ward-age-gender-wise married age data with ID: $id")

        val data = service.getWardAgeGenderWiseMarriedAgeById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.get.success")
            )
        )
    }

    /**
     * Gets all ward-age-gender-wise married age data with optional filtering.
     */
    override fun getAllWardAgeGenderWiseMarriedAge(
        filter: WardAgeGenderWiseMarriedAgeFilterDto?
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>> {
        logger.debug("Request received to get all ward-age-gender-wise married age data with filter: $filter")

        val data = service.getAllWardAgeGenderWiseMarriedAge(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.getAll.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise married age data for a specific ward.
     */
    override fun getWardAgeGenderWiseMarriedAgeByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise married age data for ward: $wardNumber")

        val data = service.getWardAgeGenderWiseMarriedAgeByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise married age data for a specific age group.
     */
    override fun getWardAgeGenderWiseMarriedAgeByAgeGroup(
        ageGroup: MarriedAgeGroup
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise married age data for age group: $ageGroup")

        val data = service.getWardAgeGenderWiseMarriedAgeByAgeGroup(ageGroup)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.getByAgeGroup.success")
            )
        )
    }

    /**
     * Gets ward-age-gender-wise married age data for a specific gender.
     */
    override fun getWardAgeGenderWiseMarriedAgeByGender(
        gender: Gender
    ): ResponseEntity<ApiResponse<List<WardAgeGenderWiseMarriedAgeResponse>>> {
        logger.debug("Request received to get ward-age-gender-wise married age data for gender: $gender")

        val data = service.getWardAgeGenderWiseMarriedAgeByGender(gender)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.getByGender.success")
            )
        )
    }

    /**
     * Gets summary of age group and gender population across all wards.
     */
    override fun getAgeGroupGenderPopulationSummary(): ResponseEntity<ApiResponse<List<AgeGroupGenderPopulationSummaryResponse>>> {
        logger.debug("Request received to get age group and gender population summary")

        val summaryData = service.getAgeGroupGenderPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.summaryByAgeGroupGender.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all age groups and genders.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.age.gender.married.summaryByWard.success")
            )
        )
    }
}
