package np.sthaniya.dpis.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
interface UserProjection {
    fun getId(): UUID?

    fun getEmail(): String?

    fun getPermissions(): Set<PermissionType>?

    fun getIsWardLevelUser(): Boolean?

    fun getWardNumber(): Int?

    fun getIsApproved(): Boolean?

    fun getApprovedBy(): UUID?

    fun getApprovedAt(): LocalDateTime?

    fun getCreatedAt(): LocalDateTime?

    fun getUpdatedAt(): LocalDateTime?
}
