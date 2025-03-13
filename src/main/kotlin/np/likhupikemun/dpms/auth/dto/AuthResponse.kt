package np.likhupikemun.dpms.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import np.likhupikemun.dpms.auth.domain.enums.PermissionType

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val permissions: Set<PermissionType>,
    val expiresIn: Long,
    val isWardLevelUser: Boolean = false,
    val wardNumber: Int? = null
)
