package np.sthaniya.dpis.citizen.mapper

import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.dto.response.CitizenDocumentsResponse
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentDetailsResponse
import np.sthaniya.dpis.citizen.dto.shared.AddressResponse
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.common.storage.DocumentStorageService
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

/**
 * Mapper component for converting between Citizen entities and DTOs.
 */
@Component
class CitizenMapper(
    private val documentStorageService: DocumentStorageService
) {
    
    /**
     * Maps a Citizen entity to a CitizenResponse DTO.
     *
     * @param citizen The Citizen entity to be mapped
     * @return A CitizenResponse DTO with all relevant citizen data
     */
    fun toResponse(citizen: Citizen): CitizenResponse {
        // Generate document details with URLs, states, and notes
        val documents = CitizenDocumentsResponse(
            photo = citizen.photoKey?.let {
                DocumentDetailsResponse(
                    url = documentStorageService.getDocumentUrl(it),
                    state = citizen.photoState,
                    note = citizen.photoStateNote,
                    uploadedAt = null // Update if you add upload timestamp to Citizen entity
                )
            },
            
            citizenshipFront = citizen.citizenshipFrontKey?.let {
                DocumentDetailsResponse(
                    url = documentStorageService.getDocumentUrl(it),
                    state = citizen.citizenshipFrontState,
                    note = citizen.citizenshipFrontStateNote,
                    uploadedAt = null // Update if you add upload timestamp to Citizen entity
                )
            },
            
            citizenshipBack = citizen.citizenshipBackKey?.let {
                DocumentDetailsResponse(
                    url = documentStorageService.getDocumentUrl(it),
                    state = citizen.citizenshipBackState,
                    note = citizen.citizenshipBackStateNote,
                    uploadedAt = null // Update if you add upload timestamp to Citizen entity
                )
            }
        )

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
            documents = documents,
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
