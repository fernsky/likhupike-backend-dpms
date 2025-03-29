package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.dto.*
import org.springframework.data.domain.Page
import java.util.*

/**
 * Service interface for managing user-related operations.
 *
 * This interface provides methods for creating, updating, and managing users
 * in the system, including their permissions and authentication details.
 */
interface UserService {
    /**
     * Creates a new user in the system.
     *
     * @param createUserDto The data transfer object containing user creation details
     * @return The created [User] entity
     * @throws AuthException if the user creation fails
     */
    fun createUser(createUserDto: CreateUserDto): User

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return The [User] entity if found, null otherwise
     */
    fun findByEmail(email: String): User?

    /**
     * Updates the permissions for a specific user.
     *
     * @param userId The ID of the user whose permissions are being updated
     * @param permissions The new permissions to assign to the user
     * @return The updated [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun updatePermissions(userId: UUID, permissions: UserPermissionsDto): User

    /**
     * Resets a user's password.
     *
     * @param userId The ID of the user whose password is being reset
     * @param newPassword The new password to set
     * @return The updated [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun resetPassword(userId: UUID, newPassword: String): User

    /**
     * Approves a user's account.
     *
     * @param userId The ID of the user being approved
     * @param approvedBy The ID of the user performing the approval
     * @return The updated [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun approveUser(userId: UUID, approvedBy: UUID): User

    /**
     * Marks a user as deleted in the system.
     *
     * @param userId The ID of the user to delete
     * @param deletedBy The ID of the user performing the deletion
     * @return The updated [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun deleteUser(userId: UUID, deletedBy: UUID): User

    /**
     * Searches for users based on specified criteria.
     *
     * @param criteria The search criteria to filter users
     * @return A paginated list of [UserProjection] matching the criteria
     */
    fun searchUsers(criteria: UserSearchCriteria): Page<UserProjection>

    /**
     * Updates a user's details.
     *
     * @param userId The ID of the user to update
     * @param request The request containing updated user details
     * @return The updated [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun updateUser(userId: UUID, request: UpdateUserRequest): User

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve
     * @return The [User] entity
     * @throws AuthException.UserNotFoundException if the user doesn't exist
     */
    fun getUserById(userId: UUID): User
}
