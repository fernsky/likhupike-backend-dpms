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

class AuthLoginTest : BaseAuthControllerTest() {
    private val ENDPOINT = "/api/v1/auth/login"

    @Test
    fun `should login approved user successfully`() {
        // Arrange
        val adminId = UUID.randomUUID()
        val createdUser = userService.createUser(CreateUserDto(
            email = UserTestFixture.REGULAR_USER_EMAIL,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))
        val approvedUser = userService.approveUser(createdUser.id!!, adminId)
        val request = UserTestFixture.createLoginRequest(
            email = approvedUser.email!!,
            password = UserTestFixture.DEFAULT_PASSWORD
        )

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Login successful"))
    }

    @Test
    fun `should return 403 when user is not approved`() {
        val user = UserTestFixture.createUser(isApproved = false)
        userService.createUser(CreateUserDto(
            email = user.email!!,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))
        val request = UserTestFixture.createLoginRequest(
            email = user.email!!,
            password = UserTestFixture.DEFAULT_PASSWORD
        )

        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.message").value("User not approved"))
    }

    @Test
    fun `should return 401 for invalid credentials`() {
        val user = UserTestFixture.createApprovedUser()
        userService.createUser(CreateUserDto(
            email = user.email!!,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        val request = UserTestFixture.createLoginRequest(
            email = user.email!!,
            password = "wrong-password"
        )

        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.message").value("Invalid credentials"))
    }

    @Test
    fun `should return 401 for non-existent user`() {
        val request = UserTestFixture.createLoginRequest(
            email = "nonexistent@test.com"
        )

        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.message").value("Invalid credentials"))
    }
}
