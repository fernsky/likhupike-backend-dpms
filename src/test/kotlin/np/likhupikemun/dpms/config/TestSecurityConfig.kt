package np.likhupikemun.dpms.config

import np.likhupikemun.dpms.auth.security.CustomAuthenticationEntryPoint
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.auth.security.TestJwtService
import np.likhupikemun.dpms.common.config.RouteRegistry
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import com.fasterxml.jackson.databind.ObjectMapper

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {
    
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        objectMapper: ObjectMapper,
        routeRegistry: RouteRegistry
    ): SecurityFilterChain {
        val authEntryPoint = CustomAuthenticationEntryPoint(objectMapper, routeRegistry)
        
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(authEntryPoint)
                it.accessDeniedHandler { request, response, _ ->
                    authEntryPoint.commence(
                        request, 
                        response, 
                        object : AuthenticationException("Access denied") {}
                    )
                }
            }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }

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
}