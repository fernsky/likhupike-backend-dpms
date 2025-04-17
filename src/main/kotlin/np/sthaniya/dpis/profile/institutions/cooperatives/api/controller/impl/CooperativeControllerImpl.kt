package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the CooperativeController interface.
 * Provides endpoints for managing cooperatives.
 */
@RestController
class CooperativeControllerImpl(
    private val cooperativeService: CooperativeService
) : CooperativeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createCooperative(
        createDto: CreateCooperativeDto
    ): ResponseEntity<ApiResponse<CooperativeResponse>> {
        logger.info("Creating new cooperative with code: ${createDto.code}")
        
        val createdCooperative = cooperativeService.createCooperative(createDto)
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(createdCooperative, "Cooperative created successfully"))
    }

    override fun updateCooperative(
        id: UUID,
        updateDto: UpdateCooperativeDto
    ): ResponseEntity<ApiResponse<CooperativeResponse>> {
        logger.info("Updating cooperative with ID: $id")
        
        val updatedCooperative = cooperativeService.updateCooperative(id, updateDto)
        
        return ResponseEntity.ok(ApiResponse.success(updatedCooperative, "Cooperative updated successfully"))
    }

    override fun getCooperativeById(id: UUID): ResponseEntity<ApiResponse<CooperativeResponse>> {
        logger.debug("Fetching cooperative with ID: $id")
        
        val cooperative = cooperativeService.getCooperativeById(id)
        
        return ResponseEntity.ok(ApiResponse.success(cooperative))
    }

    override fun getCooperativeByCode(code: String): ResponseEntity<ApiResponse<CooperativeResponse>> {
        logger.debug("Fetching cooperative with code: $code")
        
        val cooperative = cooperativeService.getCooperativeByCode(code)
        
        return ResponseEntity.ok(ApiResponse.success(cooperative))
    }

    override fun deleteCooperative(id: UUID): ResponseEntity<ApiResponse<Void>> {
        logger.info("Deleting cooperative with ID: $id")
        
        cooperativeService.deleteCooperative(id)
        
        return ResponseEntity.ok(ApiResponse.success(null, "Cooperative deleted successfully"))
    }

    override fun getAllCooperatives(pageable: Pageable): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Fetching all cooperatives with pagination")
        
        val cooperatives = cooperativeService.getAllCooperatives(pageable)
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun changeCooperativeStatus(
        id: UUID,
        status: CooperativeStatus
    ): ResponseEntity<ApiResponse<CooperativeResponse>> {
        logger.info("Changing status of cooperative $id to $status")
        
        val updatedCooperative = cooperativeService.changeCooperativeStatus(id, status)
        
        return ResponseEntity.ok(ApiResponse.success(
            updatedCooperative, 
            "Cooperative status updated to $status"
        ))
    }
}
