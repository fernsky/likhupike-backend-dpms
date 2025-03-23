package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.auth.controller.base.BasePermissionControllerTest
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.fixtures.UserTestFixture
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserPermissionUpdateTest : BasePermissionControllerTest() {
    @Test
    fun `should update user permissions successfully`() {
        val targetUser = createTestUser()

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
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User permissions updated successfully"))
    }

    @Test
    fun `should return 403 when user lacks EDIT_USER permission`() {
        val targetUser = createTestUser()

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
            .andExpect(jsonPath("$.error.code").value("AUTH_009"))
    }

    private fun createTestUser() = userService.createUser(CreateUserDto(
        email = "target@test.com",
        password = UserTestFixture.DEFAULT_PASSWORD,
        isWardLevelUser = false
    ))
}
