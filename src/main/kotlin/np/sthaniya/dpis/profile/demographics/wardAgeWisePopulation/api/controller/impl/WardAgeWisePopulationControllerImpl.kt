package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.api.controller.WardAgeWisePopulationController
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.CreateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.UpdateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.WardAgeWisePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.AgeGroupPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeGenderSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeWisePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.service.WardAgeWisePopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardAgeWisePopulationController interface.
 *
 * This controller provides REST endpoints for managing ward-age-wise population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-age-wise population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardAgeWisePopulationControllerImpl(
    private val service: WardAgeWisePopulationService,
    private val i18nMessageService: I18nMessageService
) : WardAgeWisePopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-age-wise population data.
     */
    override fun createWardAgeWisePopulation(
        createDto: CreateWardAgeWisePopulationDto
    ): ResponseEntity<ApiResponse<WardAgeWisePopulationResponse>> {
        logger.info("Request received to create ward-age-wise population data: $createDto")

        val createdData = service.createWardAgeWisePopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.age.create.success")
                )
            )
    }

    /**
     * Updates existing ward-age-wise population data.
     */
    override fun updateWardAgeWisePopulation(
        id: UUID,
        updateDto: UpdateWardAgeWisePopulationDto
    ): ResponseEntity<ApiResponse<WardAgeWisePopulationResponse>> {
        logger.info("Request received to update ward-age-wise population data with ID $id: $updateDto")

        val updatedData = service.updateWardAgeWisePopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.age.update.success")
            )
        )
    }

    /**
     * Deletes ward-age-wise population data.
     */
    override fun deleteWardAgeWisePopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-age-wise population data with ID: $id")

        service.deleteWardAgeWisePopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.age.delete.success")
            )
        )
    }

    /**
     * Gets ward-age-wise population data by ID.
     */
    override fun getWardAgeWisePopulationById(id: UUID): ResponseEntity<ApiResponse<WardAgeWisePopulationResponse>> {
        logger.debug("Request received to get ward-age-wise population data with ID: $id")

        val data = service.getWardAgeWisePopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.get.success")
            )
        )
    }

    /**
     * Gets all ward-age-wise population data with optional filtering.
     */
    override fun getAllWardAgeWisePopulation(
        filter: WardAgeWisePopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardAgeWisePopulationResponse>>> {
        logger.debug("Request received to get all ward-age-wise population data with filter: $filter")

        val data = service.getAllWardAgeWisePopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.getAll.success")
            )
        )
    }

    /**
     * Gets ward-age-wise population data for a specific ward.
     */
    override fun getWardAgeWisePopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardAgeWisePopulationResponse>>> {
        logger.debug("Request received to get ward-age-wise population data for ward: $wardNumber")

        val data = service.getWardAgeWisePopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-age-wise population data for a specific age group.
     */
    override fun getWardAgeWisePopulationByAgeGroup(
        ageGroup: AgeGroup
    ): ResponseEntity<ApiResponse<List<WardAgeWisePopulationResponse>>> {
        logger.debug("Request received to get ward-age-wise population data for age group: $ageGroup")

        val data = service.getWardAgeWisePopulationByAgeGroup(ageGroup)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.getByAgeGroup.success")
            )
        )
    }

    /**
     * Gets ward-age-wise population data for a specific gender.
     */
    override fun getWardAgeWisePopulationByGender(
        gender: Gender
    ): ResponseEntity<ApiResponse<List<WardAgeWisePopulationResponse>>> {
        logger.debug("Request received to get ward-age-wise population data for gender: $gender")

        val data = service.getWardAgeWisePopulationByGender(gender)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.age.getByGender.success")
            )
        )
    }

    /**
     * Gets summary of population by age group across all wards.
     */
    override fun getAgeGroupPopulationSummary(): ResponseEntity<ApiResponse<List<AgeGroupPopulationSummaryResponse>>> {
        logger.debug("Request received to get age group population summary")

        val summaryData = service.getAgeGroupPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.age.summaryByAgeGroup.success")
            )
        )
    }

    /**
     * Gets summary of population by gender across all wards.
     */
    override fun getGenderPopulationSummary(): ResponseEntity<ApiResponse<List<GenderPopulationSummaryResponse>>> {
        logger.debug("Request received to get gender population summary")

        val summaryData = service.getGenderPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.age.summaryByGender.success")
            )
        )
    }

    /**
     * Gets detailed summary of population by ward, age group, and gender.
     */
    override fun getWardAgeGenderSummary(): ResponseEntity<ApiResponse<List<WardAgeGenderSummaryResponse>>> {
        logger.debug("Request received to get ward-age-gender population summary")

        val summaryData = service.getWardAgeGenderSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.age.detailedSummary.success")
            )
        )
    }
}
