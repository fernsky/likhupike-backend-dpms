package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.auth.controller.base.BaseUserControllerTest
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.fixtures.UserTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class UserPermissionControllerTest : BaseUserControllerTest() {
    private lateinit var adminUser: CreateUserDto
    private lateinit var regularUser: CreateUserDto
    private val adminId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        // Create and approve admin user with necessary permissions
        adminUser = CreateUserDto(
            email = "admin@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false,
            permissions = mapOf(
                PermissionType.EDIT_USER to true,
                PermissionType.VIEW_USER to true,
                PermissionType.APPROVE_USER to true
            )
        )
        val createdAdmin = userService.createUser(adminUser)
        userService.approveUser(createdAdmin.id!!, adminId)

        // Create and approve regular user
        regularUser = CreateUserDto(
            email = "user@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        )
        val createdUser = userService.createUser(regularUser)
        userService.approveUser(createdUser.id!!, adminId)
    }

    @Test
    fun `should update user permissions successfully`() {
        // Arrange
        val targetUserId = UUID.randomUUID()
        val targetUser = userService.createUser(CreateUserDto(
            email = "target@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/${targetUser.id}/permissions")
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "permissions": {
                        "VIEW_USER": true,
                        "CREATE_USER": true,
                        "EDIT_USER": false
                    }
                }
            """.trimIndent()))
            .andDo { result -> log.debug("Response: ${result.response.contentAsString}") }
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User permissions updated successfully"))
            .andExpect(jsonPath("$.data.id").value(targetUser.id.toString()))
    }

    @Test
    fun `should return 403 when user lacks EDIT_USER permission`() {
        val targetUser = userService.createUser(CreateUserDto(
            email = "target@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        mockMvc.perform(put("/api/v1/users/${targetUser.id}/permissions")
            .header("Authorization", getAuthHeaderForUser(regularUser.email))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "permissions": {
                        "VIEW_USER": true
                    }
                }
            """.trimIndent()))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_009"))
    }

    @Test
    fun `should return 404 when target user doesn't exist`() {
        val nonExistentUserId = UUID.randomUUID()

        mockMvc.perform(put("/api/v1/users/$nonExistentUserId/permissions")
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "permissions": {
                        "VIEW_USER": true
                    }
                }
            """.trimIndent()))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_001"))
    }

    @Test
    fun `should return 400 for invalid permission type`() {
        val targetUser = userService.createUser(CreateUserDto(
            email = "target@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        mockMvc.perform(put("/api/v1/users/${targetUser.id}/permissions")
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "permissions": {
                        "INVALID_PERMISSION": true
                    }
                }
            """.trimIndent()))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("INVALID_FORMAT"))
    }

    @Test
    fun `should approve user successfully`() {
        // Create target user
        val targetUser = userService.createUser(CreateUserDto(
            email = "target@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        // Act & Assert
        mockMvc.perform(post("/api/v1/users/${targetUser.id}/approve")
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.isApproved").value(true))
            .andExpect(jsonPath("$.message").value("User approved successfully"))
    }

    @Test
    fun `should return 403 when user lacks APPROVE_USER permission`() {
        val targetUser = userService.createUser(CreateUserDto(
            email = "target@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        ))

        mockMvc.perform(post("/api/v1/users/${targetUser.id}/approve")
            .header("Authorization", getAuthHeaderForUser(regularUser.email))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_009"))
    }
}
