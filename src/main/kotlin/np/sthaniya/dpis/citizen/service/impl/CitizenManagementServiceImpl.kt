package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.exception.CitizenException.CitizenErrorCode
import np.sthaniya.dpis.citizen.mapper.CitizenMapper
import np.sthaniya.dpis.citizen.domain.entity.Citizen
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
import java.time.LocalDateTime
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

    private val logger = LoggerFactory.getLogger(CitizenManagementServiceImpl::class.java)

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
        createCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    "Citizen with citizenship number $citizenshipNumber already exists"
                )
            }

            if (citizenshipNumber.isNotBlank() &&
                (createCitizenDto.citizenshipIssuedDate == null ||
                 createCitizenDto.citizenshipIssuedOffice.isNullOrBlank())) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    "Citizenship details are incomplete. Please provide both issue date and issuing office"
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
        }

        createCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        if (createCitizenDto.isApproved) {
            citizen.isApproved = true
            citizen.approvedAt = LocalDateTime.now()
            citizen.approvedBy = createCitizenDto.approvedBy ?: securityService.getCurrentUser().id
        }

        createCitizenDto.permanentAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.permanentAddress = validatedAddress.toAddress()
        }

        createCitizenDto.temporaryAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.temporaryAddress = validatedAddress.toAddress()
        }

        val savedCitizen = citizenRepository.save(citizen)
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun getCitizenById(id: UUID): CitizenResponse {
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }
        return citizenMapper.toResponse(citizen)
    }

    @Transactional
    override fun approveCitizen(id: UUID, approvedBy: UUID): CitizenResponse {
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }

        if (citizen.isApproved) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_APPROVED,
                "Citizen with ID $id is already approved"
            )
        }

        citizen.isApproved = true
        citizen.approvedAt = LocalDateTime.now()
        citizen.approvedBy = approvedBy

        val savedCitizen = citizenRepository.save(citizen)
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun deleteCitizen(id: UUID, deletedBy: UUID): CitizenResponse {
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }

        if (citizen.isDeleted) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_DELETED,
                "Citizen with ID $id is already deleted"
            )
        }

        citizen.isDeleted = true
        citizen.deletedAt = LocalDateTime.now()
        citizen.deletedBy = deletedBy

        val savedCitizen = citizenRepository.save(citizen)
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun updateCitizen(id: UUID, updateCitizenDto: UpdateCitizenDto): CitizenResponse {
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }

        updateCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizen.citizenshipNumber != citizenshipNumber &&
                citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    "Citizen with citizenship number $citizenshipNumber already exists"
                )
            }

            if (citizenshipNumber.isNotBlank() &&
                (citizen.citizenshipIssuedDate == null && updateCitizenDto.citizenshipIssuedDate == null) ||
                ((citizen.citizenshipIssuedOffice.isNullOrBlank() && updateCitizenDto.citizenshipIssuedOffice.isNullOrBlank()))) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    "Citizenship details are incomplete. Please provide both issue date and issuing office"
                )
            }
        }

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

        updateCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        updateCitizenDto.isApproved?.let { isApproved ->
            if (isApproved && !citizen.isApproved) {
                citizen.isApproved = true
                citizen.approvedAt = LocalDateTime.now()
                citizen.approvedBy = updateCitizenDto.updatedBy ?: securityService.getCurrentUser().id
            }
        }

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
        return citizenMapper.toResponse(savedCitizen)
    }

    @Transactional
    override fun uploadCitizenPhoto(id: UUID, photo: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading photo for citizen ID: $id")

        if (photo.size > MAX_PHOTO_SIZE_BYTES) {
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_TOO_LARGE,
                "The file ${photo.originalFilename} exceeds the maximum allowed size of ${MAX_PHOTO_SIZE_BYTES / (1024 * 1024)}MB"
            )
        }

        val contentType = photo.contentType
        if (contentType.isNullOrBlank() || !ALLOWED_PHOTO_TYPES.contains(contentType)) {
            throw CitizenException(
                CitizenErrorCode.INVALID_DOCUMENT_FORMAT,
                "The file ${photo.originalFilename} is not in an acceptable format. Allowed formats: ${ALLOWED_PHOTO_TYPES.joinToString(", ")}"
            )
        }

        val citizen = getCitizenEntity(id)

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
                fileName = "${id}${fileExtension}"
            )

            citizen.photoKey = storageKey
            citizen.updatedAt = Instant.now()
            citizen.updatedBy = updatedBy

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = photo.originalFilename ?: "unknown",
                contentType = contentType,
                size = photo.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload photo for citizen with ID: $id", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }

    @Transactional
    override fun uploadCitizenshipFront(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading citizenship front page for citizen ID: $id")

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

        val citizen = getCitizenEntity(id)

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
                fileName = "${id}${fileExtension}"
            )

            citizen.citizenshipFrontKey = storageKey
            citizen.updatedAt = Instant.now()
            citizen.updatedBy = updatedBy

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = document.originalFilename ?: "unknown",
                contentType = contentType,
                size = document.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload citizenship front for citizen with ID: $id", e)
            throw CitizenException(
                CitizenErrorCode.DOCUMENT_UPLOAD_FAILED,
                "Document upload failed: ${e.message}"
            )
        }
    }

    @Transactional
    override fun uploadCitizenshipBack(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse {
        logger.info("Uploading citizenship back page for citizen ID: $id")

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

        val citizen = getCitizenEntity(id)

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
                fileName = "${id}${fileExtension}"
            )

            citizen.citizenshipBackKey = storageKey
            citizen.updatedAt = Instant.now()
            citizen.updatedBy = updatedBy

            citizenRepository.save(citizen)

            val url = documentStorageService.getDocumentUrl(storageKey)

            return DocumentUploadResponse(
                storageKey = storageKey,
                originalFilename = document.originalFilename ?: "unknown",
                contentType = contentType,
                size = document.size,
                url = url
            )
        } catch (e: Exception) {
            logger.error("Failed to upload citizenship back for citizen with ID: $id", e)
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
        return citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                "Citizen with ID $id not found"
            )
        }
    }
}
