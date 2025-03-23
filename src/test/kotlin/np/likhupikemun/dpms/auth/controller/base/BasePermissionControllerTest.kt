package np.likhupikemun.dpms.auth.controller.base

import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.fixtures.UserTestFixture
import np.likhupikemun.dpms.common.service.EmailService
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.*
import java.util.UUID

abstract class BasePermissionControllerTest : BaseUserControllerTest() {
    @MockBean
    protected lateinit var emailService: EmailService

    protected lateinit var adminUser: CreateUserDto
    protected lateinit var regularUser: CreateUserDto
    protected val adminId = UUID.randomUUID()

    @BeforeEach
    fun setupBase() {
        // First reset mocks completely
        reset(emailService)
        
        // Setup mock behavior for all email methods
        setupEmailMocks()
        
        // Then setup users
        setupUsers()
        
        // Clear recorded interactions after setup
        clearInvocations(emailService)
    }

    private fun setupEmailMocks() {
        // Mock all possible email service method calls
        doNothing().whenever(emailService).sendPasswordResetConfirmation(any())
        doNothing().whenever(emailService).sendAccountCreatedEmail(any(), any())
        doNothing().whenever(emailService).sendAccountApprovedEmail(any())
        doNothing().whenever(emailService).sendWelcomeEmail(any())
        doNothing().whenever(emailService).sendEmail(any(), any(), any())
        doNothing().whenever(emailService).sendPasswordResetEmail(any(), any())
        doNothing().whenever(emailService).sendPasswordResetOtp(any(), any())
    }

    private fun setupUsers() {
        // Create and approve admin user
        adminUser = CreateUserDto(
            email = "admin@test.com",
            password = UserTestFixture.DEFAULT_PASSWORD,
            isWardLevelUser = false,
            permissions = mapOf(
                PermissionType.EDIT_USER to true,
                PermissionType.VIEW_USER to true,
                PermissionType.APPROVE_USER to true,
                PermissionType.RESET_USER_PASSWORD to true
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
}
