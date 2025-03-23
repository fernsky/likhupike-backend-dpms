package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.auth.controller.base.BaseAuthControllerTest
import np.likhupikemun.dpms.fixtures.UserTestFixture
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class AuthTokenTest : BaseAuthControllerTest() {
    private val REFRESH_ENDPOINT = "/api/v1/auth/refresh"
    private val LOGOUT_ENDPOINT = "/api/v1/auth/logout"

    @Test
    fun `should refresh token successfully`() {
        // Arrange
        val adminId = UUID.randomUUID()
        val createdUser = userService.createUser(CreateUserDto(
            email = UserTestFixture.REGULAR_USER_EMAIL,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))
        val approvedUser = userService.approveUser(createdUser.id!!, adminId)
        val tokenPair = jwtService.generateTokenPair(approvedUser)

        mockMvc.perform(
            post(REFRESH_ENDPOINT)
                .header("X-Refresh-Token", tokenPair.refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.email").value(approvedUser.email))
            .andExpect(jsonPath("$.data.token").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
    }

    @Test
    fun `should return 401 for invalid refresh token`() {
        mockMvc.perform(
            post(REFRESH_ENDPOINT)
                .header("X-Refresh-Token", "invalid-token")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_012"))
            .andExpect(jsonPath("$.error.message").value("Invalid or expired token"))
    }

    @Test
    fun `should return 401 for expired refresh token`() {
        val user = UserTestFixture.createApprovedUser()
        userService.createUser(CreateUserDto(
            email = user.email!!,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))
        val expiredToken = jwtService.generateExpiredToken(user)

        mockMvc.perform(
            post(REFRESH_ENDPOINT)
                .header("X-Refresh-Token", expiredToken)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_012"))
            .andExpect(jsonPath("$.error.message").value("Invalid or expired token"))
    }

    @Test
    fun `should logout successfully`() {
        val user = UserTestFixture.createApprovedUser()
        val token = jwtService.generateToken(user)

        mockMvc.perform(
            post(LOGOUT_ENDPOINT)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Logged out successfully"))
    }
}
