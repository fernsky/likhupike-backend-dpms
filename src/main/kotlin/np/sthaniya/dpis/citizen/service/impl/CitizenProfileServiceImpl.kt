package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.profile.ChangePasswordDto
import np.sthaniya.dpis.citizen.dto.profile.RegisterCitizenDto
import np.sthaniya.dpis.citizen.dto.profile.UpdateProfileDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.exception.CitizenException.CitizenErrorCode
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.mapper.CitizenMapper
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenProfileService
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.storage.DocumentStorageService
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.UUID

/**
 * Implementation of [CitizenProfileService] for citizen self-registration and profile management.
 */
@Service
class CitizenProfileServiceImpl(
    private val citizenRepository: CitizenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val citizenMapper: CitizenMapper,
    private val documentStorageService: DocumentStorageService,
    private val securityService: SecurityService,
    private val i18nMessageService: I18nMessageService
) : CitizenProfileService {

    private val logger = LoggerFactory.getLogger(CitizenProfileServiceImpl::class.java)

    companion object {
        const val PHOTOS_FOLDER = "citizens/profiles/photos"
        const val CITIZENSHIP_FRONT_FOLDER = "citizens/profiles/citizenship/front"
        const val CITIZENSHIP_BACK_FOLDER = "citizens/profiles/citizenship/back"

        const val MAX_PHOTO_SIZE_BYTES = 5 * 1024 * 1024L // 5MB
        const val MAX_DOCUMENT_SIZE_BYTES = 10 * 1024 * 1024L // 10MB

        val ALLOWED_PHOTO_TYPES = setOf("image/jpeg", "image/png", "image/jpg")
        val ALLOWED_DOCUMENT_TYPES = setOf("image/jpeg", "image/png", "image/jpg", "application/pdf")
    }

    /**
     * Registers a new citizen in the system through self-registration.
     * 
     * @param registerCitizenDto Data for citizen self-registration
     * @return The created citizen profile as a response DTO
     * @throws CitizenException if validation fails or citizen already exists
     */
    @Transactional
    override fun registerCitizen(registerCitizenDto: RegisterCitizenDto): CitizenResponse {
        logger.info("Processing new citizen self-registration request")
        
        // Check if citizen with the same citizenship number already exists
        registerCitizenDto.citizenshipNumber.let { citizenshipNumber ->
            if (citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                logger.warn("Self-registration attempt with existing citizenship number: $citizenshipNumber")
                throw CitizenException(
                    CitizenErrorCode.CITIZEN_ALREADY_REGISTERED,
                    "Citizen with citizenship number $citizenshipNumber is already registered"
                )
            }
        }
        
        // Check for duplicate email if provided
        registerCitizenDto.email?.let { email ->
            if (citizenRepository.existsByEmail(email)) {
                logger.warn("Self-registration attempt with existing email: $email")
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_EMAIL,
                    "Email is already registered to another citizen"
                )
            }
        }

        // Validate citizenship data completeness
        if (registerCitizenDto.citizenshipNumber.isNotBlank() && 
            (registerCitizenDto.citizenshipIssuedDate == null || 
             registerCitizenDto.citizenshipIssuedOffice.isBlank())) {
            throw CitizenException(
                CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                "Citizenship details are incomplete. Please provide both issue date and issuing office"
            )
        }
        
        // Create new citizen entity
        val citizen = Citizen().apply {
            name = registerCitizenDto.name
            nameDevnagari = registerCitizenDto.nameDevnagari
            citizenshipNumber = registerCitizenDto.citizenshipNumber
            citizenshipIssuedDate = registerCitizenDto.citizenshipIssuedDate
            citizenshipIssuedOffice = registerCitizenDto.citizenshipIssuedOffice
            email = registerCitizenDto.email
            phoneNumber = registerCitizenDto.phoneNumber
            setPassword(passwordEncoder.encode(registerCitizenDto.password))
            isApproved = false // Self-registered citizens require approval
        }
        
        // Save the citizen
        val savedCitizen = citizenRepository.save(citizen)
        logger.info("Citizen self-registration completed successfully for ID: ${savedCitizen.id}")
        
        // Return the response
        return citizenMapper.toResponse(savedCitizen)
    }
    
    /**
     * Retrieves the profile of a logged-in citizen.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @return The citizen's profile data
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     */
    @Transactional(readOnly = true)
    override fun getMyProfile(citizenId: UUID): CitizenResponse {
        logger.debug("Retrieving profile for citizen ID: $citizenId")
        val citizen = getCitizenEntity(citizenId)
        return citizenMapper.toResponse(citizen)
    }
    
    /**
     * Updates the profile of a logged-in citizen using a dedicated DTO.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @param updateProfileDto The DTO with fields to update
     * @return The updated citizen profile
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     */
    @Transactional
    override fun updateMyProfile(citizenId: UUID, updateProfileDto: UpdateProfileDto): CitizenResponse {
        logger.info("Updating profile for citizen ID: $citizenId")
        val citizen = getCitizenEntity(citizenId)
        
        // Update email if provided and different
        updateProfileDto.email?.let { email ->
            if (email != citizen.email && citizenRepository.existsByEmail(email)) {
                logger.warn("Update attempt with existing email: $email for citizen ID: $citizenId")
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_EMAIL,
                    "Email is already registered to another citizen"
                )
            }
            citizen.email = email
        }
        
        // Update phone number if provided
        updateProfileDto.phoneNumber?.let { phoneNumber ->
            citizen.phoneNumber = phoneNumber
        }
        

        citizen.updatedAt = Instant.now()
        
        val savedCitizen = citizenRepository.save(citizen)
        logger.debug("Profile updated successfully for citizen ID: $citizenId")
        return citizenMapper.toResponse(savedCitizen)
    }
    
    /**
     * Uploads or replaces a citizen's photo.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param photo The photo file to upload
     * @return Details about the uploaded photo
     */
    @Transactional
    override fun uploadMyPhoto(citizenId: UUID, photo: MultipartFile): DocumentUploadResponse {
        logger.info("Uploading photo for citizen ID: $citizenId")

        if (photo.size > MAX_PHOTO_SIZE_BYTES) {
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_TOO_LARGE,
                "The photo ${photo.originalFilename} exceeds the maximum allowed size of ${MAX_PHOTO_SIZE_BYTES / (1024 * 1024)}MB"
            )
        }

        val contentType = photo.contentType
        if (contentType.isNullOrBlank() || !ALLOWED_PHOTO_TYPES.contains(contentType)) {
            throw CitizenException(
                CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
                "The photo ${photo.originalFilename} is not in an acceptable format. Allowed formats: ${ALLOWED_PHOTO_TYPES.joinToString(", ")}"
            )
        }

        val citizen = getCitizenEntity(citizenId)

        try {
            citizen.photoKey?.let { oldKey ->
                if (documentStorageService.documentExists(oldKey)) {
                    documentStorageService.deleteDocument(oldKey)
                    logger.debug("Deleted previous photo with key: $oldKey")
                }
            }

            val fileExtension = getFileExtension(photo.originalFilename)
            val storageKey = documentStorageService.storeDocument(
                file = photo,
                folder = PHOTOS_FOLDER,
                fileName = "${citizenId}${fileExtension}"
            )

            citizen.photoKey = storageKey
            citizen.updatedAt = Instant.now()

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)
            logger.debug("Photo uploaded successfully for citizen ID: $citizenId")

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = photo.originalFilename ?: "unknown",
                contentType = contentType,
                size = photo.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload photo for citizen with ID: $citizenId", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }
    
    /**
     * Uploads or replaces the front page of a citizen's citizenship certificate.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     */
    @Transactional
    override fun uploadMyCitizenshipFront(citizenId: UUID, document: MultipartFile): DocumentUploadResponse {
        logger.info("Uploading citizenship front page for citizen ID: $citizenId")

        if (document.size > MAX_DOCUMENT_SIZE_BYTES) {
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_TOO_LARGE,
                "The file ${document.originalFilename} exceeds the maximum allowed size of ${MAX_DOCUMENT_SIZE_BYTES / (1024 * 1024)}MB"
            )
        }

        val contentType = document.contentType
        if (contentType.isNullOrBlank() || !ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
            throw CitizenException(
                CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
                "The file ${document.originalFilename} is not in an acceptable format. Allowed formats: ${ALLOWED_DOCUMENT_TYPES.joinToString(", ")}"
            )
        }

        val citizen = getCitizenEntity(citizenId)

        try {
            citizen.citizenshipFrontKey?.let { oldKey ->
                if (documentStorageService.documentExists(oldKey)) {
                    documentStorageService.deleteDocument(oldKey)
                    logger.debug("Deleted previous citizenship front with key: $oldKey")
                }
            }

            val fileExtension = getFileExtension(document.originalFilename)
            val storageKey = documentStorageService.storeDocument(
                file = document,
                folder = CITIZENSHIP_FRONT_FOLDER,
                fileName = "${citizenId}${fileExtension}"
            )

            citizen.citizenshipFrontKey = storageKey
            citizen.updatedAt = Instant.now()

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)
            logger.debug("Citizenship front page uploaded successfully for citizen ID: $citizenId")

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = document.originalFilename ?: "unknown",
                contentType = contentType,
                size = document.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload citizenship front for citizen with ID: $citizenId", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }
    
    /**
     * Uploads or replaces the back page of a citizen's citizenship certificate.
     *
     * @param citizenId The ID of the logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     */
    @Transactional
    override fun uploadMyCitizenshipBack(citizenId: UUID, document: MultipartFile): DocumentUploadResponse {
        logger.info("Uploading citizenship back page for citizen ID: $citizenId")

        if (document.size > MAX_DOCUMENT_SIZE_BYTES) {
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_TOO_LARGE,
                "The file ${document.originalFilename} exceeds the maximum allowed size of ${MAX_DOCUMENT_SIZE_BYTES / (1024 * 1024)}MB"
            )
        }

        val contentType = document.contentType
        if (contentType.isNullOrBlank() || !ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
            throw CitizenException(
                CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
                "The file ${document.originalFilename} is not in an acceptable format. Allowed formats: ${ALLOWED_DOCUMENT_TYPES.joinToString(", ")}"
            )
        }

        val citizen = getCitizenEntity(citizenId)

        try {
            citizen.citizenshipBackKey?.let { oldKey ->
                if (documentStorageService.documentExists(oldKey)) {
                    documentStorageService.deleteDocument(oldKey)
                    logger.debug("Deleted previous citizenship back with key: $oldKey")
                }
            }

            val fileExtension = getFileExtension(document.originalFilename)
            val storageKey = documentStorageService.storeDocument(
                file = document,
                folder = CITIZENSHIP_BACK_FOLDER,
                fileName = "${citizenId}${fileExtension}"
            )

            citizen.citizenshipBackKey = storageKey
            citizen.updatedAt = Instant.now()

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)
            logger.debug("Citizenship back page uploaded successfully for citizen ID: $citizenId")

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = document.originalFilename ?: "unknown",
                contentType = contentType,
                size = document.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload citizenship back for citizen with ID: $citizenId", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }
    
    /**
     * Changes the password for a logged-in citizen using a dedicated DTO.
     * 
     * @param citizenId The ID of the logged-in citizen
     * @param changePasswordDto The DTO containing current and new passwords
     * @return The updated citizen profile
     */
    @Transactional
    override fun changeMyPassword(citizenId: UUID, changePasswordDto: ChangePasswordDto): CitizenResponse {
        logger.info("Processing password change for citizen ID: $citizenId")
        val citizen = getCitizenEntity(citizenId)
        
        // Validate that the current password is correct
        if (!passwordEncoder.matches(changePasswordDto.currentPassword, citizen.password)) {
            logger.warn("Invalid current password provided for citizen ID: $citizenId")
            throw CitizenException(
                CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                "Current password is incorrect"
            )
        }
        
        // Update the password
        citizen.setPassword(passwordEncoder.encode(changePasswordDto.newPassword))
        citizen.updatedAt = Instant.now()
        
        val savedCitizen = citizenRepository.save(citizen)
        logger.debug("Password changed successfully for citizen ID: $citizenId")
        return citizenMapper.toResponse(savedCitizen)
    }
    
    /**
     * Helper method to get a citizen entity by ID
     * 
     * @param citizenId ID of the citizen to retrieve
     * @return The citizen entity
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     */
    private fun getCitizenEntity(citizenId: UUID): Citizen {
        logger.debug("Fetching citizen entity with ID: $citizenId")
        val citizen = citizenRepository.findById(citizenId).orElseThrow {
            logger.error("Citizen not found with ID: $citizenId")
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $citizenId not found"
            )
        }
        logger.debug("Successfully retrieved citizen: ${citizen.name} (ID: ${citizen.id})")
        return citizen
    }
    
    /**
     * Extract file extension from filename
     */
    private fun getFileExtension(filename: String?): String {
        if (filename.isNullOrBlank()) return ""
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            filename.substring(lastDotIndex)
        } else {
            ""
        }
    }
}