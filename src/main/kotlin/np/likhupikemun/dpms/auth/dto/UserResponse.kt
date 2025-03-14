package np.likhupikemun.dpms.auth.dto

import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val permissions: Set<PermissionType>,
    val isWardLevelUser: Boolean,
    val wardNumber: Int?,
    val isApproved: Boolean,
    val approvedBy: UUID?,
    val approvedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
