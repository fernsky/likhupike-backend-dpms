package np.sthaniya.dpis.config

import np.sthaniya.dpis.auth.security.*
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.Customizer.withDefaults

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .anonymous(withDefaults())
            .headers { it.disable() }
            .sessionManagement { it.disable() }
            .securityContext { it.disable() }
            .exceptionHandling { it.disable() }

        return http.build()
    }

    @Bean
    @Primary
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Primary
    fun authenticationManager(passwordEncoder: PasswordEncoder): AuthenticationManager =
        TestAuthenticationManager(passwordEncoder)

    @Bean
    @Primary
    fun jwtService(): JwtService = TestJwtService()

    @Bean
    @Primary
    fun permissionEvaluator() = CustomPermissionEvaluator()
}
