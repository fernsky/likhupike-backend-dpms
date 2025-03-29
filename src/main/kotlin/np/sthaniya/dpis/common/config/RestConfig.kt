package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

/**
 * Configuration class for REST API settings and CORS policies.
 *
 * This configuration handles:
 * - Spring Data REST repository exposure settings
 * - Cross-Origin Resource Sharing (CORS) configuration
 * - Repository detection strategies
 * - Base path configuration for REST endpoints
 */
@Configuration
class RestConfig {
    /**
     * Creates and configures the [RepositoryRestConfigurer] for the application.
     *
     * The configurer provides:
     * - Disabled default repository exposure for security
     * - Custom base path to avoid endpoint conflicts
     * - Annotated-only repository detection strategy
     * - CORS configuration for API endpoints with:
     *   - Allowed origins: all
     *   - Allowed methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
     *   - Exposed headers: Authorization
     *   - Max age: 1 hour
     *
     * @return A configured [RepositoryRestConfigurer]
     */
    @Bean
    fun repositoryRestConfigurer(): RepositoryRestConfigurer =
        RepositoryRestConfigurer.withConfig { config: RepositoryRestConfiguration, cors: CorsRegistry ->
            // Set base path to a non-used path to avoid conflicts
            config.setBasePath("/api/data-rest-disabled")
            
            // Disable auto-detection of repositories
            config.repositoryDetectionStrategy = 
                RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED
            
            // Disable default exposure completely
            config.disableDefaultExposure()

            cors
                .addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .maxAge(3600)
        }
}
