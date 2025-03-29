package np.sthaniya.dpis.common.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

/**
 * Configuration class for Jackson JSON serialization/deserialization settings.
 *
 * This configuration provides customized [ObjectMapper] instances for handling JSON
 * data throughout the application, with specific settings for:
 * - Kotlin support
 * - Java Time API support
 * - Custom serialization features
 * - Custom deserialization features
 */
@Configuration
class JacksonConfig {
    /**
     * Creates and configures the primary [ObjectMapper] bean for the application.
     *
     * The configured mapper includes:
     * - Kotlin module support for Kotlin-specific features
     * - Java Time module for proper datetime handling
     * - Disabled timestamp-based date serialization
     * - Lenient deserialization for unknown properties
     * - Support for single value as array deserialization
     *
     * @return A configured [ObjectMapper] instance
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val mapper = jacksonObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(KotlinModule.Builder().build())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        return mapper
    }

    /**
     * Creates a [MappingJackson2HttpMessageConverter] bean using the configured [ObjectMapper].
     *
     * This converter is used by Spring MVC for HTTP message conversion between JSON and objects.
     *
     * @return A [MappingJackson2HttpMessageConverter] configured with the application's [ObjectMapper]
     */
    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter(objectMapper())
}
