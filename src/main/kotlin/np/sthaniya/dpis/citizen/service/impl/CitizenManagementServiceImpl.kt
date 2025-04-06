package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.model.Citizen
import np.sthaniya.dpis.citizen.model.Address
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenManagementService
import np.sthaniya.dpis.common.exception.ResourceAlreadyExistsException
import np.sthaniya.dpis.common.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CitizenManagementServiceImpl(
    private val citizenRepository: CitizenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService
) : CitizenManagementService {

    @Transactional
    override fun createCitizen(createCitizenDto: CreateCitizenDto): Citizen {
        // Check if email is already registered
        createCitizenDto.email?.let { email ->
            if (citizenRepository.existsByEmail(email)) {
                throw ResourceAlreadyExistsException("Citizen with this email already exists")
            }
        }
        
        // Check if citizenship number is already registered
        createCitizenDto.citizenshipNumber?.let { citizenshipNumber ->
            if (citizenRepository.existsByCitizenshipNumber(citizenshipNumber)) {
                throw ResourceAlreadyExistsException("Citizen with this citizenship number already exists")
            }
        }
        
        // Create and populate the citizen entity
        val citizen = Citizen(
            name = createCitizenDto.name,
            nameDevnagari = createCitizenDto.nameDevnagari,
            citizenshipNumber = createCitizenDto.citizenshipNumber,
            citizenshipIssuedDate = createCitizenDto.citizenshipIssuedDate,
            citizenshipIssuedOffice = createCitizenDto.citizenshipIssuedOffice,
            email = createCitizenDto.email,
            phoneNumber = createCitizenDto.phoneNumber,
            fatherName = createCitizenDto.fatherName,
            grandfatherName = createCitizenDto.grandfatherName,
            spouseName = createCitizenDto.spouseName,
            createdAt = LocalDateTime.now()
        )
        
        // Handle password if provided
        createCitizenDto.password?.let { password ->
            citizen.passwordHash = passwordEncoder.encode(password)
        }
        
        // Set approval status if specified
        if (createCitizenDto.isApproved) {
            citizen.isApproved = true
            citizen.approvedAt = LocalDateTime.now()
            citizen.approvedBy = createCitizenDto.approvedBy ?: userService.getCurrentUserId()
        }
        
        // Map permanent address if provided
        createCitizenDto.permanentAddress?.let { addressDto ->
            citizen.permanentAddress = Address(
                province = addressDto.province,
                district = addressDto.district,
                municipality = addressDto.municipality,
                wardNumber = addressDto.wardNumber,
                tole = addressDto.tole,
                houseNumber = addressDto.houseNumber
            )
        }
        
        // Map temporary address if provided
        createCitizenDto.temporaryAddress?.let { addressDto ->
            citizen.temporaryAddress = Address(
                province = addressDto.province,
                district = addressDto.district,
                municipality = addressDto.municipality,
                wardNumber = addressDto.wardNumber,
                tole = addressDto.tole,
                houseNumber = addressDto.houseNumber
            )
        }
        
        // Save and return the created citizen
        return citizenRepository.save(citizen)
    }
}
