package np.sthaniya.dpis.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

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
class WebSecurityConfig {

    @Bean
    @Order(2) // Execute after API security (which is order 1)
    fun webSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/ui/**") // Only apply to UI routes
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
                  .loginProcessingUrl("/ui/login")
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
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/api/**") // Disable for API
            }
            .exceptionHandling {
                it.accessDeniedPage("/ui/access-denied")
            }
            
        return http.build()
    }
}
