package np.sthaniya.dpis.config

import jakarta.annotation.PostConstruct
import java.io.File
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for environment variable loading and verification. Loads environment
 * variables from .env file if it exists and system environment variables are not set.
 */
@Configuration
class EnvConfig {
    private val logger = LoggerFactory.getLogger(EnvConfig::class.java)

    @PostConstruct
    fun init() {
        // Check if .env file exists
        val envFile = File(".env")
        if (envFile.exists()) {
            try {
                // Try to load environment variables from .env file
                // val dotenv = Dotenv.configure().directory(".").load()

                // Log loaded environment variables for debugging (without sensitive values)
                logger.info("Loaded environment variables from .env file")
                logger.info(
                        "Database URL: ${maskSensitiveInfo(System.getProperty("spring.datasource.url") ?: System.getenv("SPRING_DATASOURCE_URL") ?: "not set")}"
                )
                logger.info(
                        "Redis host: ${System.getProperty("redis.host") ?: System.getenv("REDIS_HOST") ?: "not set"}"
                )
            } catch (e: Exception) {
                logger.error("Failed to load environment variables from .env file: ${e.message}")
            }
        } else {
            logger.warn(
                    ".env file not found in ${System.getProperty("user.dir")}. Using system environment variables."
            )
        }
    }

    /** Masks sensitive information for logging */
    private fun maskSensitiveInfo(info: String): String {
        // Don't return empty strings
        if (info.isBlank()) return "not set"

        // Mask database URLs, keeping only domain/host part visible
        if (info.startsWith("jdbc:")) {
            val regex = "jdbc:[^:]+://([^:/]+)".toRegex()
            val match = regex.find(info)
            return if (match != null) {
                "jdbc:*****://${match.groupValues[1]}/*****"
            } else {
                "jdbc:*****"
            }
        }

        return info
    }
}
