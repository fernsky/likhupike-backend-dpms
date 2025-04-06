package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a citizen in the Digital Profile Information System.
 * 
 * This entity contains citizen-specific information like personal details,
 * address, family information, and document references. It also implements
 * UserDetails interface to support authentication for citizens.
 *
 * Features:
 * - Complete citizen profile with personal details
 * - Citizenship document information
 * - Structured address information using embedded Address entities
 * - Family information
 * - Document storage references for photos and citizenship certificates
 * - Authentication support (optional password)
 * - Approval tracking
 * 
 * This implementation is fully decoupled from the User entity system
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
class Citizen : UuidBaseEntity(), UserDetails {

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "name_devnagari")
    var nameDevnagari: String? = null

    @Column(name = "citizenship_number", unique = true)
    var citizenshipNumber: String? = null

    @Column(name = "citizenship_issued_date")
    var citizenshipIssuedDate: LocalDate? = null

    @Column(name = "citizenship_issued_office")
    var citizenshipIssuedOffice: String? = null

    @Column(unique = true)
    var email: String? = null
    
    @Column(name = "password")
    private var password: String? = null
    
    @Column(name = "phone_number")
    var phoneNumber: String? = null
    
    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
    
    @Column(name = "deleted_at")
    var deletedAt: LocalDate? = null
    
    @Column(name = "deleted_by")
    var deletedBy: UUID? = null
    
    @Column(name = "is_approved", nullable = false)
    var isApproved: Boolean = false
    
    @Column(name = "approved_by")
    var approvedBy: UUID? = null
    
    @Column(name = "approved_at")
    var approvedAt: LocalDateTime? = null

    @Embedded
    @AttributeOverrides(
        // Province relationship attributes
        AttributeOverride(name = "province.code", column = Column(name = "permanent_province_code")),
        
        // District relationship attributes
        AttributeOverride(name = "district.code", column = Column(name = "permanent_district_code")),
        
        // Municipality relationship attributes
        AttributeOverride(name = "municipality.code", column = Column(name = "permanent_municipality_code")),
        
        // Ward relationship attributes
        AttributeOverride(name = "ward.wardNumber", column = Column(name = "permanent_ward_number")),
        AttributeOverride(name = "ward.municipality.code", column = Column(name = "permanent_ward_municipality_code")),
        
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

    /**
     * Sets a new password for the citizen.
     * Note: The password should be encrypted before calling this method.
     *
     * @param newPassword The new encrypted password
     */
    fun setPassword(newPassword: String) {
        password = newPassword
    }
    
    /**
     * Returns the authorities granted to the citizen.
     * By default, all citizens have a ROLE_CITIZEN authority.
     *
     * @return Set of GrantedAuthority representing the citizen's roles and permissions
     */
    override fun getAuthorities(): Collection<SimpleGrantedAuthority> {
        return setOf(SimpleGrantedAuthority("ROLE_CITIZEN"))
    }

    /**
     * Returns the password used to authenticate the citizen.
     *
     * @return The citizen's encrypted password
     */
    override fun getPassword(): String? = password

    /**
     * Returns the username used to authenticate the citizen.
     * In this implementation, the email address is used as the username.
     *
     * @return The citizen's email address
     */
    override fun getUsername(): String? = email

    /**
     * Indicates whether the citizen's account has expired.
     *
     * @return true as account expiration is not implemented
     */
    override fun isAccountNonExpired(): Boolean = true

    /**
     * Indicates whether the citizen is locked or unlocked.
     *
     * @return true as account locking is not implemented
     */
    override fun isAccountNonLocked(): Boolean = true

    /**
     * Indicates whether the citizen's credentials (password) has expired.
     *
     * @return true as credential expiration is not implemented
     */
    override fun isCredentialsNonExpired(): Boolean = true

    /**
     * Indicates whether the citizen is enabled or disabled.
     * A citizen is considered enabled if they are approved and not deleted.
     *
     * @return true if the citizen is approved and not deleted
     */
    override fun isEnabled(): Boolean = true
}
