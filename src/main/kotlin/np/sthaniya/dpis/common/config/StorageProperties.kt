package np.sthaniya.dpis.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for document storage system.
 *
 * This class maps application properties with the prefix "app.storage" to a type-safe configuration
 * class.
 *
 * Example configuration in application.yml:
 * ```yaml
 * app:
 *   storage:
 *     endpoint: http://minio:9000
 *     public-endpoint: https://minio-public.example.com
 *     access-key: minioadmin
 *     secret-key: minioadmin
 *     bucket: dpis-documents
 * ```
 *
 * @property endpoint The endpoint URL for the MinIO/S3 service (internal/backend access)
 * @property publicEndpoint The public-facing endpoint URL for the MinIO/S3 service (for frontend
 * access)
 * @property accessKey The access key for authentication
 * @property secretKey The secret key for authentication
 * @property bucket The default bucket name to use for document storage
 */
@ConfigurationProperties(prefix = "app.minio")
data class StorageProperties(
        var endpoint: String = "",
        var publicEndpoint: String = "",
        var accessKey: String = "",
        var secretKey: String = "",
        var bucket: String = "dpis-documents"
)
