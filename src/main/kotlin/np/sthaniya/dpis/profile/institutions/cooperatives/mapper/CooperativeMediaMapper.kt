package np.sthaniya.dpis.profile.institutions.cooperatives.mapper

import np.sthaniya.dpis.common.config.StorageProperties
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMedia
import org.springframework.stereotype.Component

/** Mapper for converting between CooperativeMedia entities and DTOs. */
@Component
class CooperativeMediaMapper(private val storageProperties: StorageProperties) {

    /**
     * Convert a CooperativeMedia entity to a CooperativeMediaResponse DTO. Replaces internal Minio
     * endpoint URLs with public-facing URLs.
     *
     * @param media The CooperativeMedia entity to convert
     * @return The CooperativeMediaResponse DTO with public-facing URLs
     */
    fun toResponse(media: CooperativeMedia): CooperativeMediaResponse {
        return CooperativeMediaResponse(
                id = media.id ?: throw IllegalStateException("Media ID cannot be null"),
                locale = media.locale,
                type = media.type,
                title = media.title ?: "",
                description = media.description,
                altText = media.altText,
                filePath = media.filePath ?: "",
                fileUrl = convertToPublicUrl(media.fileUrl),
                thumbnailUrl = convertToPublicUrl(media.thumbnailUrl),
                mimeType = media.mimeType,
                fileSize = media.fileSize,
                dimensions = media.dimensions,
                duration = media.duration,
                metadata = media.metadata,
                tags = media.tags,
                license = media.license,
                attribution = media.attribution,
                sortOrder = media.sortOrder,
                isPrimary = media.isPrimary,
                isFeatured = media.isFeatured,
                visibilityStatus = media.visibilityStatus,
                uploadedAt = media.uploadedAt,
                lastAccessed = media.lastAccessed,
                viewCount = media.viewCount
        )
    }

    /**
     * Converts an internal Minio URL to a public-facing URL.
     *
     * @param url The original URL that may contain the internal endpoint
     * @return The URL with the internal endpoint replaced by the public endpoint
     */
    private fun convertToPublicUrl(url: String?): String? {
        if (url.isNullOrEmpty()) return url

        // If the storage properties are invalid, return the original URL
        if (storageProperties.endpoint.isEmpty() || storageProperties.publicEndpoint.isEmpty()) {
            return url
        }

        // Replace the internal endpoint with the public endpoint
        return url.replace(storageProperties.endpoint, storageProperties.publicEndpoint)
    }
}
