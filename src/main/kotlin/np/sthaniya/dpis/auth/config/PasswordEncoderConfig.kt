package np.sthaniya.dpis.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Provides BCrypt [PasswordEncoder] bean for password hashing.
 * Used by Spring Security for password operations.
 *
 * Example:
 * ```kotlin
 * @Autowired
 * private lateinit var passwordEncoder: PasswordEncoder
 *
 * fun hashPassword(raw: String) = passwordEncoder.encode(raw)
 * fun verify(raw: String, hash: String) = passwordEncoder.matches(raw, hash)
 * ```
 *
 * Configures BCrypt password encoder with default strength (10).
 * Used by Spring Security's DaoAuthenticationProvider for password verification.
 * 
 * Implementation details:
 * - Uses BCrypt with salt
 * - Default work factor: 10 rounds
 * - Generates different hashes for same password due to random salt
 * 
 * @see BCryptPasswordEncoder Internal implementation
 * @see DaoAuthenticationProvider Consumer of this bean
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
