package np.likhupikemun.dpms.auth.controller.base

import np.likhupikemun.dpms.common.BaseIntegrationTest
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.fixtures.UserTestFixture
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory

abstract class BaseUserControllerTest : BaseIntegrationTest() {
    protected val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    protected lateinit var userService: UserService

    @Autowired
    protected lateinit var jwtService: JwtService

    protected fun getAuthHeaderForUser(email: String) = 
        UserTestFixture.createApprovedUser(email = email)
            .let { jwtService.generateToken(it) }
            .let { "Bearer $it" }
}
