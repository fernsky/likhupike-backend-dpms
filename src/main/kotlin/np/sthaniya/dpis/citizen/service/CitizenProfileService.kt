package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.dto.profile.ChangePasswordDto
import np.sthaniya.dpis.citizen.dto.profile.RegisterCitizenDto
import np.sthaniya.dpis.citizen.dto.profile.UpdateProfileDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Service interface for citizen self-registration and profile management.
 * 
 * Provides operations for citizens to register themselves and manage their profiles.
 */
interface CitizenProfileService {
    
    /**
     * Registers a new citizen in the system through self-registration.
     * 
     * @param registerCitizenDto Data for citizen self-registration
     * @return The created citizen profile as a response DTO
     * @throws CitizenException.CitizenErrorCode.CITIZEN_ALREADY_REGISTERED if the citizen is already registered
     * @throws CitizenException.CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER if the citizenship number is already registered
     * @throws CitizenException.CitizenErrorCode.DUPLICATE_EMAIL if the email is already registered
     * @throws CitizenException.CitizenErrorCode.SELF_REGISTRATION_DISABLED if self-registration is disabled
     */
    fun registerCitizen(registerCitizenDto: RegisterCitizenDto): CitizenResponse
    
    /**
     * Retrieves the profile of a logged-in citizen.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @return The citizen's profile data
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     */
    fun getMyProfile(citizenId: UUID): CitizenResponse
    
    /**
     * Updates the profile of a logged-in citizen.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @param updateProfileDto The DTO with fields to update
     * @return The updated citizen profile
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.DUPLICATE_EMAIL if the email is already registered to another citizen
     */
    fun updateMyProfile(citizenId: UUID, updateProfileDto: UpdateProfileDto): CitizenResponse
    
    /**
     * Uploads or replaces a citizen's photo.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param photo The photo file to upload
     * @return Details about the uploaded photo
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadMyPhoto(citizenId: UUID, photo: MultipartFile): DocumentUploadResponse
    
    /**
     * Uploads or replaces the front page of a citizen's citizenship certificate.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadMyCitizenshipFront(citizenId: UUID, document: MultipartFile): DocumentUploadResponse
    
    /**
     * Uploads or replaces the back page of a citizen's citizenship certificate.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadMyCitizenshipBack(citizenId: UUID, document: MultipartFile): DocumentUploadResponse
    
    /**
     * Changes the password for a logged-in citizen.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @param changePasswordDto The DTO containing current and new passwords
     * @return The updated citizen profile
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException if the current password is incorrect or the new password is invalid
     */
    fun changeMyPassword(citizenId: UUID, changePasswordDto: ChangePasswordDto): CitizenResponse
}