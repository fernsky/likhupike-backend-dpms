package np.likhupikemun.dpms.auth.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import java.time.LocalDateTime
import java.util.*

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
    override fun getIsWardLevelUser() = fields["isWardLevelUser"] as? Boolean
    override fun getWardNumber() = fields["wardNumber"] as? Int
    override fun getIsApproved() = fields["isApproved"] as? Boolean
    override fun getApprovedBy() = fields["approvedBy"] as? UUID
    override fun getApprovedAt() = fields["approvedAt"] as? LocalDateTime
    override fun getCreatedAt() = fields["createdAt"] as? LocalDateTime
    override fun getUpdatedAt() = fields["updatedAt"] as? LocalDateTime
}
