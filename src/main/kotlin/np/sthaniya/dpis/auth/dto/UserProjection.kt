package np.sthaniya.dpis.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import java.time.LocalDateTime
import java.util.UUID

/**
 * Projection interface for User entity data retrieval operations.
 *
 * Used in conjunction with Spring Data JPA's query projection mechanism to 
 * optimize database queries by selecting only required fields. This interface
 * defines the contract for user data projections in search and list operations.
 *
 * Note: All methods may return null if the corresponding field was not included
 * in the projection selection.
 *
 * Implementation note: Concrete implementations should handle field selection
 * and lazy loading of data.
 *
 * @see UserProjectionImpl for reference implementation
 * @see UserRepositoryCustom.findAllWithProjection
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
interface UserProjection {
    /**
     * Returns the user's unique identifier.
     * @return The user's UUID, or null if not selected
     */
    fun getId(): UUID?

    /**
     * Returns the user's email address.
     * @return The email address, or null if not selected
     */
    fun getEmail(): String?

    /**
     * Returns the user's assigned permissions.
     * Note: Permissions are converted from Spring Security authorities.
     * @return Set of PermissionType, or null if not selected
     */
    fun getPermissions(): Set<PermissionType>?

    /**
     * Gets the ward-level access status of the user.
     * @return true if user has ward-level access, null if not selected
     */
    fun getIsWardLevelUser(): Boolean?

    /**
     * Gets the assigned ward number for ward-level users.
     * @return Ward number or null if not applicable/selected
     */
    fun getWardNumber(): Int?

    /**
     * Gets the approval status of the user account.
     * @return true if user is approved, null if not selected
     */
    fun getIsApproved(): Boolean?

    /**
     * Gets the ID of the admin who approved the user.
     * @return UUID of approving admin or null if not approved/selected
     */
    fun getApprovedBy(): UUID?

    /**
     * Gets the timestamp when the user was approved.
     * @return Approval timestamp or null if not approved/selected
     */
    fun getApprovedAt(): LocalDateTime?

    /**
     * Gets the creation timestamp of the user account.
     * @return Creation timestamp or null if not selected
     */
    fun getCreatedAt(): LocalDateTime?

    /**
     * Gets the last modification timestamp of the user account.
     * @return Last update timestamp or null if not selected
     */
    fun getUpdatedAt(): LocalDateTime?
}
