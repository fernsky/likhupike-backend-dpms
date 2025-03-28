package np.likhupikemun.dpms.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@EnableWebSecurity
class NoOpSecurityConfig {
    
    @Bean
    @Primary
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { it.disable() }
            .sessionManagement { it.disable() }
            .securityContext { it.disable() }

        return http.build()
    }
}
