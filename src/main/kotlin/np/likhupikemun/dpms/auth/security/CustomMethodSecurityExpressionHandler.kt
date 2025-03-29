package np.likhupikemun.dpis.auth.security

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.authentication.AuthenticationTrustResolver
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.Authentication
import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.stereotype.Component

@Component
class CustomMethodSecurityExpressionHandler : DefaultMethodSecurityExpressionHandler() {
    private val trustResolver: AuthenticationTrustResolver = AuthenticationTrustResolverImpl()

    override fun createSecurityExpressionRoot(
        authentication: Authentication,
        invocation: MethodInvocation
    ): MethodSecurityExpressionOperations {
        val root = CustomMethodSecurityExpressionRoot(authentication)
        root.setTrustResolver(trustResolver)
        root.setPermissionEvaluator(permissionEvaluator)
        root.setRoleHierarchy(roleHierarchy)
        return root
    }
}
