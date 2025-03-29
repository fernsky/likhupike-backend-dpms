package np.sthaniya.dpis.auth.config

import np.sthaniya.dpis.auth.security.CustomPermissionEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Configures Spring method security with custom permission evaluation.
 * 
 * Technical implementation:
 * - Enables @PreAuthorize/@PostAuthorize processing
 * - Injects CustomPermissionEvaluator into security expression handler
 * - Supports SpEL expressions in security annotations
 * 
 * Usage in controllers:
 * ```kotlin
 * @PreAuthorize("hasPermission('USERS_WRITE')")
 * fun updateUser(..) // Method only executes if permission check passes
 * ```
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig {
    
    @Bean
    fun permissionEvaluator() = CustomPermissionEvaluator()

    @Bean
    @Primary
    fun defaultMethodSecurityExpressionHandler(): DefaultMethodSecurityExpressionHandler {
        return DefaultMethodSecurityExpressionHandler().apply {
            setPermissionEvaluator(permissionEvaluator())
        }
    }
}
