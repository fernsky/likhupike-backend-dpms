package np.likhupikemun.dpis.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@Import(NoOpSecurityConfig::class, TestRouteConfig::class, TestDatabaseConfig::class)
class TestConfig
