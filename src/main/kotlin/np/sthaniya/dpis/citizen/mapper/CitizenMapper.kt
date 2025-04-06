package np.sthaniya.dpis.citizen.mapper

import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.shared.AddressResponse
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

/**
 * Mapper component for converting between Citizen entities and DTOs.
 */
@Component
class CitizenMapper {
    
    /**
     * Maps a Citizen entity to a CitizenResponse DTO.
     *
     * @param citizen The Citizen entity to be mapped
     * @return A CitizenResponse DTO with all relevant citizen data
     */
    fun toResponse(citizen: Citizen): CitizenResponse {
       

        return CitizenResponse(
            id = citizen.id ?: UUID.randomUUID(), // Provide a default in case id is null
            name = citizen.name ?: "", // Provide empty string if null
            nameDevnagari = citizen.nameDevnagari,
            citizenshipNumber = citizen.citizenshipNumber,
            citizenshipIssuedDate = citizen.citizenshipIssuedDate,
            citizenshipIssuedOffice = citizen.citizenshipIssuedOffice,
            email = citizen.email,
            phoneNumber = citizen.phoneNumber,
            permanentAddress = citizen.permanentAddress?.let { toAddressResponse(it) },
            temporaryAddress = citizen.temporaryAddress?.let { toAddressResponse(it) },
            fatherName = citizen.fatherName,
            grandfatherName = citizen.grandfatherName,
            spouseName = citizen.spouseName,
            isApproved = citizen.isApproved,
            approvedAt = citizen.approvedAt,
            approvedBy = citizen.approvedBy,
            createdAt = citizen.createdAt,
            updatedAt = citizen.updatedAt
        )
    }
    
    /**
     * Maps an Address entity to an AddressResponse DTO.
     *
     * @param address The Address entity to be mapped
     * @return An AddressResponse DTO with all address information
     */
    fun toAddressResponse(address: Address): AddressResponse {
        return AddressResponse(
            provinceCode = address.province?.code ?: "",
            provinceName = address.province?.name ?: "",
            provinceNameNepali = address.province?.nameNepali,
            
            districtCode = address.district?.code ?: "",
            districtName = address.district?.name ?: "",
            districtNameNepali = address.district?.nameNepali,
            
            municipalityCode = address.municipality?.code ?: "",
            municipalityName = address.municipality?.name ?: "",
            municipalityNameNepali = address.municipality?.nameNepali,
            municipalityType = address.municipality?.type?.name ?: "",
            
            wardNumber = address.ward?.wardNumber ?: 0,
            
            streetAddress = address.streetAddress
        )
    }
}
