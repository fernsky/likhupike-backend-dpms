package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.auth.security.CustomPermissionEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Configuration class for method-level security using Spring Security's expression-based annotations.
 *
 * This configuration enables and configures method-level security features including:
 * - @PreAuthorize/@PostAuthorize annotations
 * - Custom permission evaluation through [CustomPermissionEvaluator]
 * - Method security expression handling
 *
 * Features:
 * - Method-level security annotations
 * - Custom permission evaluation logic
 * - Integration with Spring Security's method security infrastructure
 *
 * Usage:
 * ```kotlin
 * @PreAuthorize("hasPermission('CREATE_USER')")
 * fun createUser(user: User) {
 *     // Method is secured and requires CREATE_USER permission
 * }
 * ```
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig {
    
    /**
     * Creates the custom permission evaluator bean for method security.
     *
     * @return CustomPermissionEvaluator instance for evaluating permissions
     */
    @Bean
    fun permissionEvaluator() = CustomPermissionEvaluator()

    /**
     * Configures the method security expression handler with custom permission evaluation.
     *
     * This primary bean ensures that our custom permission evaluator is used for
     * all method security expressions throughout the application.
     *
     * @return Configured DefaultMethodSecurityExpressionHandler
     */
    @Bean
    @Primary
    fun defaultMethodSecurityExpressionHandler(): DefaultMethodSecurityExpressionHandler {
        return DefaultMethodSecurityExpressionHandler().apply {
            setPermissionEvaluator(permissionEvaluator())
        }
    }
}
