package np.sthaniya.dpis.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuration class for password encryption in the application.
 *
 * This configuration provides a singleton [PasswordEncoder] bean using BCrypt hashing algorithm.
 * The encoder is used throughout the application for:
 * - Encrypting new user passwords
 * - Verifying password during authentication
 * - Password change operations
 *
 * Features:
 * - BCrypt hashing algorithm
 * - Automatic salt generation
 * - Default strength factor (10 rounds)
 * - Integration with Spring Security authentication
 *
 * Usage:
 * ```kotlin
 * @Autowired
 * private lateinit var passwordEncoder: PasswordEncoder
 *
 * fun validatePassword(rawPassword: String, encodedPassword: String): Boolean {
 *     return passwordEncoder.matches(rawPassword, encodedPassword)
 * }
 *
 * fun encodePassword(rawPassword: String): String {
 *     return passwordEncoder.encode(rawPassword)
 * }
 * ```
 *
 * Note: This encoder is used by [ApplicationConfig] for configuring
 * Spring Security's authentication provider.
 */
@Configuration
class PasswordEncoderConfig {
    
    /**
     * Creates a BCrypt password encoder bean.
     *
     * @return PasswordEncoder implementation using BCrypt hashing
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
