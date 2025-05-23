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
 * Sets up Spring Security authentication chain components.
 * 
 * Technical components:
 * 1. UserDetailsService: Loads User entity from DB by email
 * 2. DaoAuthenticationProvider: Validates credentials using BCrypt
 * 3. AuthenticationManager: Coordinates authentication process
 * 
 * Authentication flow:
 * Request -> AuthenticationManager -> DaoAuthenticationProvider -> UserDetailsService -> DB
 * 
 * @throws UserNotFoundException If user email not found in database
 */
@Configuration
class ApplicationConfig(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService { username ->
            userRepository.findByEmail(username)
                .orElseThrow { AuthException.UserNotFoundException(username) }
        }

    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService())
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}
