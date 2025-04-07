package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.exception.CitizenException.CitizenErrorCode
import np.sthaniya.dpis.citizen.mapper.CitizenMapper
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenManagementService
import np.sthaniya.dpis.location.service.AddressValidationService
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.common.storage.DocumentStorageService
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.UUID

@Service
class CitizenManagementServiceImpl(
    private val citizenRepository: CitizenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val securityService: SecurityService,
    private val addressValidationService: AddressValidationService,
    private val citizenMapper: CitizenMapper,
    private val documentStorageService: DocumentStorageService,
    private val i18nMessageService: I18nMessageService
) : CitizenManagementService {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val PHOTOS_FOLDER = "citizens/photos"
        const val CITIZENSHIP_FRONT_FOLDER = "citizens/citizenship/front"
        const val CITIZENSHIP_BACK_FOLDER = "citizens/citizenship/back"

        const val MAX_PHOTO_SIZE_BYTES = 5 * 1024 * 1024L // 5MB
        const val MAX_DOCUMENT_SIZE_BYTES = 10 * 1024 * 1024L // 10MB

        val ALLOWED_PHOTO_TYPES = setOf("image/jpeg", "image/png", "image/jpg")
        val ALLOWED_DOCUMENT_TYPES = setOf("image/jpeg", "image/png", "image/jpg", "application/pdf")
    }

    @Transactional
    override fun createCitizen(createCitizenDto: CreateCitizenDto): CitizenResponse {
        // Validate citizenship number uniqueness
        createCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    "Citizen with citizenship number $citizenshipNumber already exists"
                )
            }

            // Validate citizenship data completeness
            if (citizenshipNumber.isNotBlank() &&
                (createCitizenDto.citizenshipIssuedDate == null ||
                 createCitizenDto.citizenshipIssuedOffice.isNullOrBlank())) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    "Citizenship details are incomplete. Please provide both issue date and issuing office"
                )
            }
        }

        // Validate email uniqueness
        createCitizenDto.email?.let { email ->
            if (email.isNotBlank() && citizenRepository.existsByEmail(email)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_EMAIL,
                    "Email $email is already registered to another citizen"
                )
            }
        }

        val citizen = Citizen().apply {
            name = createCitizenDto.name
            nameDevnagari = createCitizenDto.nameDevnagari
            citizenshipNumber = createCitizenDto.citizenshipNumber
            citizenshipIssuedDate = createCitizenDto.citizenshipIssuedDate
            citizenshipIssuedOffice = createCitizenDto.citizenshipIssuedOffice
            email = createCitizenDto.email
            phoneNumber = createCitizenDto.phoneNumber
            fatherName = createCitizenDto.fatherName
            grandfatherName = createCitizenDto.grandfatherName
            spouseName = createCitizenDto.spouseName
            
            // Set initial state
            state = if (createCitizenDto.isApproved) CitizenState.APPROVED else CitizenState.UNDER_REVIEW
        }

        // Set password if provided
        createCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        // Handle approval if specified
        if (createCitizenDto.isApproved) {
            citizen.isApproved = true
            citizen.approvedAt = Instant.now()
            citizen.approvedBy = createCitizenDto.approvedBy ?: securityService.getCurrentUser().id
        }

        // Process addresses
        createCitizenDto.permanentAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.permanentAddress = validatedAddress.toAddress()
        }

        createCitizenDto.temporaryAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.temporaryAddress = validatedAddress.toAddress()
        }

        val savedCitizen = citizenRepository.save(citizen)
        logger.info("Created new citizen: ${savedCitizen.id}")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional(readOnly = true)
    override fun getCitizenById(id: UUID): CitizenResponse {
        val citizen = getCitizenEntity(id)
        return citizenMapper.toResponse(citizen)
    }

    @Transactional
    override fun approveCitizen(id: UUID, approvedBy: UUID): CitizenResponse {
        logger.info("Approving citizen with ID: $id by user: $approvedBy")
        
        val citizen = getCitizenEntity(id)

        if (citizen.isApproved) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_APPROVED,
                "Citizen with ID $id is already approved"
            )
        }

        // Set approval flags
        citizen.isApproved = true
        citizen.approvedAt = Instant.now()
        citizen.approvedBy = approvedBy
        
        // Update state to APPROVED
        citizen.updateState(CitizenState.APPROVED, "Approved by administrator", approvedBy)

        val savedCitizen = citizenRepository.save(citizen)
        logger.info("Successfully approved citizen with ID: $id")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun deleteCitizen(id: UUID, deletedBy: UUID): CitizenResponse {
        logger.info("Deleting citizen with ID: $id by user: $deletedBy")
        
        val citizen = getCitizenEntity(id)

        if (citizen.isDeleted) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_DELETED,
                "Citizen with ID $id is already deleted"
            )
        }

        citizen.isDeleted = true
        citizen.deletedAt = Instant.now()
        citizen.deletedBy = deletedBy

        val savedCitizen = citizenRepository.save(citizen)
        logger.info("Successfully deleted citizen with ID: $id")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun updateCitizen(id: UUID, updateCitizenDto: UpdateCitizenDto, updatedBy: UUID): CitizenResponse {
        logger.info("Updating citizen with ID: $id")
        
        val citizen = getCitizenEntity(id)

        // Check for citizenship number uniqueness if changed
        updateCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizen.citizenshipNumber != citizenshipNumber &&
                citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    "Citizen with citizenship number $citizenshipNumber already exists"
                )
            }

            // Validate citizenship data completeness
            if (citizenshipNumber.isNotBlank() &&
                (citizen.citizenshipIssuedDate == null && updateCitizenDto.citizenshipIssuedDate == null) ||
                ((citizen.citizenshipIssuedOffice.isNullOrBlank() && updateCitizenDto.citizenshipIssuedOffice.isNullOrBlank()))) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    "Citizenship details are incomplete. Please provide both issue date and issuing office"
                )
            }
        }

        // Check for email uniqueness if changed
        updateCitizenDto.email?.let { email ->
            if (citizen.email != email && 
                email.isNotBlank() && 
                citizenRepository.existsByEmail(email)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_EMAIL,
                    "Email $email is already registered to another citizen"
                )
            }
        }

        // Update fields selectively
        updateCitizenDto.name?.let { citizen.name = it }
        updateCitizenDto.nameDevnagari?.let { citizen.nameDevnagari = it }
        updateCitizenDto.citizenshipNumber?.let { citizen.citizenshipNumber = it }
        updateCitizenDto.citizenshipIssuedDate?.let { citizen.citizenshipIssuedDate = it }
        updateCitizenDto.citizenshipIssuedOffice?.let { citizen.citizenshipIssuedOffice = it }
        updateCitizenDto.email?.let { citizen.email = it }
        updateCitizenDto.phoneNumber?.let { citizen.phoneNumber = it }
        updateCitizenDto.fatherName?.let { citizen.fatherName = it }
        updateCitizenDto.grandfatherName?.let { citizen.grandfatherName = it }
        updateCitizenDto.spouseName?.let { citizen.spouseName = it }

        // Set password if provided
        updateCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        // Handle approval if specified
        updateCitizenDto.isApproved?.let { isApproved ->
            if (isApproved && !citizen.isApproved) {
                citizen.isApproved = true
                citizen.approvedAt = Instant.now()
                citizen.approvedBy = updatedBy
                
                // Update state to APPROVED
                citizen.updateState(
                    CitizenState.APPROVED, 
                    "Approved during update", 
                    updatedBy
                )
            }
        }

        // Process addresses
        updateCitizenDto.permanentAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.permanentAddress = validatedAddress.toAddress()
        }

        updateCitizenDto.temporaryAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.temporaryAddress = validatedAddress.toAddress()
        }

        citizen.updatedAt = Instant.now()
        updateCitizenDto.updatedBy?.let { citizen.updatedBy = it }

        val savedCitizen = citizenRepository.save(citizen)
        logger.info("Successfully updated citizen with ID: $id")
        
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun uploadCitizenPhoto(id: UUID, photo: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading photo for citizen ID: $id")
        return uploadDocument(
            id,
            photo,
            PHOTOS_FOLDER,
            MAX_PHOTO_SIZE_BYTES,
            ALLOWED_PHOTO_TYPES,
            DocumentUploadHandler { citizen, storageKey ->
                citizen.photoKey = storageKey
                citizen.photoState = DocumentState.AWAITING_REVIEW
            },
            updatedBy
        )
    }

    @Transactional
    override fun uploadCitizenshipFront(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading citizenship front page for citizen ID: $id")
        return uploadDocument(
            id,
            document,
            CITIZENSHIP_FRONT_FOLDER,
            MAX_DOCUMENT_SIZE_BYTES,
            ALLOWED_DOCUMENT_TYPES,
            DocumentUploadHandler { citizen, storageKey ->
                citizen.citizenshipFrontKey = storageKey
                citizen.citizenshipFrontState = DocumentState.AWAITING_REVIEW
            },
            updatedBy
        )
    }

    @Transactional
    override fun uploadCitizenshipBack(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading citizenship back page for citizen ID: $id")
        return uploadDocument(
            id,
            document,
            CITIZENSHIP_BACK_FOLDER,
            MAX_DOCUMENT_SIZE_BYTES,
            ALLOWED_DOCUMENT_TYPES,
            DocumentUploadHandler { citizen, storageKey ->
                citizen.citizenshipBackKey = storageKey
                citizen.citizenshipBackState = DocumentState.AWAITING_REVIEW
            },
            updatedBy
        )
    }

    /**
     * Helper functional interface for document upload handlers
     */
    private fun interface DocumentUploadHandler {
        fun updateCitizen(citizen: Citizen, storageKey: String)
    }

    /**
     * Generic document upload method to avoid code duplication
     */
    private fun uploadDocument(
        id: UUID,
        file: MultipartFile,
        folder: String,
        maxSize: Long,
        allowedTypes: Set<String>,
        documentHandler: DocumentUploadHandler,
        updatedBy: UUID
    ): DocumentUploadResponse {
        if (file.size > maxSize) {
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_TOO_LARGE,
                "The file ${file.originalFilename} exceeds the maximum allowed size of ${maxSize / (1024 * 1024)}MB"
            )
        }

        val contentType = file.contentType
        if (contentType.isNullOrBlank() || !allowedTypes.contains(contentType)) {
            throw CitizenException(
                CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
                "The file ${file.originalFilename} is not in an acceptable format. Allowed formats: ${allowedTypes.joinToString(", ")}"
            )
        }

        val citizen = getCitizenEntity(id)

        try {
            // Delete old document if exists (depends on which document we're uploading)
            when (folder) {
                PHOTOS_FOLDER -> {
                    citizen.photoKey?.let { oldKey ->
                        if (documentStorageService.documentExists(oldKey)) {
                            documentStorageService.deleteDocument(oldKey)
                            logger.debug("Deleted previous photo with key: $oldKey")
                        }
                    }
                }
                CITIZENSHIP_FRONT_FOLDER -> {
                    citizen.citizenshipFrontKey?.let { oldKey ->
                        if (documentStorageService.documentExists(oldKey)) {
                            documentStorageService.deleteDocument(oldKey)
                            logger.debug("Deleted previous citizenship front with key: $oldKey")
                        }
                    }
                }
                CITIZENSHIP_BACK_FOLDER -> {
                    citizen.citizenshipBackKey?.let { oldKey ->
                        if (documentStorageService.documentExists(oldKey)) {
                            documentStorageService.deleteDocument(oldKey)
                            logger.debug("Deleted previous citizenship back with key: $oldKey")
                        }
                    }
                }
            }

            val fileExtension = getFileExtension(file.originalFilename)
            val storageKey = documentStorageService.storeDocument(
                file = file,
                folder = folder,
                fileName = "${id}${fileExtension}"
            )

            // Update the appropriate field based on which document we're uploading
            documentHandler.updateCitizen(citizen, storageKey)
            
            citizen.updatedAt = Instant.now()
            citizen.updatedBy = updatedBy

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = file.originalFilename ?: "unknown",
                contentType = contentType,
                size = file.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload document for citizen with ID: $id", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }

    private fun getFileExtension(filename: String?): String {
        if (filename.isNullOrBlank()) return ""
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            filename.substring(lastDotIndex)
        } else {
            ""
        }
    }
    
    private fun getCitizenEntity(id: UUID): Citizen {
        logger.debug("Fetching citizen entity with ID: $id")
        val citizen = citizenRepository.findById(id).orElseThrow {
            logger.error("Citizen not found with ID: $id")
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }
        logger.debug("Successfully retrieved citizen: ${citizen.name} (ID: ${citizen.id})")
        return citizen
    }
}
