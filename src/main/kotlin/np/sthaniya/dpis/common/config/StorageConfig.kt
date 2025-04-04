package np.gov.mofaga.imis.shared.storage

import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(StorageProperties::class)
class StorageConfig(
    private val storageProperties: StorageProperties,
) {
    @Bean
    fun minioClient(): MinioClient =
        MinioClient
            .builder()
            .endpoint(storageProperties.endpoint, 443, true)
            .credentials(storageProperties.accessKey, storageProperties.secretKey)
            .build()
}
