package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.api.controller.WardWiseMajorSkillsController
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.CreateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.UpdateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.WardWiseMajorSkillsFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.SkillPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardSkillsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardWiseMajorSkillsResponse
import np.sthaniya.dpis.profile.economics.common.model.SkillType
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.service.WardWiseMajorSkillsService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the WardWiseMajorSkillsController interface.
 *
 * This controller provides REST endpoints for managing ward-wise major skills data.
 * Endpoints handle CRUD operations and statistics retrieval for economic skills data.
 *
 * @property service Service for managing ward-wise major skills data
 * @property i18nMessageService Service for internationalized messages
 */
@RestController
class WardWiseMajorSkillsControllerImpl(
    private val service: WardWiseMajorSkillsService,
    private val i18nMessageService: I18nMessageService
) : WardWiseMajorSkillsController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new ward-wise major skills data.
     */
    override fun createWardWiseMajorSkills(
        createDto: CreateWardWiseMajorSkillsDto
    ): ResponseEntity<ApiResponse<WardWiseMajorSkillsResponse>> {
        logger.info("Request received to create ward-wise major skills data: $createDto")

        val createdData = service.createWardWiseMajorSkills(createDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdData,
                    message = i18nMessageService.getMessage("economics.ward.skills.create.success")
                )
            )
    }

    /**
     * Updates existing ward-wise major skills data.
     */
    override fun updateWardWiseMajorSkills(
        id: UUID,
        updateDto: UpdateWardWiseMajorSkillsDto
    ): ResponseEntity<ApiResponse<WardWiseMajorSkillsResponse>> {
        logger.info("Request received to update ward-wise major skills data with ID $id: $updateDto")

        val updatedData = service.updateWardWiseMajorSkills(id, updateDto)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedData,
                message = i18nMessageService.getMessage("economics.ward.skills.update.success")
            )
        )
    }

    /**
     * Deletes ward-wise major skills data.
     */
    override fun deleteWardWiseMajorSkills(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Request received to delete ward-wise major skills data with ID: $id")

        service.deleteWardWiseMajorSkills(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("economics.ward.skills.delete.success")
            )
        )
    }

    /**
     * Gets ward-wise major skills data by ID.
     */
    override fun getWardWiseMajorSkillsById(id: UUID): ResponseEntity<ApiResponse<WardWiseMajorSkillsResponse>> {
        logger.debug("Request received to get ward-wise major skills data with ID: $id")

        val data = service.getWardWiseMajorSkillsById(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.skills.get.success")
            )
        )
    }

    /**
     * Gets all ward-wise major skills data with optional filtering.
     */
    override fun getAllWardWiseMajorSkills(
        filter: WardWiseMajorSkillsFilterDto?
    ): ResponseEntity<ApiResponse<List<WardWiseMajorSkillsResponse>>> {
        logger.debug("Request received to get all ward-wise major skills data with filter: $filter")

        val data = service.getAllWardWiseMajorSkills(filter)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.skills.getAll.success")
            )
        )
    }

    /**
     * Gets ward-wise major skills data for a specific ward.
     */
    override fun getWardWiseMajorSkillsByWard(
        wardNumber: Int
    ): ResponseEntity<ApiResponse<List<WardWiseMajorSkillsResponse>>> {
        logger.debug("Request received to get ward-wise major skills data for ward: $wardNumber")

        val data = service.getWardWiseMajorSkillsByWard(wardNumber)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.skills.getByWard.success")
            )
        )
    }

    /**
     * Gets ward-wise major skills data for a specific skill type.
     */
    override fun getWardWiseMajorSkillsBySkill(
        skill: SkillType
    ): ResponseEntity<ApiResponse<List<WardWiseMajorSkillsResponse>>> {
        logger.debug("Request received to get ward-wise major skills data for skill: $skill")

        val data = service.getWardWiseMajorSkillsBySkill(skill)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = data,
                message = i18nMessageService.getMessage("economics.ward.skills.getBySkill.success")
            )
        )
    }

    /**
     * Gets summary of skill population across all wards.
     */
    override fun getSkillPopulationSummary(): ResponseEntity<ApiResponse<List<SkillPopulationSummaryResponse>>> {
        logger.debug("Request received to get skill population summary")

        val summaryData = service.getSkillPopulationSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.skills.summaryBySkill.success")
            )
        )
    }

    /**
     * Gets summary of total population with skills by ward.
     */
    override fun getWardSkillsSummary(): ResponseEntity<ApiResponse<List<WardSkillsSummaryResponse>>> {
        logger.debug("Request received to get ward skills summary")

        val summaryData = service.getWardSkillsSummary()

        return ResponseEntity.ok(
            ApiResponse.success(
                data = summaryData,
                message = i18nMessageService.getMessage("economics.ward.skills.summaryByWard.success")
            )
        )
    }
}
