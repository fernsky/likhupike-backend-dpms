package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.context.annotation.Bean
import java.time.Instant
import java.util.Optional

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class JpaAuditingConfig {
    @Bean
    fun auditingDateTimeProvider() = DateTimeProvider { Optional.of(Instant.now()) }
}
