package np.sthaniya.dpis.auth.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.enums.RoleType
import java.time.LocalDateTime
import java.util.*

/**
 * Reference implementation of [UserProjection] that provides field-level data selection.
 * 
 * Implements the projection interface by initializing and caching selected fields
 * from the source User entity on instantiation. Fields not included in [includedFields]
 * are not loaded or cached.
 * 
 * Permissions are converted from Spring Security authorities to [PermissionType] enums
 * during initialization. Invalid or unrecognized authorities are silently ignored.
 *
 * Roles are converted to [RoleType] enums
 * Usage with repository:
 * ```kotlin
 * val projection = UserProjectionImpl(
 *     user = userEntity,
 *     includedFields = setOf("id", "email", "permissions")
 * )
 * ```
 * 
 * @param user Source User entity
 * @param includedFields Set of field names to include in the projection
 * @see UserProjection
 * @see UserRepositoryCustom
 */
class UserProjectionImpl(
    @JsonIgnore private val user: User,
    @JsonIgnore private val includedFields: Set<String>
) : UserProjection {

    private val fields = mutableMapOf<String, Any?>()

    init {
        if ("id" in includedFields) fields["id"] = user.id
        if ("email" in includedFields) fields["email"] = user.email
        if ("permissions" in includedFields) {
            fields["permissions"] = user.getAuthorities()
                .mapNotNull { 
                    runCatching { 
                        PermissionType.valueOf(it.authority.removePrefix("PERMISSION_"))
                    }.getOrNull()
                }.toSet()
        }
        if ("roles" in includedFields) {
            fields["roles"] = user.getRoles()
                 .mapNotNull { role -> 
                    runCatching { 
                        role.type
                    }.getOrNull()
                }.toSet()
        }
        if ("isWardLevelUser" in includedFields) fields["isWardLevelUser"] = user.isWardLevelUser
        if ("wardNumber" in includedFields) fields["wardNumber"] = user.wardNumber
        if ("isApproved" in includedFields) fields["isApproved"] = user.isApproved
        if ("approvedBy" in includedFields) fields["approvedBy"] = user.approvedBy
        if ("approvedAt" in includedFields) fields["approvedAt"] = user.approvedAt
        if ("createdAt" in includedFields) fields["createdAt"] = user.createdAt
        if ("updatedAt" in includedFields) fields["updatedAt"] = user.updatedAt
    }

    override fun getId() = fields["id"] as? UUID
    override fun getEmail() = fields["email"] as? String
    override fun getPermissions() = fields["permissions"] as? Set<PermissionType>
    override fun getRoles() = fields["roles"] as? Set<RoleType>
    override fun getIsWardLevelUser() = fields["isWardLevelUser"] as? Boolean
    override fun getWardNumber() = fields["wardNumber"] as? Int
    override fun getIsApproved() = fields["isApproved"] as? Boolean
    override fun getApprovedBy() = fields["approvedBy"] as? UUID
    override fun getApprovedAt() = fields["approvedAt"] as? LocalDateTime
    override fun getCreatedAt() = fields["createdAt"] as? LocalDateTime
    override fun getUpdatedAt() = fields["updatedAt"] as? LocalDateTime
}
