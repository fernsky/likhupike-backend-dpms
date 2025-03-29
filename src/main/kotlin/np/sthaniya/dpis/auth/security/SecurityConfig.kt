package np.sthaniya.dpis.auth.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.auth.config.JwtAuthenticationFilter
import np.sthaniya.dpis.auth.config.RateLimitFilter
import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.slf4j.LoggerFactory

/**
 * Spring Security configuration implementing JWT-based authentication.
 *
 * Implementation Notes:
 * - Uses custom JwtAuthenticationFilter for token validation
 * - Implements stateless session management
 * - Configures route-based security rules
 * - Handles CORS/CSRF configuration
 *
 * Usage Example:
 * ```kotlin
 * // Adding custom security rules
 * http.authorizeHttpRequests {
 *     it.requestMatchers("/api/public/<all>").permitAll()
 *     it.requestMatchers("/api/admin/<all>").hasRole("ADMIN")
 *     it.anyRequest().authenticated()
 * }
 *
 * // Implementing custom authentication
 * http.authenticationProvider(customProvider)
 *     .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
 * ```
 *
 * Technical Details:
 * - JWT filter runs before Spring's UsernamePasswordAuthenticationFilter
 * - Uses custom RequestMatcher for dynamic route validation
 * - Returns 404 for unregistered routes (security by obscurity)
 * - Configures CORS with specific allowed origins/methods
 *
 * Security Considerations:
 * - CSRF disabled for stateless API
 * - Swagger UI conditionally secured
 * - All routes denied by default
 * - Custom authentication entry point
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val routeRegistry: RouteRegistry,
    private val permissionEvaluator: CustomPermissionEvaluator,
    private val rateLimitFilter: RateLimitFilter,
    private val env: Environment
) {
    /**
     * Configures the security filter chain.
     *
     * Chain Order:
     * 1. CORS/CSRF filters
     * 2. Rate limit filter
     * 3. JWT authentication filter
     * 4. Exception handling
     * 5. Route security
     *
     * Performance Notes:
     * - Use antMatcher for better performance on fixed paths
     * - Custom matchers may impact performance on high load
     * - Consider caching route registry results
     *
     * @param http Spring Security builder
     * @return Configured SecurityFilterChain
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val swaggerEnabled = env.getProperty("springdoc.swagger-ui.enabled", Boolean::class.java, false)

        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests {
                it.requestMatchers("/error", "/actuator/health").permitAll()

                // Only permit Swagger paths if enabled
                if (swaggerEnabled) {
                    it.requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll()
                }

                it.requestMatchers(
                    createRequestMatcher { request ->
                        routeRegistry.isPublicRoute(request.servletPath, HttpMethod.valueOf(request.method))
                    }
                ).permitAll()
                it.requestMatchers(
                    createRequestMatcher { request ->
                        routeRegistry.isValidRoute(request.servletPath, HttpMethod.valueOf(request.method))
                    }
                ).authenticated()
                it.anyRequest()
                    .denyAll() // Change from permitAll to denyAll to return 403 for unregistered routes
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling {
                it
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler { request, response, _ ->
                        // Return 404 for all unregistered routes
                        response.status = 404
                    }
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthenticationFilter, RateLimitFilter::class.java)

        return http.build()
    }

    /**
     * Configures CORS settings for cross-origin requests.
     *
     * Configuration Details:
     * - Allows all origins (*) - customize for production
     * - Permits common HTTP methods
     * - Exposes Authorization header
     * - Sets preflight cache to 1 hour
     *
     * Production Considerations:
     * - Replace * with specific origins
     * - Limit exposed headers
     * - Adjust cache duration
     * - Consider environment-specific configs
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Authorization")
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", configuration)
        return source
    }

    /**
     * Creates dynamic request matcher for route validation.
     *
     * Implementation Details:
     * - Lazy evaluation of routes
     * - Supports dynamic path patterns
     * - Integrates with RouteRegistry
     *
     * Performance Impact:
     * - Evaluated per request
     * - Consider caching for high-traffic routes
     * - Use antMatcher for fixed paths instead
     */
    private fun createRequestMatcher(matcher: (HttpServletRequest) -> Boolean): RequestMatcher =
        RequestMatcher { request -> matcher(request) }
}
