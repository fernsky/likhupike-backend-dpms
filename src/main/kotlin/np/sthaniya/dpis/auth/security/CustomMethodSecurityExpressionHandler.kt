package np.sthaniya.dpis.auth.security

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.authentication.AuthenticationTrustResolver
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.Authentication
import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.stereotype.Component

/**
 * Custom implementation of [DefaultMethodSecurityExpressionHandler] that provides
 * method-level security expression handling.
 *
 * This handler is responsible for creating security expression roots that evaluate
 * method security expressions. It extends Spring Security's default implementation
 * to provide custom security expression evaluation logic.
 */
@Component
class CustomMethodSecurityExpressionHandler : DefaultMethodSecurityExpressionHandler() {
    private val trustResolver: AuthenticationTrustResolver = AuthenticationTrustResolverImpl()

    /**
     * Creates a custom security expression root for method security evaluation.
     *
     * @param authentication The [Authentication] object representing the current security context
     * @param invocation The [MethodInvocation] object representing the method being invoked
     * @return A [MethodSecurityExpressionOperations] object configured with necessary security components
     */
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
