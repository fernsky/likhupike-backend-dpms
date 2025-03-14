package np.likhupikemun.dpms.auth.mapper

import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.dto.UserResponse
import java.time.LocalDateTime
import java.time.ZoneId

object UserMapper {
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
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber,
            isApproved = user.isApproved,
            approvedBy = user.approvedBy,
            approvedAt = user.approvedAt,
            createdAt = LocalDateTime.ofInstant(user.createdAt!!, ZoneId.systemDefault()),
            updatedAt = user.updatedAt?.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) },
        )

    fun toResponseList(users: List<User>): List<UserResponse> = users.map(::toResponse)
}
