package np.likhupikemun.dpis.auth.config

import np.likhupikemun.dpis.auth.security.CustomPermissionEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

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
