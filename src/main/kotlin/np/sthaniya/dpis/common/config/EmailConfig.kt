package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Configuration

/**
 * Configuration class for email service settings.
 *
 * This class serves as a marker for Spring Boot's auto-configuration of email services.
 * All email-related settings are defined in application.yml and automatically configured
 * by Spring Boot's JavaMailSender auto-configuration.
 *
 * Required application.yml properties:
 * ```yaml
 * spring:
 *   mail:
 *     host: smtp.example.com
 *     port: 587
 *     username: your-username
 *     password: your-password
 *     properties:
 *       mail.smtp.auth: true
 *       mail.smtp.starttls.enable: true
 * ```
 */
@Configuration
class EmailConfig {
    // Empty configuration class - all settings moved to application.yml
    // Spring Boot's auto-configuration will handle mail setup
}
