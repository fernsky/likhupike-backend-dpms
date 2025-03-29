package np.sthaniya.dpis.auth.security

import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.security.core.Authentication
import org.slf4j.LoggerFactory
/**
 * Custom security expression handler for method-level security.
 * 
 * Provides custom security expressions for @PreAuthorize and @PostAuthorize annotations.
 * Extends Spring's SecurityExpressionRoot for method security evaluation.
 *
 * Usage:
 * ```kotlin
 * @PreAuthorize("hasPermission('CREATE_USER')")
 * fun createUser() {
 *     // Method only executes if user has CREATE_USER permission
 * }
 * ```
 *
 * @property filterObject Optional object used for filtering collections
 * @property returnObject Object being returned from the secured method
 */
class CustomMethodSecurityExpressionRoot(
    authentication: Authentication
) : SecurityExpressionRoot(authentication), MethodSecurityExpressionOperations {
    
    private val log = LoggerFactory.getLogger(javaClass)
    private var filterObject: Any? = null
    private var returnObject: Any? = null

    /**
     * Evaluates if authenticated user has specified permission.
     * 
     * Automatically prefixes permission with "PERMISSION_" and checks against
     * user's granted authorities.
     *
     * @param permission Permission name without "PERMISSION_" prefix
     * @return true if user has the permission
     */
    fun hasPermission(permission: String): Boolean {
        val requiredPermission = "PERMISSION_$permission"
        log.debug("Checking permission {} against authorities: {}", requiredPermission, authentication.authorities)
        return authentication.authorities.any { it.authority == requiredPermission }
    }

    /**
     * Sets the object to use for collection filtering.
     * Required by MethodSecurityExpressionOperations interface.
     */
    override fun setFilterObject(filterObject: Any?) {
        this.filterObject = filterObject
    }

    /**
     * Gets the current filter object.
     * Required by MethodSecurityExpressionOperations interface.
     */
    override fun getFilterObject(): Any? = filterObject

    /**
     * Sets the method's return object for post-execution security checks.
     * Required by MethodSecurityExpressionOperations interface.
     */
    override fun setReturnObject(returnObject: Any?) {
        this.returnObject = returnObject
    }

    /**
     * Gets the method's return object.
     * Required by MethodSecurityExpressionOperations interface.
     */
    override fun getReturnObject(): Any? = returnObject

    /**
     * Gets the current security expression root.
     * Required by MethodSecurityExpressionOperations interface.
     */
    override fun getThis(): Any? = this
}
