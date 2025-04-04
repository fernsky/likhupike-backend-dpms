package np.sthaniya.dpis.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for MinIO S3-compatible storage client.
 * 
 * Sets up the MinIO client bean and ensures the required bucket exists.
 */
@Configuration
class MinioConfig {
    
    @Value("\${app.storage.endpoint}")
    private lateinit var endpoint: String
    
    @Value("\${app.storage.access-key}")
    private lateinit var accessKey: String
    
    @Value("\${app.storage.secret-key}")
    private lateinit var secretKey: String
    
    @Value("\${app.storage.bucket:dpis-documents}")
    private lateinit var bucketName: String
    
    /**
     * Creates and configures the MinIO client bean.
     * 
     * @return Configured MinIO client instance
     */
    @Bean
    fun minioClient(): MinioClient {
        val client = MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()
            
        // Create bucket if it doesn't exist
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }
        
        return client
    }
}
