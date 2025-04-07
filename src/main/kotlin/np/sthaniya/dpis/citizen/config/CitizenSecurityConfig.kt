package np.sthaniya.dpis.citizen.config

import np.sthaniya.dpis.common.annotation.Public
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 * Security configuration specific to citizen authentication.
 *
 * This class configures Spring Security for citizen-specific authentication
 * using JWT tokens. It sets up routes, filters, authentication providers, and
 * security policies specific to citizen access.
 */
@Configuration
@EnableWebSecurity
class CitizenSecurityConfig(
    private val citizenUserDetailsService: UserDetailsService,
    private val citizenJwtAuthFilter: CitizenJwtAuthenticationFilter,
    private val passwordEncoder: PasswordEncoder,
    private val requestMappingHandlerMapping: RequestMappingHandlerMapping,
    private val citizenRouteRegistry: CitizenRouteRegistry
) {
    /**
     * Configures the security filter chain for citizen authentication.
     */
    @Bean
    @Order(2) // Order is important - this should run after the main security config
    fun citizenSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Find all endpoints annotated with @Public
        val publicEndpoints = findPublicEndpoints()
        
        // Get all citizen routes for security matching
        val allCitizenRoutes = citizenRouteRegistry.getAllRoutes()
            .map { it.toSecurityPattern() }
            .toTypedArray()
        
        // Get public citizen routes
        val publicCitizenRoutes = citizenRouteRegistry.getPublicRoutes()
            .map { it.toSecurityPattern() }
            .toList()
        
        http
            .securityMatcher(*allCitizenRoutes)
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                // Public endpoints annotated with @Public
                publicEndpoints.forEach { path ->
                    auth.requestMatchers(path).permitAll()
                }
                
                // Public routes from registry
                publicCitizenRoutes.forEach { path ->
                    auth.requestMatchers(path).permitAll()
                }
                
                // All other citizen endpoints require authentication
                auth.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(citizenJwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        
        return http.build()
    }

    /**
     * Finds all endpoints annotated with @Public
     */
    private fun findPublicEndpoints(): List<String> {
        val publicEndpoints = mutableListOf<String>()
        
        requestMappingHandlerMapping.handlerMethods.forEach { (mapping, handlerMethod) ->
            // Check if method has @Public annotation
            val publicAnnotation = AnnotationUtils.findAnnotation(handlerMethod.method, Public::class.java)
            if (publicAnnotation != null) {
                mapping.pathPatternsCondition?.patterns?.forEach { pattern ->
                    publicEndpoints.add(pattern.patternString)
                }
            }
            
            // Check if class has @Public annotation
            val controllerPublicAnnotation = AnnotationUtils.findAnnotation(handlerMethod.beanType, Public::class.java)
            if (controllerPublicAnnotation != null && publicAnnotation == null) {
                mapping.pathPatternsCondition?.patterns?.forEach { pattern ->
                    publicEndpoints.add(pattern.patternString)
                }
            }
        }
        
        return publicEndpoints
    }

    /**
     * Configures the authentication provider for citizen authentication.
     */
    // @Bean
    // fun citizenAuthenticationProvider(): AuthenticationProvider {
    //     val authProvider = DaoAuthenticationProvider()
    //     authProvider.setUserDetailsService(citizenUserDetailsService)
    //     authProvider.setPasswordEncoder(passwordEncoder)
    //     return authProvider
    // }

    /**
     * Provides the AuthenticationManager for citizen authentication.
     *
     * @param config The authentication configuration
     * @return The configured AuthenticationManager
     */
    // @Bean(name = ["citizenAuthenticationManager"])  // Give it a specific name to avoid conflicts
    // fun citizenAuthenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
    //     return config.authenticationManager
    // }
}
