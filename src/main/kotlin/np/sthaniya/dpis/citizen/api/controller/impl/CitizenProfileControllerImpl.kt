package np.sthaniya.dpis.citizen.api.controller.impl

import np.sthaniya.dpis.citizen.api.controller.CitizenProfileController
import np.sthaniya.dpis.citizen.dto.profile.ChangePasswordDto
import np.sthaniya.dpis.citizen.dto.profile.RegisterCitizenDto
import np.sthaniya.dpis.citizen.dto.profile.UpdateProfileDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.citizen.service.CitizenProfileService
import np.sthaniya.dpis.common.dto.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Implementation of the [CitizenProfileController] interface for citizen self-registration and profile management.
 */
@RestController
class CitizenProfileControllerImpl(
    private val citizenProfileService: CitizenProfileService
) : CitizenProfileController {

    private val logger = LoggerFactory.getLogger(CitizenProfileControllerImpl::class.java)

    /**
     * Handles citizen self-registration.
     */
    override fun registerCitizen(registerCitizenDto: RegisterCitizenDto): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Received citizen self-registration request")
        val citizenResponse = citizenProfileService.registerCitizen(registerCitizenDto)
        logger.info("Citizen self-registration completed successfully for ID: ${citizenResponse.id}")
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    data = citizenResponse,
                    message = "Citizen registered successfully. Your registration is pending approval."
                )
            )
    }

    /**
     * Retrieves the profile of the currently logged-in citizen.
     */
    override fun getMyProfile(currentUserId: UUID): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.debug("Retrieving profile for citizen ID: $currentUserId")
        val profileResponse = citizenProfileService.getMyProfile(currentUserId)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = profileResponse,
                message = "Profile retrieved successfully"
            )
        )
    }

    /**
     * Updates specific fields of the logged-in citizen's profile using the UpdateProfileDto.
     */
    override fun updateMyProfile(
        currentUserId: UUID,
        updateProfileDto: UpdateProfileDto
    ): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Updating profile for citizen ID: $currentUserId")
        val updatedProfile = citizenProfileService.updateMyProfile(currentUserId, updateProfileDto)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedProfile,
                message = "Profile updated successfully"
            )
        )
    }

    /**
     * Changes the password for the logged-in citizen using the ChangePasswordDto.
     */
    override fun changeMyPassword(
        currentUserId: UUID,
        changePasswordDto: ChangePasswordDto
    ): ResponseEntity<ApiResponse<CitizenResponse>> {
        logger.info("Processing password change for citizen ID: $currentUserId")
        val updatedProfile = citizenProfileService.changeMyPassword(currentUserId, changePasswordDto)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedProfile,
                message = "Password changed successfully"
            )
        )
    }

    /**
     * Uploads a citizen's photo.
     */
    override fun uploadMyPhoto(
        currentUserId: UUID, 
        photo: MultipartFile
    ): ResponseEntity<ApiResponse<DocumentUploadResponse>> {
        logger.info("Uploading photo for citizen ID: $currentUserId")
        val uploadResponse = citizenProfileService.uploadMyPhoto(currentUserId, photo)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = uploadResponse,
                message = "Photo uploaded successfully"
            )
        )
    }

    /**
     * Uploads the front page of a citizen's citizenship certificate.
     */
    override fun uploadMyCitizenshipFront(
        currentUserId: UUID,
        document: MultipartFile
    ): ResponseEntity<ApiResponse<DocumentUploadResponse>> {
        logger.info("Uploading citizenship front page for citizen ID: $currentUserId")
        val uploadResponse = citizenProfileService.uploadMyCitizenshipFront(currentUserId, document)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = uploadResponse,
                message = "Citizenship front page uploaded successfully"
            )
        )
    }

    /**
     * Uploads the back page of a citizen's citizenship certificate.
     */
    override fun uploadMyCitizenshipBack(
        currentUserId: UUID,
        document: MultipartFile
    ): ResponseEntity<ApiResponse<DocumentUploadResponse>> {
        logger.info("Uploading citizenship back page for citizen ID: $currentUserId")
        val uploadResponse = citizenProfileService.uploadMyCitizenshipBack(currentUserId, document)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = uploadResponse,
                message = "Citizenship back page uploaded successfully"
            )
        )
    }
}