package np.likhupikemun.dpis.auth.controller

import np.likhupikemun.dpis.common.BaseIntegrationTest
import np.likhupikemun.dpis.fixtures.UserTestFixture
import np.likhupikemun.dpis.auth.security.TestJwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import np.likhupikemun.dpis.auth.service.UserService
import np.likhupikemun.dpis.auth.dto.CreateUserDto
import np.likhupikemun.dpis.auth.security.JwtService
import org.slf4j.LoggerFactory
import java.util.UUID
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever  // Add this for when() functionality
import org.mockito.kotlin.mock
import np.likhupikemun.dpis.common.service.EmailService
import np.likhupikemun.dpis.auth.repository.PasswordResetOtpRepository
import np.likhupikemun.dpis.auth.domain.entity.PasswordResetOtp
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@Deprecated("This class has been split into smaller test classes. See AuthRegistrationTest, AuthLoginTest, etc.")
class AuthControllerTest : BaseIntegrationTest() {
    // Leave as deprecated marker or remove completely
}
