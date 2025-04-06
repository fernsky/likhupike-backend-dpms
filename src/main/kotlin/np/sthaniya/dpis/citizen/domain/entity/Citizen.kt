package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.time.LocalDate
import java.util.UUID

/**
 * Represents a citizen in the Digital Profile Information System.
 * 
 * This entity contains citizen-specific information like personal details,
 * address, family information, and document references.
 *
 * Features:
 * - Complete citizen profile with personal details
 * - Citizenship document information
 * - Structured address information using embedded Address entities
 * - Family information
 * - Document storage references for photos and citizenship certificates
 * 
 * This implementation is now fully decoupled from the User entity system
 * and stands as its own independent entity.
 */
@Entity
@Table(
    name = "citizens",
    indexes = [
        Index(name = "idx_citizens_citizenship_number", columnList = "citizenship_number"),
        Index(name = "idx_citizens_email", columnList = "email"),
        Index(name = "idx_citizens_phone_number", columnList = "phone_number")
    ]
)
class Citizen : UuidBaseEntity() {

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "name_devnagari", nullable = false)
    var nameDevnagari: String? = null

    @Column(name = "citizenship_number", nullable = false, unique = true)
    var citizenshipNumber: String? = null

    @Column(name = "citizenship_issued_date", nullable = false)
    var citizenshipIssuedDate: LocalDate? = null

    @Column(name = "citizenship_issued_office", nullable = false)
    var citizenshipIssuedOffice: String? = null

    @Column(nullable = false, unique = true)
    var email: String? = null
    
    @Column(name = "phone_number")
    var phoneNumber: String? = null
    
    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
    
    @Column(name = "deleted_at")
    var deletedAt: LocalDate? = null
    
    @Column(name = "deleted_by")
    var deletedBy: UUID? = null

    @Embedded
    @AttributeOverrides(
        // Province relationship attributes
        AttributeOverride(name = "province.code", column = Column(name = "permanent_province_code", nullable = false)),
        
        // District relationship attributes
        AttributeOverride(name = "district.code", column = Column(name = "permanent_district_code", nullable = false)),
        
        // Municipality relationship attributes
        AttributeOverride(name = "municipality.code", column = Column(name = "permanent_municipality_code", nullable = false)),
        
        // Ward relationship attributes
        AttributeOverride(name = "ward.wardNumber", column = Column(name = "permanent_ward_number", nullable = false)),
        AttributeOverride(name = "ward.municipality.code", column = Column(name = "permanent_ward_municipality_code", nullable = false)),
        
        // Street address
        AttributeOverride(name = "streetAddress", column = Column(name = "permanent_street_address"))
    )
    var permanentAddress: Address? = null

    @Embedded
    @AttributeOverrides(
        // Province relationship attributes
        AttributeOverride(name = "province.code", column = Column(name = "temporary_province_code")),
        
        // District relationship attributes
        AttributeOverride(name = "district.code", column = Column(name = "temporary_district_code")),
        
        // Municipality relationship attributes
        AttributeOverride(name = "municipality.code", column = Column(name = "temporary_municipality_code")),
        
        // Ward relationship attributes
        AttributeOverride(name = "ward.wardNumber", column = Column(name = "temporary_ward_number")),
        AttributeOverride(name = "ward.municipality.code", column = Column(name = "temporary_ward_municipality_code")),
        
        // Street address
        AttributeOverride(name = "streetAddress", column = Column(name = "temporary_street_address"))
    )
    var temporaryAddress: Address? = null

    @Column(name = "father_name")
    var fatherName: String? = null

    @Column(name = "grandfather_name")
    var grandfatherName: String? = null

    @Column(name = "spouse_name")
    var spouseName: String? = null

    /**
     * Storage key for citizen's photo in the document storage system.
     * This references the actual file in the S3-compatible storage.
     */
    @Column(name = "photo_key")
    var photoKey: String? = null

    /**
     * Storage key for front side of citizenship certificate in the document storage system.
     * This references the actual file in the S3-compatible storage.
     */
    @Column(name = "citizenship_front_key")
    var citizenshipFrontKey: String? = null

    /**
     * Storage key for back side of citizenship certificate in the document storage system.
     * This references the actual file in the S3-compatible storage.
     */
    @Column(name = "citizenship_back_key")
    var citizenshipBackKey: String? = null
}
