package np.sthaniya.dpis.citizen.api.controller.impl

import np.sthaniya.dpis.citizen.api.controller.CitizenManagementController
import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.service.CitizenManagementService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

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
}
