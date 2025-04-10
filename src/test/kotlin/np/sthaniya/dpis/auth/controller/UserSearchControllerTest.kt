package np.sthaniya.dpis.auth.controller

import np.sthaniya.dpis.auth.controller.base.BaseUserControllerTest
import np.sthaniya.dpis.auth.dto.CreateUserDto
import np.sthaniya.dpis.auth.dto.UserSearchCriteria
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.fixtures.UserTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import np.sthaniya.dpis.common.service.EmailService
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.concurrent.CompletableFuture


class UserSearchControllerTest : BaseUserControllerTest() {
    @MockBean
    private lateinit var emailService: EmailService

    private val ENDPOINT = "/api/v1/users/search"
    private lateinit var adminUser: CreateUserDto
    private val adminId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        // Mock email service methods before creating users
        whenever(emailService.sendAccountCreatedEmailAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendAccountApprovedEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendWelcomeEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        
        // Clean up existing data
        cleanupTestData()
        
        // Create and approve admin user with necessary permissions
        adminUser = CreateUserDto(
            email = "admin@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false,
            permissions = mapOf(
                PermissionType.VIEW_USER to true,
                PermissionType.EDIT_USER to true
            )
        )
        val createdAdmin = userService.createUser(adminUser)
        userService.approveUser(createdAdmin.id!!, adminId)
    }

    @Test
    fun `should search users with valid permissions`() {
        // Create test users
        createTestUsers()

        mockMvc.perform(get(ENDPOINT)
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .param("page", "1")  // Changed from 0 to 1
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> log.debug("Response: ${result.response.contentAsString}") }
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.meta.totalElements").value(6))
            .andExpect(jsonPath("$.meta.page").value(1))  // Expecting page 1 instead of 0
    }

    @Test
    fun `should filter users by email pattern`() {
        // Create and approve test users
        createTestUsers()

        mockMvc.perform(get(ENDPOINT)
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .param("email", "test3@example.com")  // Use full email
            .param("page", "1")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].email").value("test3@example.com"))
    }

    @Test
    fun `should filter users by ward number`() {
        // Create ward users
        (1..3).forEach { wardNum ->
            val wardUser = userService.createUser(CreateUserDto(
                email = "ward$wardNum@test.com",
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = true,
                wardNumber = wardNum
            ))
            // Approve the ward users
            userService.approveUser(wardUser.id!!, adminId)
        }

        mockMvc.perform(get(ENDPOINT)
            .header("Authorization", getAuthHeaderForUser(adminUser.email))
            .param("wardNumber", "2")    // Changed to single wardNumber parameter
            .param("page", "1")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> log.debug("Response: ${result.response.contentAsString}") }
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].wardNumber").value(2))
            .andExpect(jsonPath("$.data[0].isWardLevelUser").value(true))
    }

    @Test
    fun `should return 403 when user lacks VIEW_USER permission`() {
        val regularUser = CreateUserDto(
            email = "regular@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false
        )
        userService.createUser(regularUser)

        mockMvc.perform(get(ENDPOINT)
            .header("Authorization", getAuthHeaderForUser(regularUser.email))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_009"))
    }

    private fun createTestUsers() {
        // Create exactly 5 test users
        (1..5).forEach { i ->
            val user = userService.createUser(CreateUserDto(
                email = "test$i@example.com",
                password = UserTestFixture.DEFAULT_PASSWORD,
                isWardLevelUser = false
            ))
            userService.approveUser(user.id!!, adminId)
        }
    }

    private fun cleanupTestData() {
        val criteria = UserSearchCriteria(page = 1, size = 100)  // Changed from 0 to 1
        userService.searchUsers(criteria).content.forEach { projection ->
            try {
                // Get ID safely using string interpolation of nullable property
                val userId = projection.getId().toString() ?: return@forEach
                userService.deleteUser(UUID.fromString(userId), UUID.fromString(userId))
            } catch (e: Exception) {
                log.warn("Failed to delete test user: ${e.message}")
            }
        }
    }
}

// Add interface for projection to handle nullable ID
interface UserProjection {
    fun getId(): UUID?
    fun getEmail(): String?
    fun getIsWardLevelUser(): Boolean?
    fun getWardNumber(): Int?
    fun getIsApproved(): Boolean?
}
