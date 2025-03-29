package np.likhupikemun.dpis.auth.security

import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.security.core.Authentication
import org.slf4j.LoggerFactory

class CustomMethodSecurityExpressionRoot(
    authentication: Authentication
) : SecurityExpressionRoot(authentication), MethodSecurityExpressionOperations {
    private val log = LoggerFactory.getLogger(javaClass)
    private var filterObject: Any? = null
    private var returnObject: Any? = null

    fun hasPermission(permission: String): Boolean {
        val requiredPermission = "PERMISSION_$permission"
        log.debug("Checking permission {} against authorities: {}", requiredPermission, authentication.authorities)
        return authentication.authorities.any { it.authority == requiredPermission }
    }

    override fun setFilterObject(filterObject: Any?) {
        this.filterObject = filterObject
    }

    override fun getFilterObject(): Any? = filterObject

    override fun setReturnObject(returnObject: Any?) {
        this.returnObject = returnObject
    }

    override fun getReturnObject(): Any? = returnObject

    override fun getThis(): Any? = this
}
