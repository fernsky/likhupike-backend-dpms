package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.Instant
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
 * - State management for verification workflow
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
        Index(name = "idx_citizens_phone_number", columnList = "phone_number"),
        Index(name = "idx_citizens_state", columnList = "state")
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
    
    /**
     * Current state of the citizen in the verification workflow.
     * Default state is PENDING_REGISTRATION for self-registered citizens.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    var state: CitizenState = CitizenState.PENDING_REGISTRATION
    
    /**
     * Optional note providing additional context about the current state.
     * Particularly useful for ACTION_REQUIRED and REJECTED states.
     */
    @Column(name = "state_note", length = 500)
    var stateNote: String? = null
    
    /**
     * Timestamp when the state was last updated.
     */
    @Column(name = "state_updated_at")
    var stateUpdatedAt: Instant? = null
    
    /**
     * User who last updated the state.
     */
    @Column(name = "state_updated_by")
    var stateUpdatedBy: UUID? = null
    
    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
    
    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
    
    @Column(name = "deleted_by")
    var deletedBy: UUID? = null
    
    @Column(name = "is_approved", nullable = false)
    var isApproved: Boolean = false
    
    @Column(name = "approved_by")
    var approvedBy: UUID? = null
    
    @Column(name = "approved_at")
    var approvedAt: Instant? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "provinceCode", column = Column(name = "permanent_province_code")),
        AttributeOverride(name = "districtCode", column = Column(name = "permanent_district_code")),
        AttributeOverride(name = "municipalityCode", column = Column(name = "permanent_municipality_code")),
        AttributeOverride(name = "wardNumber", column = Column(name = "permanent_ward_number")),
        AttributeOverride(name = "wardMunicipalityCode", column = Column(name = "permanent_ward_municipality_code")),
        AttributeOverride(name = "streetAddress", column = Column(name = "permanent_street_address"))
    )
    var permanentAddress: Address? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "provinceCode", column = Column(name = "temporary_province_code")),
        AttributeOverride(name = "districtCode", column = Column(name = "temporary_district_code")),
        AttributeOverride(name = "municipalityCode", column = Column(name = "temporary_municipality_code")),
        AttributeOverride(name = "wardNumber", column = Column(name = "temporary_ward_number")),
        AttributeOverride(name = "wardMunicipalityCode", column = Column(name = "temporary_ward_municipality_code")),
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
     * State of the citizen's photo verification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "photo_state")
    var photoState: DocumentState? = null

    /**
     * Note providing context about the photo state.
     */
    @Column(name = "photo_state_note", length = 255)
    var photoStateNote: String? = null

    /**
     * Storage key for front side of citizenship certificate in the document storage system.
     * This references the actual file in the S3-compatible storage.
     */
    @Column(name = "citizenship_front_key")
    var citizenshipFrontKey: String? = null

    /**
     * State of the front citizenship certificate verification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "citizenship_front_state")
    var citizenshipFrontState: DocumentState? = null

    /**
     * Note providing context about the front citizenship certificate state.
     */
    @Column(name = "citizenship_front_state_note", length = 255)
    var citizenshipFrontStateNote: String? = null

    /**
     * Storage key for back side of citizenship certificate in the document storage system.
     * This references the actual file in the S3-compatible storage.
     */
    @Column(name = "citizenship_back_key")
    var citizenshipBackKey: String? = null

    /**
     * State of the back citizenship certificate verification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "citizenship_back_state")
    var citizenshipBackState: DocumentState? = null

    /**
     * Note providing context about the back citizenship certificate state.
     */
    @Column(name = "citizenship_back_state_note", length = 255)
    var citizenshipBackStateNote: String? = null

    /**
     * Updates the state of the citizen in the verification workflow.
     *
     * @param newState The new citizen state
     * @param note Optional note providing context for the state change
     * @param updatedBy ID of the user updating the state
     */
    fun updateState(newState: CitizenState, note: String? = null, updatedBy: UUID) {
        this.state = newState
        this.stateNote = note
        this.stateUpdatedAt = Instant.now()
        this.stateUpdatedBy = updatedBy
        
        // When approving, update approval fields
        if (newState == CitizenState.APPROVED && !this.isApproved) {
            this.isApproved = true
            this.approvedAt = Instant.now()
            this.approvedBy = updatedBy
        }
    }

    /**
     * Updates the state of a citizen document.
     *
     * @param documentType Type of document being updated (PHOTO, CITIZENSHIP_FRONT, CITIZENSHIP_BACK)
     * @param newState The new document state
     * @param note Optional note providing context for the state change
     */
    fun updateDocumentState(documentType: DocumentType, newState: DocumentState, note: String? = null) {
        when (documentType) {
            DocumentType.CITIZEN_PHOTO -> {
                photoState = newState
                photoStateNote = note
            }
            DocumentType.CITIZENSHIP_FRONT -> {
                citizenshipFrontState = newState
                citizenshipFrontStateNote = note
            }
            DocumentType.CITIZENSHIP_BACK -> {
                citizenshipBackState = newState
                citizenshipBackStateNote = note
            }
            else -> throw IllegalArgumentException("Unsupported document type: $documentType")
        }
    }

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
     * @return true if the citizen is not deleted
     */
    // We remove isApproved check from here as it is not a part of UserDetails contract
    // This is to ensure that the citizen can be enabled even if not approved.
    // So that the citizens can interact with the dashboard and handle approval process requirements.
    override fun isEnabled(): Boolean = !isDeleted
}
