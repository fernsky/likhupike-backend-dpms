package np.sthaniya.dpis.citizen.dto.projection

import com.fasterxml.jackson.annotation.JsonInclude
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * Projection interface for Citizen entity data retrieval operations.
 *
 * Used in conjunction with Spring Data JPA's query projection mechanism to
 * optimize database queries by selecting only required fields. This interface
 * defines the contract for citizen data projections in search and list operations.
 *
 * Note: All methods may return null if the corresponding field was not included
 * in the projection selection.
 *
 * Implementation note: Concrete implementations should handle field selection
 * and lazy loading of data.
 *
 * @see CitizenProjectionImpl for reference implementation
 * @see CitizenRepositoryCustom.findAllWithProjection
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
interface CitizenProjection {
    /**
     * Returns the citizen's unique identifier.
     * @return The citizen's UUID, or null if not selected
     */
    fun getId(): UUID?

    /**
     * Returns the citizen's name in English.
     * @return The name, or null if not selected
     */
    fun getName(): String?

    /**
     * Returns the citizen's name in Devanagari script.
     * @return The Devanagari name, or null if not selected
     */
    fun getNameDevnagari(): String?

    /**
     * Returns the citizen's citizenship number.
     * @return The citizenship number, or null if not selected
     */
    fun getCitizenshipNumber(): String?

    /**
     * Returns the date when the citizen's citizenship was issued.
     * @return The citizenship issued date, or null if not selected
     */
    fun getCitizenshipIssuedDate(): LocalDate?

    /**
     * Returns the office that issued the citizen's citizenship.
     * @return The citizenship issuing office, or null if not selected
     */
    fun getCitizenshipIssuedOffice(): String?

    /**
     * Returns the citizen's email address.
     * @return The email address, or null if not selected
     */
    fun getEmail(): String?

    /**
     * Returns the citizen's phone number.
     * @return The phone number, or null if not selected
     */
    fun getPhoneNumber(): String?

    /**
     * Returns the current state of the citizen in the verification workflow.
     * @return The citizen state, or null if not selected
     */
    fun getState(): CitizenState?

    /**
     * Returns the note associated with the citizen's current state.
     * @return The state note, or null if not selected
     */
    fun getStateNote(): String?

    /**
     * Returns the timestamp when the citizen's state was last updated.
     * @return The state update timestamp, or null if not selected
     */
    fun getStateUpdatedAt(): Instant?

    /**
     * Returns the ID of the user who last updated the citizen's state.
     * @return The state updater's ID, or null if not selected
     */
    fun getStateUpdatedBy(): UUID?

    /**
     * Returns the approval status of the citizen.
     * @return true if approved, or null if not selected
     */
    fun getIsApproved(): Boolean?

    /**
     * Returns the ID of the administrator who approved the citizen.
     * @return The approver's ID, or null if not approved or not selected
     */
    fun getApprovedBy(): UUID?

    /**
     * Returns the timestamp when the citizen was approved.
     * @return The approval timestamp, or null if not approved or not selected
     */
    fun getApprovedAt(): Instant?

    /**
     * Returns whether the citizen record is deleted.
     * @return true if deleted, or null if not selected
     */
    fun getIsDeleted(): Boolean?

    /**
     * Returns the ID of the administrator who deleted the citizen record.
     * @return The deleter's ID, or null if not deleted or not selected
     */
    fun getDeletedBy(): UUID?

    /**
     * Returns the timestamp when the citizen record was deleted.
     * @return The deletion timestamp, or null if not deleted or not selected
     */
    fun getDeletedAt(): Instant?

    /**
     * Returns the permanent address of the citizen.
     * @return The permanent address, or null if not selected
     */
    fun getPermanentAddress(): Address?

    /**
     * Returns the temporary address of the citizen.
     * @return The temporary address, or null if not selected
     */
    fun getTemporaryAddress(): Address?

    /**
     * Returns the name of the citizen's father.
     * @return The father's name, or null if not selected
     */
    fun getFatherName(): String?

    /**
     * Returns the name of the citizen's grandfather.
     * @return The grandfather's name, or null if not selected
     */
    fun getGrandfatherName(): String?

    /**
     * Returns the name of the citizen's spouse.
     * @return The spouse's name, or null if not selected
     */
    fun getSpouseName(): String?

    /**
     * Returns the storage key for the citizen's photo.
     * @return The photo storage key, or null if not selected
     */
    fun getPhotoKey(): String?

    /**
     * Returns the verification state of the citizen's photo.
     * @return The photo state, or null if not selected
     */
    fun getPhotoState(): DocumentState?

    /**
     * Returns the note associated with the citizen's photo state.
     * @return The photo state note, or null if not selected
     */
    fun getPhotoStateNote(): String?

    /**
     * Returns the storage key for the front of the citizen's citizenship certificate.
     * @return The front citizenship certificate storage key, or null if not selected
     */
    fun getCitizenshipFrontKey(): String?

    /**
     * Returns the verification state of the front of the citizen's citizenship certificate.
     * @return The front citizenship certificate state, or null if not selected
     */
    fun getCitizenshipFrontState(): DocumentState?

    /**
     * Returns the note associated with the front of the citizen's citizenship certificate state.
     * @return The front citizenship certificate state note, or null if not selected
     */
    fun getCitizenshipFrontStateNote(): String?

    /**
     * Returns the storage key for the back of the citizen's citizenship certificate.
     * @return The back citizenship certificate storage key, or null if not selected
     */
    fun getCitizenshipBackKey(): String?

    /**
     * Returns the verification state of the back of the citizen's citizenship certificate.
     * @return The back citizenship certificate state, or null if not selected
     */
    fun getCitizenshipBackState(): DocumentState?

    /**
     * Returns the note associated with the back of the citizen's citizenship certificate state.
     * @return The back citizenship certificate state note, or null if not selected
     */
    fun getCitizenshipBackStateNote(): String?

    /**
     * Returns the creation timestamp of the citizen record.
     * @return The creation timestamp, or null if not selected
     */
    fun getCreatedAt(): Instant?

    /**
     * Returns the last modification timestamp of the citizen record.
     * @return The last update timestamp, or null if not selected
     */
    fun getUpdatedAt(): Instant?
}
