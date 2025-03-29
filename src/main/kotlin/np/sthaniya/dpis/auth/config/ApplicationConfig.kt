package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.auth.repository.UserRepository
import np.sthaniya.dpis.auth.exception.AuthException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Spring Security configuration class that sets up authentication-related beans.
 *
 * This configuration class provides the core authentication components needed for the application:
 * - UserDetailsService for loading user data
 * - AuthenticationProvider for processing authentication requests
 * - AuthenticationManager for managing the authentication process
 *
 * Features:
 * - Custom UserDetailsService implementation using JPA repository
 * - DaoAuthenticationProvider with password encoding
 * - Integration with Spring Security authentication chain
 *
 * Usage:
 * These beans are automatically used by Spring Security's authentication process.
 * No direct usage in application code is typically needed.
 *
 * @property userRepository Repository for accessing user data
 * @property passwordEncoder Bean for encoding and verifying passwords
 */
@Configuration
class ApplicationConfig(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    /**
     * Creates a UserDetailsService that loads user data from the database.
     *
     * This implementation uses [UserRepository] to fetch user details by email
     * and throws [AuthException.UserNotFoundException] if the user doesn't exist.
     *
     * @return UserDetailsService implementation
     * @throws AuthException.UserNotFoundException if user is not found
     */
    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService { username ->
            userRepository.findByEmail(username)
                .orElseThrow { AuthException.UserNotFoundException(username) }
        }

    /**
     * Creates an AuthenticationProvider that uses the custom UserDetailsService.
     *
     * This provider:
     * - Uses the custom UserDetailsService for loading user data
     * - Applies password encoding for password verification
     * - Integrates with Spring Security's authentication mechanism
     *
     * @return Configured DaoAuthenticationProvider
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService())
            setPasswordEncoder(passwordEncoder)
        }

    /**
     * Creates an AuthenticationManager from the Spring Security configuration.
     *
     * This manager coordinates the authentication process using the configured
     * authentication providers.
     *
     * @param config The Spring Security authentication configuration
     * @return Configured AuthenticationManager
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}
