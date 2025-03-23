package np.likhupikemun.dpms.auth.controller.base

import np.likhupikemun.dpms.common.BaseIntegrationTest
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.common.service.EmailService
import np.likhupikemun.dpms.auth.repository.PasswordResetOtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.*
import org.slf4j.LoggerFactory
import org.junit.jupiter.api.BeforeEach

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
    }
}
