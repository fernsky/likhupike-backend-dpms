package np.sthaniya.dpis.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import np.sthaniya.dpis.common.config.StorageProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Configuration for MinIO S3-compatible storage client.
 * 
 * Sets up the MinIO client bean and ensures the required bucket exists.
 * This configuration is only active in non-test environments.
 *
 * @property storageProperties Configuration properties for storage
 */
@Configuration
@EnableConfigurationProperties(StorageProperties::class)
@Profile("!test") // Exclude from test profile
class MinioConfig(
    private val storageProperties: StorageProperties
) {
    
    /**
     * Creates and configures the MinIO client bean.
     * 
     * @return Configured MinIO client instance
     */
    @Bean
    fun minioClient(): MinioClient {
        val client = MinioClient.builder()
            .endpoint(storageProperties.endpoint)
            .credentials(storageProperties.accessKey, storageProperties.secretKey)
            .build()
            
        // Create bucket if it doesn't exist
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(storageProperties.bucket).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(storageProperties.bucket).build())
        }
        
        return client
    }
}
