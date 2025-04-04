package np.gov.mofaga.imis.shared.storage

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.minio")
data class StorageProperties(
    var endpoint: String = "",
    var accessKey: String = "",
    var secretKey: String = "",
    var bucket: String = "",
)