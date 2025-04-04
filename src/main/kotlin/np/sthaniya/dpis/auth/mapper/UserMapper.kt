package np.sthaniya.dpis.auth.mapper

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.dto.UserResponse
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Object responsible for mapping User entity objects to their corresponding DTO representations.
 * 
 * This mapper provides utility functions to convert between domain entities and DTOs,
 * particularly focusing on the conversion of User entities to UserResponse DTOs.
 * It handles the transformation of all user-related data, including:
 * - Basic user information (ID, email)
 * - User permissions
 * - Ward-level access information
 * - Approval status and metadata
 * - Timestamps (created, updated)
 */
object UserMapper {
    /**
     * Converts a single User entity to its DTO representation (UserResponse).
     *
     * This function performs the following transformations:
     * - Maps basic user data (ID, email)
     * - Converts authorities to PermissionType enums
     * - Handles ward-level user information
     * - Processes approval status and related metadata
     * - Converts timestamps from Instant to LocalDateTime
     *
     * @param user The User entity to be converted
     * @return UserResponse DTO containing the mapped user data
     */
    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id!!,
            email = user.email!!,
            permissions =
                user
                    .getAuthorities()
                    .mapNotNull {
                        runCatching {
                            PermissionType.valueOf(it.authority.removePrefix("PERMISSION_"))
                        }.getOrNull()
                    }.toSet(),
            roles = user.getRoles()
                .mapNotNull { role ->
                    runCatching {
                        role.type
                    }.getOrNull()
                }.toSet(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber,
            isApproved = user.isApproved,
            approvedBy = user.approvedBy,
            approvedAt = user.approvedAt,
            createdAt = LocalDateTime.ofInstant(user.createdAt!!, ZoneId.systemDefault()),
            updatedAt = user.updatedAt?.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) },
        )

    /**
     * Converts a list of User entities to a list of UserResponse DTOs.
     *
     * @param users List of User entities to be converted
     * @return List of UserResponse DTOs
     */
    fun toResponseList(users: List<User>): List<UserResponse> = users.map(::toResponse)
}
