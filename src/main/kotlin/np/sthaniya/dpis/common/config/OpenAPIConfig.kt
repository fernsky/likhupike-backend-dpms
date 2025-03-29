package np.sthaniya.dpis.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.core.env.Environment
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import np.sthaniya.dpis.common.annotation.Public

/**
 * Configuration class for OpenAPI (Swagger) documentation settings.
 *
 * This class configures the OpenAPI documentation for the application's REST APIs,
 * including security schemes, API grouping, and general API information.
 * The configuration is only active when Swagger UI and API docs are enabled in properties.
 *
 * Required properties:
 * ```yaml
 * springdoc:
 *   api-docs:
 *     enabled: true
 *   swagger-ui:
 *     enabled: true
 * ```
 *
 * @property env Spring environment for accessing application properties
 */
@Configuration
@ConditionalOnProperty(
    value = ["springdoc.api-docs.enabled", "springdoc.swagger-ui.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class OpenAPIConfig(private val env: Environment) {

    companion object {
        private const val SECURITY_SCHEME_NAME = "bearerAuth"
    }

    /**
     * Configures the authentication API documentation group.
     *
     * Groups all authentication-related endpoints under "/api/v1/auth/<all>"
     * and filters them based on the [Public] annotation.
     *
     * @return Configured [GroupedOpenApi] for authentication endpoints
     */
    @Bean
    fun authenticationApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("1. Authentication")
            .pathsToMatch("/api/v1/auth/**")
            .addOpenApiMethodFilter { method -> method.isAnnotationPresent(Public::class.java) }
            .build()
    }

    /**
     * Configures the user management API documentation group.
     *
     * Groups all user management endpoints under "/api/v1/users/<all>".
     *
     * @return Configured [GroupedOpenApi] for user management endpoints
     */
    @Bean
    fun userManagementApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("2. User Management")
            .pathsToMatch("/api/v1/users/**")
            .build()
    }

    /**
     * Creates the main OpenAPI configuration.
     *
     * Configures:
     * - API information and metadata
     * - Security schemes (JWT Bearer authentication)
     * - Server URL from application properties
     * - Contact information and license details
     *
     * @return Configured [OpenAPI] instance
     */
    @Bean
    fun customOpenAPI(): OpenAPI {
        val serverUrl = env.getProperty("app.api.url", "/")

        return OpenAPI()
            .info(
                Info()
                    .title("Digital Profile Information System API")
                    .version("1.0.0")
                    .description("""
                        REST API documentation for Digital Profile Information System.
                        Authentication is required for most endpoints except those marked as public.
                    """.trimIndent())
                    .contact(
                        Contact()
                            .name("Support Team")
                            .email("support@example.com")
                    )
                    .license(
                        License()
                            .name("Private License")
                    )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT token authentication")
                    )
            )
            .addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .addServersItem(Server().url(serverUrl))
    }
}
