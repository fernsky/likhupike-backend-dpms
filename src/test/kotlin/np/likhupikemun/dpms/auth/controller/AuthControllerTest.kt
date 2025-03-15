package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.common.BaseIntegrationTest
import np.likhupikemun.dpms.fixtures.UserTestFixture
import np.likhupikemun.dpms.auth.security.TestJwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.auth.security.JwtService
import org.slf4j.LoggerFactory
import java.util.UUID

class AuthControllerTest : BaseIntegrationTest() {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var userService: UserService
    
    @Autowired
    private lateinit var jwtService: JwtService

    @Nested
    @DisplayName("POST /api/v1/auth/register")
    inner class Register {
        private val ENDPOINT = "/api/v1/auth/register"

        @Test
        fun `should register a new regular user successfully`() {
            // Arrange
            val request = UserTestFixture.createRegisterRequest()

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
        }

        @Test
        fun `should register a ward level user successfully`() {
            // Arrange
            val request = UserTestFixture.createRegisterRequest(
                isWardLevelUser = true,
                wardNumber = 1
            )

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
                .andExpect(jsonPath("$.error.status").value(400))
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
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.details.wardNumberValid").value("Ward number is required for ward level users"))
                .andExpect(jsonPath("$.error.status").value(400))
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
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.details.passwordValid").value("Passwords do not match"))
                .andExpect(jsonPath("$.error.status").value(400))
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/login")
    inner class Login {
        private val ENDPOINT = "/api/v1/auth/login"

        @Test
        fun `should login approved user successfully`() {
            // Arrange
            val userId = UUID.randomUUID()
            val adminId = UUID.randomUUID() // ID for admin who approves
            
            // Create unapproved user first
            val createdUser = userService.createUser(CreateUserDto(
                email = UserTestFixture.REGULAR_USER_EMAIL,
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))
            log.debug("Created test user: $createdUser")

            // Approve the user using the service
            val approvedUser = userService.approveUser(createdUser.id!!, adminId)
            log.debug("Approved user: $approvedUser")

            val request = UserTestFixture.createLoginRequest(
                email = approvedUser.email!!,
                password = UserTestFixture.DEFAULT_PASSWORD
            )
            log.debug("Login request: $request")

            // Act & Assert
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toJson())
            )
                .andDo { result -> 
                    log.debug("Response: ${result.response.contentAsString}")
                }
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(request.email))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.message").value("Login successful"))
        }

        @Test
        fun `should return 403 when user is not approved`() {
            // Arrange
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

            // Act & Assert
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
            // Arrange
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

            // Act & Assert
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
            // Arrange
            val request = UserTestFixture.createLoginRequest(
                email = "nonexistent@test.com"
            )

            // Act & Assert
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

    @Nested
    @DisplayName("POST /api/v1/auth/refresh")
    inner class RefreshToken {
        private val ENDPOINT = "/api/v1/auth/refresh"

        @Test
        fun `should refresh token successfully`() {
            // Arrange
            val adminId = UUID.randomUUID()
            
            // Create and approve the user first
            val createdUser = userService.createUser(CreateUserDto(
                email = UserTestFixture.REGULAR_USER_EMAIL,
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))
            val approvedUser = userService.approveUser(createdUser.id!!, adminId)
            
            // Generate tokens for the approved user
            val tokenPair = jwtService.generateTokenPair(approvedUser)

            // Act & Assert
            mockMvc.perform(
                post(ENDPOINT)
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
                post(ENDPOINT)
                    .header("X-Refresh-Token", "invalid-token")
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH_012"))
                .andExpect(jsonPath("$.error.message").value("Invalid or expired token"))
                .andExpect(jsonPath("$.error.status").value(401))
        }

        @Test
        fun `should return 401 for expired refresh token`() {
            // Arrange
            val user = UserTestFixture.createApprovedUser()
            userService.createUser(CreateUserDto(
                email = user.email!!,
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))
            val expiredToken = jwtService.generateExpiredToken(user)

            // Act & Assert
            mockMvc.perform(
                post(ENDPOINT)
                    .header("X-Refresh-Token", expiredToken)
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH_012"))
                .andExpect(jsonPath("$.error.message").value("Invalid or expired token"))
                .andExpect(jsonPath("$.error.status").value(401))
        }

    }

    @Nested
    @DisplayName("POST /api/v1/auth/password-reset/request")
    inner class RequestPasswordReset {
        private val ENDPOINT = "/api/v1/auth/password-reset/request"

        @Test
        fun `should process password reset request for existing user`() {
            // Arrange
            val user = UserTestFixture.createApprovedUser()
            userService.createUser(CreateUserDto(
                email = user.email!!,
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))

            // Act & Assert
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "${user.email}"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password reset email sent"))
        }

        @Test
        fun `should return 404 for non-existent user`() {
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "nonexistent@test.com"}""")
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH_001"))
                .andExpect(jsonPath("$.error.message").value("User not found"))
                .andExpect(jsonPath("$.error.status").value(404))
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/password-reset/reset")
    inner class ResetPassword {
        private val ENDPOINT = "/api/v1/auth/password-reset/reset"

        @Test
        fun `should reset password with valid token`() {
            // Arrange
            val user = UserTestFixture.createApprovedUser()
            val createdUser = userService.createUser(CreateUserDto(
                email = user.email!!,
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))

            // Trigger password reset request first to store token in AuthService
            mockMvc.perform(
                post("/api/v1/auth/password-reset/request")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "${user.email}"}""")
            ).andExpect(status().isOk)

            // Get the reset token from JWT service
            val testJwtService = jwtService as TestJwtService
            val resetToken = testJwtService.getResetTokenForEmail(user.email!!)
            log.debug("Generated reset token: $resetToken")

            Thread.sleep(100) // Small delay to ensure token is registered

            // Act & Assert
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "token": "$resetToken",
                            "newPassword": "NewPassword@123",
                            "confirmPassword": "NewPassword@123"
                        }
                    """.trimIndent())
            )
                .andDo { result -> log.debug("Response: ${result.response.contentAsString}") }
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password reset successful"))
        }

        @Test
        fun `should return 400 when passwords don't match`() {
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "token": "any-token",
                            "newPassword": "Password1@",
                            "confirmPassword": "DifferentPassword1@"
                        }
                    """.trimIndent())
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH_013"))
                .andExpect(jsonPath("$.error.message").value("Passwords do not match"))
                .andExpect(jsonPath("$.error.status").value(400))
        }

        @Test
        fun `should return 400 for invalid reset token`() {
            mockMvc.perform(
                post(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "token": "invalid-token",
                            "newPassword": "NewPassword@123",
                            "confirmPassword": "NewPassword@123"
                        }
                    """.trimIndent())
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH_015"))
                .andExpect(jsonPath("$.error.message").value("Password reset token is invalid or expired"))
                .andExpect(jsonPath("$.error.status").value(400))
        }
    }
}