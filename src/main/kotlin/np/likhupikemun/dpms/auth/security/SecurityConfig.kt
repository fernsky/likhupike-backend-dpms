package np.likhupikemun.dpis.auth.security

import jakarta.servlet.http.HttpServletRequest
import np.likhupikemun.dpis.auth.config.JwtAuthenticationFilter
import np.likhupikemun.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val routeRegistry: RouteRegistry,
    private val permissionEvaluator: CustomPermissionEvaluator,
    private val env: Environment
) {
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
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

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

    private fun createRequestMatcher(matcher: (HttpServletRequest) -> Boolean): RequestMatcher =
        RequestMatcher { request -> matcher(request) }
}
