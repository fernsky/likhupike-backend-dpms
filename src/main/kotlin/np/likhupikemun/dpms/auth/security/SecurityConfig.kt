package np.likhupikemun.dpms.auth.security

import jakarta.servlet.http.HttpServletRequest
import np.likhupikemun.dpms.auth.config.JwtAuthenticationFilter
import np.likhupikemun.dpms.common.config.RouteRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val routeRegistry: RouteRegistry,
    private val permissionEvaluator: CustomPermissionEvaluator
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/error", "/actuator/health")
                    .permitAll()
                    .requestMatchers(
                        createRequestMatcher { request ->
                            routeRegistry.isPublicRoute(request.servletPath, HttpMethod.valueOf(request.method))
                        },
                    ).permitAll()
                    .requestMatchers(
                        createRequestMatcher { request ->
                            routeRegistry.isValidRoute(request.servletPath, HttpMethod.valueOf(request.method))
                        },
                    ).authenticated()
                    .anyRequest()
                    .permitAll()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.exceptionHandling {
                it
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler { request, response, _ ->
                        val exception = object : AuthenticationException("Access Denied") {}
                        customAuthenticationEntryPoint.commence(request, response, exception)
                    }
            }.authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    private fun createRequestMatcher(matcher: (HttpServletRequest) -> Boolean): RequestMatcher =
        RequestMatcher { request -> matcher(request) }
}