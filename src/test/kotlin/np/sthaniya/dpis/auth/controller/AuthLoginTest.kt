package np.sthaniya.dpis.auth.controller

import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.fixtures.UserTestFixture
import np.sthaniya.dpis.auth.dto.CreateUserDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class AuthLoginTest : BaseRestDocsTest() {
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
            .andDo(
                document(
                    "auth-login-success",
                    requestFields(
                        fieldWithPath("email").description("User's email address"),
                        fieldWithPath("password").description("User's password")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Response data object"),
                        fieldWithPath("data.token").description("JWT access token for authenticated requests"),
                        fieldWithPath("data.refreshToken").description("JWT refresh token to get new access tokens"),
                        fieldWithPath("data.userId").description("Unique identifier of the user"),
                        fieldWithPath("data.email").description("Email address of the user"),
                        fieldWithPath("data.permissions").description("List of user permissions"),
                        fieldWithPath("data.isWardLevelUser").description("Flag indicating if the user is ward-level"),
                        fieldWithPath("data.expiresIn").description("Access token expiration time in seconds")
                    )
                )
            )
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
            .andDo(
                document(
                    "auth-login-user-not-approved",
                    requestFields(
                        fieldWithPath("email").description("User's email address"),
                        fieldWithPath("password").description("User's password")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful (false for error)"),
                        fieldWithPath("error").description("Error information"),
                        fieldWithPath("error.code").description("Error code (AUTH_011)"),
                        fieldWithPath("error.message").description("Error description"),
                        fieldWithPath("error.status").description("HTTP status code (403)"),
                        fieldWithPath("error.details").description("Additional error details (if any)")
                    )
                )
            )
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
            .andDo(
                document(
                    "auth-login-invalid-credentials",
                    requestFields(
                        fieldWithPath("email").description("User's email address"),
                        fieldWithPath("password").description("User's password (incorrect)")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful (false for error)"),
                        fieldWithPath("error").description("Error information"),
                        fieldWithPath("error.code").description("Error code (AUTH_010)"),
                        fieldWithPath("error.message").description("Error description"),
                        fieldWithPath("error.status").description("HTTP status code (401)"),
                        fieldWithPath("error.details").description("Additional error details (if any)")
                    )
                )
            )
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
            .andDo(
                document(
                    "auth-login-user-not-found",
                    requestFields(
                        fieldWithPath("email").description("Non-existent user's email address"),
                        fieldWithPath("password").description("User's password")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful (false for error)"),
                        fieldWithPath("error").description("Error information"),
                        fieldWithPath("error.code").description("Error code (AUTH_010)"),
                        fieldWithPath("error.message").description("Error description"),
                        fieldWithPath("error.status").description("HTTP status code (401)"),
                        fieldWithPath("error.details").description("Additional error details (if any)")
                    )
                )
            )
    }
}
