package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.exception.CitizenException.CitizenErrorCode
import np.sthaniya.dpis.citizen.mapper.CitizenMapper
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenManagementService
import np.sthaniya.dpis.location.service.AddressValidationService
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    private val i18nMessageService: I18nMessageService
) : CitizenManagementService {

    private val logger = LoggerFactory.getLogger(CitizenManagementServiceImpl::class.java)

    @Transactional
    override fun createCitizen(createCitizenDto: CreateCitizenDto): CitizenResponse {
        // Validate citizenship number if provided
        createCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    i18nMessageService.getMessage("citizen.error.duplicate_citizenship", arrayOf(citizenshipNumber))
                )
            }

            // Validate citizenship certificate data
            if (citizenshipNumber.isNotBlank() &&
                (createCitizenDto.citizenshipIssuedDate == null ||
                 createCitizenDto.citizenshipIssuedOffice.isNullOrBlank())) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    i18nMessageService.getMessage("citizen.error.invalid_citizenship_data")
                )
            }
        }

        // Create and populate the citizen entity
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

        // Handle password if provided
        createCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        // Set approval status if specified
        if (createCitizenDto.isApproved) {
            citizen.isApproved = true
            citizen.approvedAt = LocalDateTime.now()
            citizen.approvedBy = createCitizenDto.approvedBy ?: securityService.getCurrentUser().id
        }

        // Validate and map permanent address if provided
        createCitizenDto.permanentAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.permanentAddress = validatedAddress.toAddress()
        }

        // Validate and map temporary address if provided
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
                i18nMessageService.getMessage("citizen.error.not_found", arrayOf(id))
            )
        }
        return citizenMapper.toResponse(citizen)
    }

    @Transactional
    override fun approveCitizen(id: UUID, approvedBy: UUID): CitizenResponse {
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                i18nMessageService.getMessage("citizen.error.not_found", arrayOf(id))
            )
        }

        if (citizen.isApproved) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_APPROVED,
                i18nMessageService.getMessage("citizen.error.already_approved", arrayOf(id))
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
                i18nMessageService.getMessage("citizen.error.not_found", arrayOf(id))
            )
        }

        if (citizen.isDeleted) {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_ALREADY_DELETED,
                i18nMessageService.getMessage("citizen.error.already_deleted", arrayOf(id))
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
        // Find the existing citizen
        val citizen = citizenRepository.findById(id).orElseThrow {
            throw CitizenException(
                CitizenErrorCode.CITIZEN_NOT_FOUND,
                i18nMessageService.getMessage("citizen.error.not_found", arrayOf(id))
            )
        }

        // Validate citizenship number if provided and changed
        updateCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizen.citizenshipNumber != citizenshipNumber &&
                citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw CitizenException(
                    CitizenErrorCode.DUPLICATE_CITIZENSHIP_NUMBER,
                    i18nMessageService.getMessage("citizen.error.duplicate_citizenship", arrayOf(citizenshipNumber))
                )
            }

            // Validate citizenship certificate data
            if (citizenshipNumber.isNotBlank() &&
                (citizen.citizenshipIssuedDate == null && updateCitizenDto.citizenshipIssuedDate == null) ||
                ((citizen.citizenshipIssuedOffice.isNullOrBlank() && updateCitizenDto.citizenshipIssuedOffice.isNullOrBlank()))) {
                throw CitizenException(
                    CitizenErrorCode.INVALID_CITIZENSHIP_DATA,
                    i18nMessageService.getMessage("citizen.error.invalid_citizenship_data")
                )
            }
        }

        // Update fields if provided
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

        // Handle password if provided
        updateCitizenDto.password?.let { password ->
            citizen.setPassword(passwordEncoder.encode(password))
        }

        // Update approval status if specified
        updateCitizenDto.isApproved?.let { isApproved ->
            if (isApproved && !citizen.isApproved) {
                citizen.isApproved = true
                citizen.approvedAt = LocalDateTime.now()
                citizen.approvedBy = updateCitizenDto.updatedBy ?: securityService.getCurrentUser().id
            }
        }

        // Validate and map permanent address if provided
        updateCitizenDto.permanentAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.permanentAddress = validatedAddress.toAddress()
        }

        // Validate and map temporary address if provided
        updateCitizenDto.temporaryAddress?.let { addressDto ->
            val validatedAddress = addressValidationService.validateAddress(addressDto)
            citizen.temporaryAddress = validatedAddress.toAddress()
        }

        // Update modification tracking fields
        citizen.updatedAt = Instant.now()
        updateCitizenDto.updatedBy?.let { citizen.updatedBy = it }

        val savedCitizen = citizenRepository.save(citizen)
        return citizenMapper.toResponse(savedCitizen)
    }
}
