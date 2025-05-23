package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.api.controller.WardAgeWiseMaritalStatusController
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.CreateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.UpdateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.WardAgeWiseMaritalStatusFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.AgeGroupSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.MaritalStatusSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.WardAgeWiseMaritalStatusResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.service.WardAgeWiseMaritalStatusService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardAgeWiseMaritalStatusController interface.
 *
 * This controller provides REST endpoints for managing ward age-wise marital status data.
 * Endpoints handle CRUD operations and statistics retrieval for demographic data.
 *
 * @property service Service for managing ward age-wise marital status data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardAgeWiseMaritalStatusControllerImpl(
    private val service: WardAgeWiseMaritalStatusService,
    private val i18nMessageService: I18nMessageService
) : WardAgeWiseMaritalStatusController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward age-wise marital status data.
     */
    override fun createWardAgeWiseMaritalStatus(
        createDto: CreateWardAgeWiseMaritalStatusDto
    ): ResponseEntity<ApiResponse<WardAgeWiseMaritalStatusResponse>> {
        logger.info("Request received to create ward age-wise marital status data: $createDto")

        val createdData = service.createWardAgeWiseMaritalStatus(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("demographics.ward.marital.create.success")
                )
            )
    }

    /**
     * Updates existing ward age-wise marital status data.
     */
    override fun updateWardAgeWiseMaritalStatus(
        id: UUID,
        updateDto: UpdateWardAgeWiseMaritalStatusDto
    ): ResponseEntity<ApiResponse<WardAgeWiseMaritalStatusResponse>> {
        logger.info("Request received to update ward age-wise marital status data with ID $id: $updateDto")

        val updatedData = service.updateWardAgeWiseMaritalStatus(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("demographics.ward.marital.update.success")
            )
        )
    }

    /**
     * Deletes ward age-wise marital status data.
     */
    override fun deleteWardAgeWiseMaritalStatus(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward age-wise marital status data with ID: $id")

        service.deleteWardAgeWiseMaritalStatus(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("demographics.ward.marital.delete.success")
            )
        )
    }

    /**
     * Gets ward age-wise marital status data by ID.
     */
    override fun getWardAgeWiseMaritalStatusById(id: UUID): ResponseEntity<ApiResponse<WardAgeWiseMaritalStatusResponse>> {
        logger.debug("Request received to get ward age-wise marital status data with ID: $id")

        val data = service.getWardAgeWiseMaritalStatusById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.marital.get.success")
            )
        )
    }

    /**
     * Gets all ward age-wise marital status data with optional filtering.
     */
    override fun getAllWardAgeWiseMaritalStatus(
        filter: WardAgeWiseMaritalStatusFilterDto?
    ): ResponseEntity<ApiResponse<List<WardAgeWiseMaritalStatusResponse>>> {
        logger.debug("Request received to get all ward age-wise marital status data with filter: $filter")

        val data = service.getAllWardAgeWiseMaritalStatus(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.marital.getAll.success")
            )
        )
    }

    /**
     * Gets ward age-wise marital status data for a specific ward.
     */
    override fun getWardAgeWiseMaritalStatusByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardAgeWiseMaritalStatusResponse>>> {
        logger.debug("Request received to get ward age-wise marital status data for ward: $wardNumber")

        val data = service.getWardAgeWiseMaritalStatusByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.marital.getByWard.success")
            )
        )
    }

    /**
     * Gets ward age-wise marital status data for a specific age group.
     */
    override fun getWardAgeWiseMaritalStatusByAgeGroup(
        ageGroup: MaritalAgeGroup
    ): ResponseEntity<ApiResponse<List<WardAgeWiseMaritalStatusResponse>>> {
        logger.debug("Request received to get ward age-wise marital status data for age group: $ageGroup")

        val data = service.getWardAgeWiseMaritalStatusByAgeGroup(ageGroup)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.marital.getByAgeGroup.success")
            )
        )
    }

    /**
     * Gets ward age-wise marital status data for a specific marital status.
     */
    override fun getWardAgeWiseMaritalStatusByMaritalStatus(
        maritalStatus: MaritalStatusGroup
    ): ResponseEntity<ApiResponse<List<WardAgeWiseMaritalStatusResponse>>> {
        logger.debug("Request received to get ward age-wise marital status data for marital status: $maritalStatus")

        val data = service.getWardAgeWiseMaritalStatusByMaritalStatus(maritalStatus)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("demographics.ward.marital.getByMaritalStatus.success")
            )
        )
    }

    /**
     * Gets summary of marital status population across all wards and age groups.
     */
    override fun getMaritalStatusSummary(): ResponseEntity<ApiResponse<List<MaritalStatusSummaryResponse>>> {
        logger.debug("Request received to get marital status population summary")

        val summaryData = service.getMaritalStatusSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.marital.summaryByMaritalStatus.success")
            )
        )
    }

    /**
     * Gets summary of age group population across all wards and marital statuses.
     */
    override fun getAgeGroupSummary(): ResponseEntity<ApiResponse<List<AgeGroupSummaryResponse>>> {
        logger.debug("Request received to get age group population summary")

        val summaryData = service.getAgeGroupSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("demographics.ward.marital.summaryByAgeGroup.success")
            )
        )
    }
}
