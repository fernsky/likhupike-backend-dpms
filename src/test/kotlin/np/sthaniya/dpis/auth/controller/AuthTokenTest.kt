package np.sthaniya.dpis.auth.controller

import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.fixtures.UserTestFixture
import np.sthaniya.dpis.auth.dto.CreateUserDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class AuthTokenTest : BaseRestDocsTest() {
    private val REFRESH_ENDPOINT = "/api/v1/auth/refresh"
    private val LOGOUT_ENDPOINT = "/api/v1/auth/logout"

    @Test
    fun `should refresh token successfully`() {
        // Arrange - Create and approve a user first
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
            .andDo(
                document(
                    "auth-token-refresh-success",
                    requestHeaders(
                        headerWithName("X-Refresh-Token").description("Valid refresh token")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Response data object"),
                        fieldWithPath("data.token").description("New JWT access token"),
                        fieldWithPath("data.refreshToken").description("New JWT refresh token"),
                        fieldWithPath("data.userId").description("Unique identifier of the user"),
                        fieldWithPath("data.email").description("Email address of the user"),
                        fieldWithPath("data.permissions").description("List of user permissions"),
                        fieldWithPath("data.roles").description("List of user roles"),
                        fieldWithPath("data.isWardLevelUser").description("Flag indicating if the user is ward-level"),
                        fieldWithPath("data.expiresIn").description("Access token expiration time in seconds")
                    )
                )
            )
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
        // Arrange - Create and approve a user first
        val adminId = UUID.randomUUID()
        val createdUser = userService.createUser(CreateUserDto(
            email = UserTestFixture.REGULAR_USER_EMAIL,
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))
        val approvedUser = userService.approveUser(createdUser.id!!, adminId)
        val token = jwtService.generateToken(approvedUser)

        mockMvc.perform(
            post(LOGOUT_ENDPOINT)
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Logged out successfully"))
    }
}
