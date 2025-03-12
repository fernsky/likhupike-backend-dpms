package np.likhupikemun.dpms.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.admin")
data class AdminConfig(
    val email: String,
    val password: String,
    val permissions: Set<String> = setOf("*")  // * means all permissions
)
