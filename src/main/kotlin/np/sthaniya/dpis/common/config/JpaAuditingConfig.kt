package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.context.annotation.Bean
import java.time.Instant
import java.util.Optional

/**
 * Configuration class that enables and configures JPA auditing functionality.
 *
 * This configuration automatically manages entity auditing by providing:
 * - Automatic timestamp recording for creation and modification dates
 * - UTC-based [Instant] timestamps for consistent datetime handling
 * - Integration with Spring Data JPA's auditing framework
 *
 * The configuration uses a custom [DateTimeProvider] to ensure all audit timestamps
 * are recorded using [Instant].
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class JpaAuditingConfig {
    /**
     * Creates a [DateTimeProvider] bean that supplies the current timestamp for JPA auditing.
     *
     * The provider returns the current time as an [Instant], which ensures:
     * - Consistent UTC-based timestamps
     * - Proper timezone handling
     * - High precision timing
     *
     * @return A [DateTimeProvider] that supplies the current [Instant]
     */
    @Bean
    fun auditingDateTimeProvider() = DateTimeProvider { Optional.of(Instant.now()) }
}
