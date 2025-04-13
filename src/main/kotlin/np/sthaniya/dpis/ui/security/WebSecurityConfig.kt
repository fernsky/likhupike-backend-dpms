package np.sthaniya.dpis.ui.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Web security configuration specifically for UI routes.
 * 
 * This configuration:
 * 1. Handles form-based authentication for the UI
 * 2. Sets up appropriate redirects and error pages
 * 3. Configures CSRF protection for web forms
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    @Order(2) // Execute after API security (which is order 1)
    fun webSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/ui/**", "/webjars/**", "/css/**", "/js/**", "/images/**") // Only apply to UI routes
            // Configure proper CSRF protection for web forms
            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/ui",
                    "/ui/",
                    "/ui/login",
                    "/ui/register",
                    "/ui/password-reset",
                    "/ui/password-reset/confirm",
                    "/webjars/**",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .formLogin {
                it.loginPage("/ui/login")
                  .loginProcessingUrl("/ui/login") // Process login at the same URL as the form action
                  .defaultSuccessUrl("/ui/dashboard")
                  .failureUrl("/ui/login?error=true")
                  .permitAll()
            }
            .logout {
                it.logoutRequestMatcher(AntPathRequestMatcher("/ui/logout"))
                  .logoutSuccessUrl("/ui/login?logout=true")
                  .invalidateHttpSession(true)
                  .deleteCookies("JSESSIONID")
                  .permitAll()
            }
            .exceptionHandling {
                it.accessDeniedPage("/ui/access-denied")
            }
            // Use session-based auth for the UI (different from the stateless API)
            .sessionManagement { session ->
                session.maximumSessions(1)
                    .expiredUrl("/ui/login?expired=true")
            }
            
        return http.build()
    }
    
    /**
     * Configure authentication manager with user details service
     */
    @Bean
    fun authenticationManagerForUi(http: HttpSecurity): AuthenticationManager {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
        return authManagerBuilder.build()
    }
}
