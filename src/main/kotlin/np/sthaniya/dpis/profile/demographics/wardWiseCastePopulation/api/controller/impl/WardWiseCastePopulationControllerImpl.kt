package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.api.controller.WardWiseCastePopulationController
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.CreateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.UpdateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.WardWiseCastePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.CastePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardWiseCastePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.service.WardWiseCastePopulationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseCastePopulationController interface.
 *
 * This controller provides REST endpoints for managing ward-wise caste population data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward-wise caste population data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseCastePopulationControllerImpl(
    private val service: WardWiseCastePopulationService,
    private val i18nMessageService: I18nMessageService
) : WardWiseCastePopulationController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise caste population data.
     */
    override fun createWardWiseCastePopulation(
        createDto: CreateWardWiseCastePopulationDto
    ): ResponseEntity<ApiResponse<WardWiseCastePopulationResponse>> {
        logger.info("Request received to create ward-wise caste population data: $createDto")

        val createdData = service.createWardWiseCastePopulation(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.caste.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise caste population data.
     */
    override fun updateWardWiseCastePopulation(
        id: UUID,
        updateDto: UpdateWardWiseCastePopulationDto
    ): ResponseEntity<ApiResponse<WardWiseCastePopulationResponse>> {
        logger.info("Request received to update ward-wise caste population data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseCastePopulation(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.caste.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise caste population data.
     */
    override fun deleteWardWiseCastePopulation(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise caste population data with ID: $id")

        service.deleteWardWiseCastePopulation(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.caste.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise caste population data by ID.
     */
    override fun getWardWiseCastePopulationById(id: UUID): ResponseEntity<ApiResponse<WardWiseCastePopulationResponse>> {
        logger.debug("Request received to get ward-wise caste population data with ID: $id")

        val data = service.getWardWiseCastePopulationById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.caste.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise caste population data with optional filtering.
     */
    override fun getAllWardWiseCastePopulation(
        filter: WardWiseCastePopulationFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseCastePopulationResponse>>> {
        logger.debug("Request received to get all ward-wise caste population data with filter: $filter")

        val data = service.getAllWardWiseCastePopulation(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.caste.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise caste population data for a specific ward.
     */
    override fun getWardWiseCastePopulationByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseCastePopulationResponse>>> {
        logger.debug("Request received to get ward-wise caste population data for ward: $wardNumber")

        val data = service.getWardWiseCastePopulationByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.caste.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise caste population data for a specific caste type.
     */
    override fun getWardWiseCastePopulationByCaste(
        casteType: CasteType
    ): ResponseEntity<ApiResponse<List<WardWiseCastePopulationResponse>>> {
        logger.debug("Request received to get ward-wise caste population data for caste: $casteType")

        val data = service.getWardWiseCastePopulationByCaste(casteType)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.caste.getByCaste.success")
            )
        )
    }

    /**
     * Gets summary of caste population across all wards.
     */
    override fun getCastePopulationSummary(): ResponseEntity<ApiResponse<List<CastePopulationSummaryResponse>>> {
        logger.debug("Request received to get caste population summary")

        val summaryData = service.getCastePopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.caste.summaryByCaste.success")
            )
        )
    }

    /**
     * Gets summary of total population by ward across all castes.
     */
    override fun getWardPopulationSummary(): ResponseEntity<ApiResponse<List<WardPopulationSummaryResponse>>> {
        logger.debug("Request received to get ward population summary")

        val summaryData = service.getWardPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.caste.summaryByWard.success")
            )
        )
    }
}
