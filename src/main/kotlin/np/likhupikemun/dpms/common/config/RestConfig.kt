package np.likhupikemun.dpms.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class RestConfig {
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