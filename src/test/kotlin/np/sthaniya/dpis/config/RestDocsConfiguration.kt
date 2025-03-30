package np.sthaniya.dpis.config

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.operation.preprocess.Preprocessors

@TestConfiguration
class RestDocsConfiguration {

    @Bean
    fun restDocsMockMvcConfigurationCustomizer() = RestDocsMockMvcConfigurationCustomizer {
        it.operationPreprocessors()
            .withRequestDefaults(
                Preprocessors.prettyPrint(),
                Preprocessors.removeHeaders("Content-Length", "X-CSRF-TOKEN")
            )
            .withResponseDefaults(
                Preprocessors.prettyPrint(),
                Preprocessors.removeHeaders(
                    "X-Content-Type-Options", "X-XSS-Protection", "Cache-Control",
                    "Pragma", "Expires", "Strict-Transport-Security", "X-Frame-Options",
                    "Content-Length"
                )
            )
    }
}
