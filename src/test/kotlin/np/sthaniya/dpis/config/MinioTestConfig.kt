package np.sthaniya.dpis.config

import io.minio.MinioClient
import np.sthaniya.dpis.common.config.StorageProperties
import org.mockito.kotlin.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * Test configuration for MinIO client and related components.
 * 
 * Provides mock beans for MinioClient and StorageProperties for tests.
 */
@TestConfiguration
class MinioTestConfig {
    
    /**
     * Creates and provides a mock MinioClient bean for tests.
     * 
     * @return A mock MinioClient instance
     */
    @Bean
    @Primary
    fun minioClient(): MinioClient {
        return mock()
    }
    
    /**
     * Creates and provides a test StorageProperties bean.
     * 
     * @return A StorageProperties instance configured with test values
     */
    @Bean
    @Primary
    fun storageProperties(): StorageProperties {
        return StorageProperties(
            endpoint = "http://test-minio:9000",
            accessKey = "test-access-key",
            secretKey = "test-secret-key",
            bucket = "test-bucket"
        )
    }
}
