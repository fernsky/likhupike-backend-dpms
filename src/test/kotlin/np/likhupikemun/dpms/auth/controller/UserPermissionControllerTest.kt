package np.likhupikemun.dpis.auth.controller

import np.likhupikemun.dpis.auth.controller.base.BasePermissionControllerTest
import np.likhupikemun.dpis.auth.dto.CreateUserDto
import np.likhupikemun.dpis.fixtures.UserTestFixture
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class UserPermissionControllerTest : BasePermissionControllerTest() {
    @Test
    fun `should get user details by id successfully`() {
        val targetUser = createTestUser()

        mockMvc.perform(get("/api/v1/users/${targetUser.id}")
            .header("Authorization", getAuthHeaderForUser(adminUser.email)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User details retrieved successfully"))
    }

    @Test
    fun `should return 404 for non-existent user`() {
        val nonExistentUserId = UUID.randomUUID()

        mockMvc.perform(get("/api/v1/users/$nonExistentUserId")
            .header("Authorization", getAuthHeaderForUser(adminUser.email)))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error.code").value("AUTH_001"))
    }

    private fun createTestUser() = userService.createUser(CreateUserDto(
        email = "target@test.com",
        password = UserTestFixture.DEFAULT_PASSWORD,
        isWardLevelUser = false
    ))
}
