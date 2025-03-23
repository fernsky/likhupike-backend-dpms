package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.auth.controller.base.BasePermissionControllerTest
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.fixtures.UserTestFixture
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.kotlin.*

class UserPasswordResetTest : BasePermissionControllerTest() {
    private lateinit var targetUser: CreateUserDto

    @Test
    fun `should reset password successfully`() {
        // Arrange
        val targetUser = createTestUser()
        val resetRequest = UserTestFixture.createResetPasswordRequest()

        // Act & Assert
        mockMvc.perform(post("/api/v1/users/${targetUser.id}/reset-password")
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(resetRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Password reset successfully"))

        // Verify the correct email was sent
        verify(emailService).sendPasswordResetConfirmation("target@test.com")
    }

    private fun createTestUser() = userService.createUser(CreateUserDto(
        email = "target@test.com",
        password = UserTestFixture.DEFAULT_PASSWORD,
        isWardLevelUser = false
    ))
}
