package np.likhupikemun.dpms.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@Import(TestSecurityConfig::class, TestRouteConfig::class)
class TestConfig
