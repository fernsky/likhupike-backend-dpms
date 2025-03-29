package np.sthaniya.dpis.auth.controller.base

import np.sthaniya.dpis.common.BaseIntegrationTest
import np.sthaniya.dpis.auth.service.UserService
import np.sthaniya.dpis.auth.security.JwtService
import np.sthaniya.dpis.common.service.EmailService
import np.sthaniya.dpis.auth.repository.PasswordResetOtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.*
import org.slf4j.LoggerFactory
import org.junit.jupiter.api.BeforeEach
import java.util.concurrent.CompletableFuture


abstract class BaseAuthControllerTest : BaseIntegrationTest() {
    protected val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    protected lateinit var userService: UserService
    
    @Autowired
    protected lateinit var jwtService: JwtService

    @MockBean
    protected lateinit var emailService: EmailService

    @MockBean
    protected lateinit var otpRepository: PasswordResetOtpRepository

    @BeforeEach
    open fun setupAuthBase() {
        // Clear all previous interactions and reset mocks
        clearAllMocks()
        // Setup email service mocks
        setupEmailMocks()
    }

    protected fun clearAllMocks() {
        reset(emailService)
        reset(otpRepository)
        clearInvocations(emailService)
        clearInvocations(otpRepository)
    }

    protected fun setupEmailMocks() {
        doNothing().whenever(emailService).sendEmail(any(), any(), any())
        doNothing().whenever(emailService).sendPasswordResetEmail(any(), any())
        doNothing().whenever(emailService).sendWelcomeEmail(any())
        doNothing().whenever(emailService).sendAccountApprovedEmail(any())
        doNothing().whenever(emailService).sendAccountCreatedEmail(any(), any())
        doNothing().whenever(emailService).sendPasswordResetOtp(any(), any())
        doNothing().whenever(emailService).sendPasswordResetConfirmation(any())
        whenever(emailService.sendEmailAsync(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetEmailAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendWelcomeEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendAccountApprovedEmailAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendAccountCreatedEmailAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetOtpAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(null))
        whenever(emailService.sendPasswordResetConfirmationAsync(any())).thenReturn(CompletableFuture.completedFuture(null))
    }
}
