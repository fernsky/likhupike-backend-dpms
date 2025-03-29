package np.likhupikemun.dpis.auth.controller.base

import np.likhupikemun.dpis.auth.domain.enums.PermissionType
import np.likhupikemun.dpis.auth.dto.CreateUserDto
import np.likhupikemun.dpis.fixtures.UserTestFixture
import np.likhupikemun.dpis.common.service.EmailService
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.*
import java.util.UUID
import java.util.concurrent.CompletableFuture


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
          whenever(emailService.sendEmailAsync(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetEmailAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendWelcomeEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendAccountApprovedEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendAccountCreatedEmailAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetOtpAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetConfirmationAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
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
