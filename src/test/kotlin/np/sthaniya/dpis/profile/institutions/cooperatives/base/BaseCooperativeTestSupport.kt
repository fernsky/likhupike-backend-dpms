package np.sthaniya.dpis.profile.institutions.cooperatives.base

import java.util.UUID
import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.dto.CreateUserDto
import np.sthaniya.dpis.auth.repository.UserRepository
import np.sthaniya.dpis.auth.service.UserService
import np.sthaniya.dpis.fixtures.UserTestFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.transaction.annotation.Transactional

/**
 * Base test class for cooperative-related tests Provides helper methods for user creation and
 * authentication
 */
@Transactional
abstract class BaseCooperativeTestSupport : BaseRestDocsTest() {

    @Autowired override lateinit var userService: UserService

    @Autowired protected lateinit var userRepository: UserRepository

    protected var testUser: User? = null
    protected val systemUserId = UUID.randomUUID() // Simulated system user ID for approvals

    @BeforeEach
    open fun setupBaseCooperativeTest() {
        // Clear security context before each test
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        // Clear security context after each test
        SecurityContextHolder.clearContext()
    }

    /** Creates a test user with the specified permissions and sets up the security context */
    protected fun createTestUserWithPermissions(
            email: String = "cooperative-test-user@example.com",
            permissions: List<String>
    ): User {
        // Convert permissions list to map with true values
        val permissionsMap =
                permissions.associate {
                    try {
                        // Try to convert permission string to PermissionType
                        PermissionType.valueOf(it) to true
                    } catch (e: IllegalArgumentException) {
                        // Fallback to a default permission if string doesn't match
                        PermissionType.VIEW_COOPERATIVE to true
                    }
                }

        // Create user with permissions directly
        val createUserDto =
                CreateUserDto(
                        email = email,
                        password = UserTestFixture.DEFAULT_PASSWORD,
                        isWardLevelUser = false,
                        permissions = permissionsMap
                )

        // Create the user
        val user = userService.createUser(createUserDto)

        // Approve the user
        userService.approveUser(user.id!!, systemUserId)

        // Save the reference to use in tests
        testUser = userRepository.findById(user.id!!).orElseThrow()

        // Set up authentication in the security context
        setupAuthenticationContext(testUser!!)

        return testUser!!
    }

    /** Sets up the SecurityContext with the provided user */
    protected fun setupAuthenticationContext(user: User) {
        // Create authentication with just the username as principal to match real-world scenario
        // This fixes the "Principal is not a UserDetails instance" error
        val auth = UsernamePasswordAuthenticationToken(user.email, null, user.authorities)

        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = auth
        SecurityContextHolder.setContext(context)
    }

    /** Creates a common test user with all cooperative permissions */
    protected fun setupTestUserWithAllCooperativePermissions(): User {
        return createTestUserWithPermissions(
                permissions =
                        listOf(
                                "VIEW_COOPERATIVE",
                                "CREATE_COOPERATIVE",
                                "UPDATE_COOPERATIVE",
                                "DELETE_COOPERATIVE",
                                "MANAGE_COOPERATIVE_MEDIA"
                        )
        )
    }

    /** Gets Spring Security authentication for the test user */
    protected fun getAuthForUser(user: User? = testUser): RequestPostProcessor {
        val userToUse = user ?: testUser
        return SecurityMockMvcRequestPostProcessors.user(userToUse?.email ?: "unknown@example.com")
                .password(UserTestFixture.DEFAULT_PASSWORD)
                .roles("USER")
    }

    /** Creates a user with specific permission and returns authentication */
    protected fun getAuthForUserWithPermission(permission: String): RequestPostProcessor {
        val user =
                createTestUserWithPermissions(
                        email = "user-with-$permission@example.com",
                        permissions = listOf(permission)
                )
        return getAuthForUser(user)
    }
}
