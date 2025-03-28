package np.likhupikemun.dpms.common.config

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
import np.likhupikemun.dpms.common.annotation.Public

@Configuration
class OpenAPIConfig(private val env: Environment) {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/v1/auth/**", "/api/v1/public/**")
            .build()
    }

    @Bean
    fun privateApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("private")
            .pathsToMatch("/api/v1/**")
            .addOpenApiMethodFilter { method -> !method.isAnnotationPresent(Public::class.java) }
            .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        val serverUrl = env.getProperty("app.api.url", "/")
        
        return OpenAPI()
            .info(
                Info()
                    .title("Likhupike Digital Profile Management System API")
                    .version("1.0.0")
                    .description("""
                        REST API documentation for Likhupike Digital Profile Management System
                        
                        Authentication:
                        - All private endpoints require Bearer JWT token
                        - Public endpoints are accessible without authentication
                        - Token can be obtained from /api/v1/auth/login endpoint
                    """.trimIndent())
                    .contact(
                        Contact()
                            .name("Likhupike Team")
                            .email("support@likhupikemun.np")
                            .url("https://likhupikemun.np")
                    )
                    .license(
                        License()
                            .name("Private License")
                            .url("https://likhupikemun.np/license")
                    )
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Enter JWT token with 'Bearer ' prefix")
                    )
            )
            .addServersItem(Server().url(serverUrl).description("API Server"))
    }
}
