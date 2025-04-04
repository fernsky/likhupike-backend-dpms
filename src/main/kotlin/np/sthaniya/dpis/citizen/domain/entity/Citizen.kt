package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.auth.domain.entity.User
import java.time.LocalDate

/**
 * Represents a citizen in the Digital Profile Information System.
 * 
 * This entity extends the [User] class to provide additional citizen-specific information
 * like personal details, address, family information, and document references.
 *
 * Features:
 * - Complete citizen profile with personal details
 * - Citizenship document information
 * - Structured address information using embedded Address entities
 * - Family information
 * - Document storage references for photos and citizenship certificates
 * 
 * This implementation uses JPA inheritance with a JOINED strategy,
 * creating a separate citizens table linked to the users table.
 *
 * @property name Full name of the citizen
 * @property citizenshipNumber Unique citizenship number
 * @property citizenshipIssuedDate Date when citizenship was issued
 * @property citizenshipIssuedOffice Office that issued the citizenship
 * @property permanentAddress Detailed permanent address with province, district, etc.
 * @property temporaryAddress Optional temporary/current address
 * @property fatherName Name of citizen's father
 * @property grandfatherName Name of citizen's grandfather
 * @property spouseName Name of citizen's spouse (if applicable)
 * @property photoKey Storage key for citizen's photo
 * @property citizenshipFrontKey Storage key for front side of citizenship certificate
 * @property citizenshipBackKey Storage key for back side of citizenship certificate
 */
@Entity
@Table(
    name = "citizens",
    indexes = [
        Index(name = "idx_citizens_citizenship_number", columnList = "citizenship_number")
    ]
)
@DiscriminatorValue("CITIZEN")
class Citizen : User() {

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "citizenship_number", nullable = false, unique = true)
    var citizenshipNumber: String? = null

    @Column(name = "citizenship_issued_date", nullable = false)
    var citizenshipIssuedDate: LocalDate? = null

    @Column(name = "citizenship_issued_office", nullable = false)
    var citizenshipIssuedOffice: String? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "province", column = Column(name = "permanent_province", nullable = false)),
        AttributeOverride(name = "district", column = Column(name = "permanent_district", nullable = false)),
        AttributeOverride(name = "municipality", column = Column(name = "permanent_municipality", nullable = false)),
        AttributeOverride(name = "wardNumber", column = Column(name = "permanent_ward_number", nullable = false)),
        AttributeOverride(name = "streetAddress", column = Column(name = "permanent_street_address"))
    )
    var permanentAddress: Address? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "province", column = Column(name = "temporary_province")),
        AttributeOverride(name = "district", column = Column(name = "temporary_district")),
        AttributeOverride(name = "municipality", column = Column(name = "temporary_municipality")),
        AttributeOverride(name = "wardNumber", column = Column(name = "temporary_ward_number")),
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
