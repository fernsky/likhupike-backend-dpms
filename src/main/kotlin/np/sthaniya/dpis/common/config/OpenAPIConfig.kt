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

    @Bean
    fun authenticationApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("1. Authentication")
            .pathsToMatch("/api/v1/auth/**")
            .addOpenApiMethodFilter { method -> method.isAnnotationPresent(Public::class.java) }
            .build()
    }

    @Bean
    fun userManagementApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("2. User Management")
            .pathsToMatch("/api/v1/users/**")
            .build()
    }

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
