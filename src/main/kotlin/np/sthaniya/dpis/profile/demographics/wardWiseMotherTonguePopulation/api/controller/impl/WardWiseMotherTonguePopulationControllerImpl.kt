package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.api.controller.WardWiseMotherTonguePopulationController
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.CreateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.UpdateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.WardWiseMotherTonguePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.LanguagePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardWiseMotherTonguePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.service.WardWiseMotherTonguePopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseMotherTonguePopulationController interface.
 *
 * This controller provides REST endpoints for managing ward-wise mother tongue population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-wise mother tongue population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseMotherTonguePopulationControllerImpl(
    private val service: WardWiseMotherTonguePopulationService,
    private val i18nMessageService: I18nMessageService
) : WardWiseMotherTonguePopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise mother tongue population data.
     */
    override fun createWardWiseMotherTonguePopulation(
        createDto: CreateWardWiseMotherTonguePopulationDto
    ): ResponseEntity<ApiResponse<WardWiseMotherTonguePopulationResponse>> {
        logger.info("Request received to create ward-wise mother tongue population data: $createDto")

        val createdData = service.createWardWiseMotherTonguePopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.mothertongue.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise mother tongue population data.
     */
    override fun updateWardWiseMotherTonguePopulation(
        id: UUID,
        updateDto: UpdateWardWiseMotherTonguePopulationDto
    ): ResponseEntity<ApiResponse<WardWiseMotherTonguePopulationResponse>> {
        logger.info("Request received to update ward-wise mother tongue population data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseMotherTonguePopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise mother tongue population data.
     */
    override fun deleteWardWiseMotherTonguePopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise mother tongue population data with ID: $id")

        service.deleteWardWiseMotherTonguePopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise mother tongue population data by ID.
     */
    override fun getWardWiseMotherTonguePopulationById(id: UUID): ResponseEntity<ApiResponse<WardWiseMotherTonguePopulationResponse>> {
        logger.debug("Request received to get ward-wise mother tongue population data with ID: $id")

        val data = service.getWardWiseMotherTonguePopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise mother tongue population data with optional filtering.
     */
    override fun getAllWardWiseMotherTonguePopulation(
        filter: WardWiseMotherTonguePopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseMotherTonguePopulationResponse>>> {
        logger.debug("Request received to get all ward-wise mother tongue population data with filter: $filter")

        val data = service.getAllWardWiseMotherTonguePopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise mother tongue population data for a specific ward.
     */
    override fun getWardWiseMotherTonguePopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseMotherTonguePopulationResponse>>> {
        logger.debug("Request received to get ward-wise mother tongue population data for ward: $wardNumber")

        val data = service.getWardWiseMotherTonguePopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise mother tongue population data for a specific language type.
     */
    override fun getWardWiseMotherTonguePopulationByLanguage(
        languageType: LanguageType
    ): ResponseEntity<ApiResponse<List<WardWiseMotherTonguePopulationResponse>>> {
        logger.debug("Request received to get ward-wise mother tongue population data for language: $languageType")

        val data = service.getWardWiseMotherTonguePopulationByLanguage(languageType)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.getByLanguage.success")
            )
        )
    }

    /**
     * Gets summary of language population across all wards.
     */
    override fun getLanguagePopulationSummary(): ResponseEntity<ApiResponse<List<LanguagePopulationSummaryResponse>>> {
        logger.debug("Request received to get language population summary")

        val summaryData = service.getLanguagePopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.summaryByLanguage.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all languages.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.mothertongue.summaryByWard.success")
            )
        )
    }
}
