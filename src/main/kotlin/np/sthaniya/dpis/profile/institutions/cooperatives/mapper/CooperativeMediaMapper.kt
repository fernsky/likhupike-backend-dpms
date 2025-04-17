package np.sthaniya.dpis.profile.institutions.cooperatives.mapper

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMedia
import org.springframework.stereotype.Component

/**
 * Mapper for converting between CooperativeMedia entities and DTOs.
 */
@Component
class CooperativeMediaMapper {

    /**
     * Convert a CooperativeMedia entity to a CooperativeMediaResponse DTO.
     *
     * @param media The CooperativeMedia entity to convert
     * @return The CooperativeMediaResponse DTO
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
            fileUrl = media.fileUrl,
            thumbnailUrl = media.thumbnailUrl,
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
}
