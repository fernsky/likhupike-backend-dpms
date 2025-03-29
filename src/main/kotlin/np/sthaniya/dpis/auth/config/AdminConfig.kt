package np.sthaniya.dpis.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for the system administrator account.
 *
 * This class manages the initial admin user configuration, which is used by [AdminInitializationService]
 * to create or update the system administrator account during application startup.
 *
 * Features:
 * - Automatic configuration binding from application properties
 * - Support for default admin account setup
 * - Permission configuration through property files
 *
 * Usage in application.yml:
 * ```yaml
 * app:
 *   admin:
 *     email: "admin@example.com"
 *     password: "securePassword123"
 *     permissions: ["*"]  # Wildcard for all permissions
 * ```
 *
 * @property email The email address for the admin account
 * @property password The initial password for the admin account (should be changed after first login)
 * @property permissions Set of permission strings, where "*" grants all available permissions
 */
@ConfigurationProperties(prefix = "app.admin")
data class AdminConfig(
    val email: String,
    val password: String,
    val permissions: Set<String> = setOf("*")  // * means all permissions
)
