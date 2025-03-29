package np.sthaniya.dpis.auth.controller

import np.sthaniya.dpis.auth.controller.base.BaseAuthControllerTest
import np.sthaniya.dpis.fixtures.UserTestFixture
import np.sthaniya.dpis.auth.domain.entity.PasswordResetOtp
import np.sthaniya.dpis.auth.dto.CreateUserDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture


class AuthPasswordResetTest : BaseAuthControllerTest() {
    private val REQUEST_ENDPOINT = "/api/v1/auth/password-reset/request"
    private val RESET_ENDPOINT = "/api/v1/auth/password-reset/reset"
    private val TEST_OTP = "123456"
    private val TEST_EMAIL = "test@example.com"

    @Test
    fun `should process password reset request for existing user`() {
        // Create user first
        createTestUser()
        
        whenever(emailService.sendPasswordResetOtpAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))

        mockMvc.perform(
            post(REQUEST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "$TEST_EMAIL"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Password reset email sent"))

        verify(emailService).sendPasswordResetOtpAsync(eq(TEST_EMAIL), any())
    }

    @Test
    fun `should reset password with valid OTP`() {
        // Create user first
        createTestUser()

        val otpEntity = PasswordResetOtp().apply {
            email = TEST_EMAIL
            otp = TEST_OTP
            expiresAt = LocalDateTime.now().plusMinutes(15)
            isUsed = false
            attempts = 0
        }
        
        whenever(otpRepository.findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
            eq(TEST_EMAIL),
            eq(TEST_OTP),
            any()
        )).thenReturn(otpEntity)

        whenever(emailService.sendPasswordResetConfirmationAsync(any())).thenReturn(CompletableFuture.completedFuture(null))

        mockMvc.perform(
            post(RESET_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "$TEST_EMAIL",
                        "otp": "$TEST_OTP",
                        "newPassword": "NewPassword@123",
                        "confirmPassword": "NewPassword@123"
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Password reset successful"))

        verify(otpRepository).save(argThat { 
            isUsed && attempts >= 1 
        })
        verify(emailService).sendPasswordResetConfirmationAsync(TEST_EMAIL)
    }

    @Test
    fun `should return 400 when OTP attempts exceeded`() {
        // Arrange
        val email = "test@example.com"
        val otpEntity = PasswordResetOtp().apply {
            this.email = email
            otp = TEST_OTP
            expiresAt = LocalDateTime.now().plusMinutes(15)
            isUsed = false
            attempts = 3
        }
        
        whenever(otpRepository.findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
            eq(email),
            eq(TEST_OTP),
            any()
        )).thenReturn(otpEntity)

        // Act & Assert
        mockMvc.perform(
            post(RESET_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "$email",
                        "otp": "$TEST_OTP",
                        "newPassword": "NewPassword@123",
                        "confirmPassword": "NewPassword@123"
                    }
                """.trimIndent())
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_017"))
            .andExpect(jsonPath("$.error.message").value("Too many invalid attempts"))
    }

    @Test
    fun `should return 400 when passwords don't match`() {
        mockMvc.perform(
            post(RESET_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "test@example.com",
                        "otp": "$TEST_OTP",
                        "newPassword": "Password1@",
                        "confirmPassword": "DifferentPassword1@"
                    }
                """.trimIndent())
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_016"))
            .andExpect(jsonPath("$.error.message").value("Passwords do not match"))
    }

    private fun createTestUser() = userService.createUser(CreateUserDto(
        email = TEST_EMAIL,
        password = UserTestFixture.DEFAULT_PASSWORD,
        isWardLevelUser = false
    ))
}
