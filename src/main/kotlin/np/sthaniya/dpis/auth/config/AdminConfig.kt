package np.sthaniya.dpis.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Binds application.yml admin properties to a data class.
 * 
 * During application startup, Spring Boot binds the following YAML structure:
 * ```yaml
 * app.admin:
 *   email: string          # Required: Admin login email
 *   password: string       # Required: Initial admin password (plaintext)
 *   permissions: string[]  # Optional: Defaults to ["*"] for full access
 * ```
 * 
 * @see AdminInitializationService Uses this config to create/update admin user
 */
@ConfigurationProperties(prefix = "app.admin")
data class AdminConfig(
    val email: String,
    val password: String,
    val permissions: Set<String> = setOf("*")  // * means all permissions
)
