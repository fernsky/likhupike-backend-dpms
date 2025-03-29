package np.likhupikemun.dpis.auth.controller

import np.likhupikemun.dpis.auth.controller.base.BaseAuthControllerTest
import np.likhupikemun.dpis.fixtures.UserTestFixture
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.kotlin.*
import java.util.concurrent.CompletableFuture


class AuthRegistrationTest : BaseAuthControllerTest() {
    private val ENDPOINT = "/api/v1/auth/register"

    @Test
    fun `should register a new regular user successfully`() {
        // Arrange
        val request = UserTestFixture.createRegisterRequest()
        whenever(emailService.sendWelcomeEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.email").value(request.email))
            .andExpect(jsonPath("$.message").value("Registration successful. Waiting for admin approval."))

        verify(emailService).sendWelcomeEmailAsync(request.email)
    }

    @Test
    fun `should register a ward level user successfully`() {
        // Arrange
        val request = UserTestFixture.createRegisterRequest(
            isWardLevelUser = true,
            wardNumber = 1
        )
        whenever(emailService.sendEmailAsync(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null))

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.email").value(request.email))
            .andExpect(jsonPath("$.message").value("Registration successful. Waiting for admin approval."))

        verify(emailService).sendWelcomeEmailAsync(request.email)
    }

    @Test
    fun `should return 400 when email is invalid`() {
        // Arrange
        val request = UserTestFixture.createRegisterRequest(
            email = "invalid-email"
        )

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.error.message").value("Validation failed"))
            .andExpect(jsonPath("$.error.details.email").value("Please provide a valid email address"))
    }

    @Test
    fun `should return 400 when ward number is missing for ward level user`() {
        // Arrange
        val request = UserTestFixture.createRegisterRequest(
            isWardLevelUser = true,
            wardNumber = null
        )

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.error.details.wardNumberValid").value("Ward number is required for ward level users"))
    }

    @Test
    fun `should return 400 when passwords don't match`() {
        // Arrange
        val request = UserTestFixture.createRegisterRequest(
            password = "Test@123",
            confirmPassword = "DifferentTest@123"
        )

        // Act & Assert
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.error.details.passwordValid").value("Passwords do not match"))
    }

    @Test
    fun `should return 409 when email already exists`() {
        // Arrange
        val existingUser = UserTestFixture.createRegisterRequest()
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingUser.toJson())
        )

        // Act & Assert - Try to register with same email
        mockMvc.perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingUser.toJson())
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_002"))
            .andExpect(jsonPath("$.error.message").value("User already exists"))
    }
}
