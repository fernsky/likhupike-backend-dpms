package np.sthaniya.dpis.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@TestConfiguration
@ActiveProfiles("test")
@Import(NoOpSecurityConfig::class, TestRouteConfig::class, TestDatabaseConfig::class, MinioTestConfig::class)
class TestConfig
