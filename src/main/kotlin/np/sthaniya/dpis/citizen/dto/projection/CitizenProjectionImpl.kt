package np.sthaniya.dpis.citizen.dto.projection

import com.fasterxml.jackson.annotation.JsonIgnore
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.Province
import np.sthaniya.dpis.location.domain.Ward
import java.time.Instant
import java.time.LocalDate
import java.util.*

/**
 * Reference implementation of [CitizenProjection] that provides field-level data selection.
 * 
 * Implements the projection interface by initializing and caching selected fields
 * from the source Citizen entity on instantiation. Fields not included in [includedFields]
 * are not loaded or cached.
 * 
 * Usage with repository:
 * ```kotlin
 * val projection = CitizenProjectionImpl(
 *     citizen = citizenEntity,
 *     includedFields = setOf("id", "name", "state")
 * )
 * ```
 * 
 * @param citizen Source Citizen entity
 * @param includedFields Set of field names to include in the projection
 * @see CitizenProjection
 * @see CitizenRepositoryCustom
 */
class CitizenProjectionImpl(
    @JsonIgnore private val citizen: Citizen,
    @JsonIgnore private val includedFields: Set<String>
) : CitizenProjection {

    private val fields = mutableMapOf<String, Any?>()

    init {
        // Handle address fields to avoid infinite recursion
        if ("permanentAddress" in includedFields) {
            citizen.permanentAddress?.let { address ->
                // Create a simplified copy of the address without circular references
                val simplifiedAddress = Address().apply {
                    streetAddress = address.streetAddress
                    
                    // Set province with minimal data
                    if (address.province != null) {
                        province = Province().apply {
                            code = address.province?.code
                            name = address.province?.name
                        }
                    }
                    
                    // Set district with minimal data
                    if (address.district != null) {
                        district = District().apply {
                            code = address.district?.code
                            name = address.district?.name
                        }
                    }
                    
                    // Set municipality with minimal data
                    if (address.municipality != null) {
                        municipality = Municipality().apply {
                            code = address.municipality?.code
                            name = address.municipality?.name
                        }
                    }
                    
                    // Set ward with minimal data
                    if (address.ward != null) {
                        val simplifiedMunicipality = Municipality().apply {
                            code = address.ward?.municipality?.code
                            name = address.ward?.municipality?.name
                        }
                        
                        ward = Ward().apply {
                            wardNumber = address.ward?.wardNumber ?: 0
                            municipality = simplifiedMunicipality
                        }
                    }
                }
                fields["permanentAddress"] = simplifiedAddress
            } ?: run { 
                fields["permanentAddress"] = null 
            }
        }
        
        if ("temporaryAddress" in includedFields) {
            citizen.temporaryAddress?.let { address ->
                // Create a simplified copy of the address without circular references
                val simplifiedAddress = Address().apply {
                    streetAddress = address.streetAddress
                    
                    // Set province with minimal data
                    if (address.province != null) {
                        province = Province().apply {
                            code = address.province?.code
                            name = address.province?.name
                        }
                    }
                    
                    // Set district with minimal data
                    if (address.district != null) {
                        district = District().apply {
                            code = address.district?.code
                            name = address.district?.name
                        }
                    }
                    
                    // Set municipality with minimal data
                    if (address.municipality != null) {
                        municipality = Municipality().apply {
                            code = address.municipality?.code
                            name = address.municipality?.name
                        }
                    }
                    
                    // Set ward with minimal data
                    if (address.ward != null) {
                        val simplifiedMunicipality = Municipality().apply {
                            code = address.ward?.municipality?.code
                            name = address.ward?.municipality?.name
                        }
                        
                        ward = Ward().apply {
                            wardNumber = address.ward?.wardNumber ?: 0
                            municipality = simplifiedMunicipality
                        }
                    }
                }
                fields["temporaryAddress"] = simplifiedAddress
            } ?: run { 
                fields["temporaryAddress"] = null 
            }
        }
        
        // All other fields can be processed normally
        if ("id" in includedFields) fields["id"] = citizen.id
        if ("name" in includedFields) fields["name"] = citizen.name
        if ("nameDevnagari" in includedFields) fields["nameDevnagari"] = citizen.nameDevnagari
        if ("citizenshipNumber" in includedFields) fields["citizenshipNumber"] = citizen.citizenshipNumber
        if ("citizenshipIssuedDate" in includedFields) fields["citizenshipIssuedDate"] = citizen.citizenshipIssuedDate
        if ("citizenshipIssuedOffice" in includedFields) fields["citizenshipIssuedOffice"] = citizen.citizenshipIssuedOffice
        if ("email" in includedFields) fields["email"] = citizen.email
        if ("phoneNumber" in includedFields) fields["phoneNumber"] = citizen.phoneNumber
        if ("state" in includedFields) fields["state"] = citizen.state
        if ("stateNote" in includedFields) fields["stateNote"] = citizen.stateNote
        if ("stateUpdatedAt" in includedFields) fields["stateUpdatedAt"] = citizen.stateUpdatedAt
        if ("stateUpdatedBy" in includedFields) fields["stateUpdatedBy"] = citizen.stateUpdatedBy
        if ("isApproved" in includedFields) fields["isApproved"] = citizen.isApproved
        if ("approvedBy" in includedFields) fields["approvedBy"] = citizen.approvedBy
        if ("approvedAt" in includedFields) fields["approvedAt"] = citizen.approvedAt
        if ("isDeleted" in includedFields) fields["isDeleted"] = citizen.isDeleted
        if ("deletedBy" in includedFields) fields["deletedBy"] = citizen.deletedBy
        if ("deletedAt" in includedFields) fields["deletedAt"] = citizen.deletedAt
        if ("permanentAddress" in includedFields) fields["permanentAddress"] = citizen.permanentAddress
        if ("temporaryAddress" in includedFields) fields["temporaryAddress"] = citizen.temporaryAddress
        if ("fatherName" in includedFields) fields["fatherName"] = citizen.fatherName
        if ("grandfatherName" in includedFields) fields["grandfatherName"] = citizen.grandfatherName
        if ("spouseName" in includedFields) fields["spouseName"] = citizen.spouseName
        if ("photoKey" in includedFields) fields["photoKey"] = citizen.photoKey
        if ("photoState" in includedFields) fields["photoState"] = citizen.photoState
        if ("photoStateNote" in includedFields) fields["photoStateNote"] = citizen.photoStateNote
        if ("citizenshipFrontKey" in includedFields) fields["citizenshipFrontKey"] = citizen.citizenshipFrontKey
        if ("citizenshipFrontState" in includedFields) fields["citizenshipFrontState"] = citizen.citizenshipFrontState
        if ("citizenshipFrontStateNote" in includedFields) fields["citizenshipFrontStateNote"] = citizen.citizenshipFrontStateNote
        if ("citizenshipBackKey" in includedFields) fields["citizenshipBackKey"] = citizen.citizenshipBackKey
        if ("citizenshipBackState" in includedFields) fields["citizenshipBackState"] = citizen.citizenshipBackState
        if ("citizenshipBackStateNote" in includedFields) fields["citizenshipBackStateNote"] = citizen.citizenshipBackStateNote
        if ("createdAt" in includedFields) fields["createdAt"] = citizen.createdAt
        if ("updatedAt" in includedFields) fields["updatedAt"] = citizen.updatedAt
    }

    override fun getId(): UUID? = fields["id"] as? UUID
    override fun getName(): String? = fields["name"] as? String
    override fun getNameDevnagari(): String? = fields["nameDevnagari"] as? String
    override fun getCitizenshipNumber(): String? = fields["citizenshipNumber"] as? String
    override fun getCitizenshipIssuedDate(): LocalDate? = fields["citizenshipIssuedDate"] as? LocalDate
    override fun getCitizenshipIssuedOffice(): String? = fields["citizenshipIssuedOffice"] as? String
    override fun getEmail(): String? = fields["email"] as? String
    override fun getPhoneNumber(): String? = fields["phoneNumber"] as? String
    override fun getState(): CitizenState? = fields["state"] as? CitizenState
    override fun getStateNote(): String? = fields["stateNote"] as? String
    override fun getStateUpdatedAt(): Instant? = fields["stateUpdatedAt"] as? Instant
    override fun getStateUpdatedBy(): UUID? = fields["stateUpdatedBy"] as? UUID
    override fun getIsApproved(): Boolean? = fields["isApproved"] as? Boolean
    override fun getApprovedBy(): UUID? = fields["approvedBy"] as? UUID
    override fun getApprovedAt(): Instant? = fields["approvedAt"] as? Instant
    override fun getIsDeleted(): Boolean? = fields["isDeleted"] as? Boolean
    override fun getDeletedBy(): UUID? = fields["deletedBy"] as? UUID
    override fun getDeletedAt(): Instant? = fields["deletedAt"] as? Instant
    override fun getPermanentAddress(): Address? = fields["permanentAddress"] as? Address
    override fun getTemporaryAddress(): Address? = fields["temporaryAddress"] as? Address
    override fun getFatherName(): String? = fields["fatherName"] as? String
    override fun getGrandfatherName(): String? = fields["grandfatherName"] as? String
    override fun getSpouseName(): String? = fields["spouseName"] as? String
    override fun getPhotoKey(): String? = fields["photoKey"] as? String
    override fun getPhotoState(): DocumentState? = fields["photoState"] as? DocumentState
    override fun getPhotoStateNote(): String? = fields["photoStateNote"] as? String
    override fun getCitizenshipFrontKey(): String? = fields["citizenshipFrontKey"] as? String
    override fun getCitizenshipFrontState(): DocumentState? = fields["citizenshipFrontState"] as? DocumentState
    override fun getCitizenshipFrontStateNote(): String? = fields["citizenshipFrontStateNote"] as? String
    override fun getCitizenshipBackKey(): String? = fields["citizenshipBackKey"] as? String
    override fun getCitizenshipBackState(): DocumentState? = fields["citizenshipBackState"] as? DocumentState
    override fun getCitizenshipBackStateNote(): String? = fields["citizenshipBackStateNote"] as? String
    override fun getCreatedAt(): Instant? = fields["createdAt"] as? Instant
    override fun getUpdatedAt(): Instant? = fields["updatedAt"] as? Instant
}
