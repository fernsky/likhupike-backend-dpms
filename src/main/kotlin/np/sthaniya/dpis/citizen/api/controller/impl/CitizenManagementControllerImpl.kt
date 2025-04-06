package np.sthaniya.dpis.citizen.api.controller.impl

import np.sthaniya.dpis.citizen.api.controller.CitizenManagementController
import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.service.CitizenManagementService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Implementation of the CitizenManagementController interface.
 * 
 * Provides the actual implementation for endpoint handling, delegating business
 * logic to the appropriate services.
 */
@RestController
class CitizenManagementControllerImpl(
    private val citizenManagementService: CitizenManagementService,
    private val i18nMessageService: I18nMessageService
) : CitizenManagementController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates a new citizen record in the system.
     * 
     * @param createCitizenDto The data for creating a new citizen
     * @return HTTP 201 CREATED with the created citizen data
     */
    override fun createCitizen(createCitizenDto: CreateCitizenDto): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Creating new citizen with name: ${createCitizenDto.name}")
        
        val createdCitizen = citizenManagementService.createCitizen(createCitizenDto)
        
        logger.info("Successfully created citizen with ID: ${createdCitizen.id}")
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = createdCitizen,
                    message = i18nMessageService.getMessage("citizen.create.success")
                )
            )
    }

    /**
     * Updates an existing citizen record in the system.
     * 
     * @param id The unique identifier of the citizen to update
     * @param updateCitizenDto The data for updating the citizen
     * @return HTTP 200 OK with the updated citizen data
     */
    override fun updateCitizen(id: UUID, updateCitizenDto: UpdateCitizenDto): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Updating citizen with ID: $id")
        
        val updatedCitizen = citizenManagementService.updateCitizen(id, updateCitizenDto)
        
        logger.info("Successfully updated citizen with ID: $id")
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    data = updatedCitizen,
                    message = i18nMessageService.getMessage("citizen.update.success")
                )
            )
    }

    /**
     * Retrieves a citizen record by ID.
     * 
     * @param id The unique identifier of the citizen to retrieve
     * @return HTTP 200 OK with the citizen data
     */
    override fun getCitizenById(id: UUID): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Retrieving citizen with ID: $id")
        
        val citizen = citizenManagementService.getCitizenById(id)
        
        logger.debug("Successfully retrieved citizen with ID: $id")
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    data = citizen,
                    message = i18nMessageService.getMessage("citizen.get.success")
                )
            )
    }

    /**
     * Approves a citizen record.
     * 
     * @param id The unique identifier of the citizen to approve
     * @param currentUserId ID of the administrator approving the citizen
     * @return HTTP 200 OK with the approved citizen data
     */
    override fun approveCitizen(id: UUID, currentUserId: UUID): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Approving citizen with ID: $id by user: $currentUserId")
        
        val approvedCitizen = citizenManagementService.approveCitizen(id, currentUserId)
        
        logger.info("Successfully approved citizen with ID: $id")
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    data = approvedCitizen,
                    message = i18nMessageService.getMessage("citizen.approve.success")
                )
            )
    }

    /**
     * Deletes a citizen record.
     * 
     * @param id The unique identifier of the citizen to delete
     * @param currentUserId ID of the administrator deleting the citizen
     * @return HTTP 200 OK with the deleted citizen data
     */
    override fun deleteCitizen(id: UUID, currentUserId: UUID): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Deleting citizen with ID: $id by user: $currentUserId")
        
        val deletedCitizen = citizenManagementService.deleteCitizen(id, currentUserId)
        
        logger.info("Successfully deleted citizen with ID: $id")
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    data = deletedCitizen,
                    message = i18nMessageService.getMessage("citizen.delete.success")
                )
            )
    }
}
