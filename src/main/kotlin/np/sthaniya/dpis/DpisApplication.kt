package np.sthaniya.dpis

import np.sthaniya.dpis.auth.config.AdminConfig
import np.sthaniya.dpis.common.config.JacksonConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ComponentScan

/**
 * Main application class for the Digital Profile Information System (DPIS).
 *
 * This class serves as the entry point for the Spring Boot application and provides:
 * - Configuration properties support via [EnableConfigurationProperties]
 * - JSON configuration via [JacksonConfig]
 * - Component scanning for the application package
 */
@SpringBootApplication
@EnableConfigurationProperties(AdminConfig::class)
@Import(JacksonConfig::class)
@ComponentScan(basePackages = ["np.sthaniya.dpis"])
class dpisApplication

/**
 * Application entry point that starts the Spring Boot application.
 *
 * @param args Command line arguments passed to the application
 */
fun main(args: Array<String>) {
    runApplication<dpisApplication>(*args)
}



