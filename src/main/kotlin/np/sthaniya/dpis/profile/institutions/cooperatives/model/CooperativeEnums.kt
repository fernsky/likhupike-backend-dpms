package np.sthaniya.dpis.profile.institutions.cooperatives.model

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Status values for a cooperative.
 */
@Schema(description = "Status of a cooperative")
enum class CooperativeStatus {
    /** Cooperative is active and operational */
    @Schema(description = "Cooperative is active and operational") ACTIVE,
    
    /** Cooperative is temporarily inactive */
    @Schema(description = "Cooperative is temporarily inactive") INACTIVE,
    
    /** Cooperative is pending approval */
    @Schema(description = "Cooperative is pending approval") PENDING_APPROVAL,
    
    /** Cooperative has been dissolved */
    @Schema(description = "Cooperative has been dissolved") DISSOLVED,
    
    /** Cooperative is under legal review */
    @Schema(description = "Cooperative is under legal review") UNDER_REVIEW,
    
    /** Cooperative is merged with another cooperative */
    @Schema(description = "Cooperative is merged with another cooperative") MERGED
}

/**
 * Status values for content publication.
 */
@Schema(description = "Status of content")
enum class ContentStatus {
    /** Content is in draft state */
    @Schema(description = "Content is in draft state") DRAFT,
    
    /** Content is published and visible */
    @Schema(description = "Content is published and visible") PUBLISHED,
    
    /** Content is archived and not visible */
    @Schema(description = "Content is archived and not visible") ARCHIVED,
    
    /** Content is pending review before publication */
    @Schema(description = "Content is pending review before publication") PENDING_REVIEW
}

/**
 * Types of media that can be associated with cooperatives.
 */
@Schema(description = "Types of cooperative media")
enum class CooperativeMediaType {
    /** Logo of the cooperative */
    @Schema(description = "Logo of the cooperative") LOGO,
    
    /** Main hero image for the cooperative */
    @Schema(description = "Main hero image for the cooperative") HERO_IMAGE,
    
    /** General image for the gallery */
    @Schema(description = "General image for the gallery") GALLERY_IMAGE,
    
    /** Photo of a product */
    @Schema(description = "Photo of a product") PRODUCT_PHOTO,
    
    /** Photo of team members */
    @Schema(description = "Photo of team members") TEAM_PHOTO,
    
    /** Official document */
    @Schema(description = "Official document") DOCUMENT,
    
    /** Video content */
    @Schema(description = "Video content") VIDEO,
    
    /** Brochure or marketing material */
    @Schema(description = "Brochure or marketing material") BROCHURE,
    
    /** Annual report document */
    @Schema(description = "Annual report document") ANNUAL_REPORT,
    
    /** Certificate or award */
    @Schema(description = "Certificate or award") CERTIFICATE
}

/**
 * Visibility status for media items.
 */
@Schema(description = "Visibility status for media items")
enum class MediaVisibilityStatus {
    /** Publicly visible to all users */
    @Schema(description = "Publicly visible to all users") PUBLIC,
    
    /** Visible only to authenticated users */
    @Schema(description = "Visible only to authenticated users") AUTHENTICATED,
    
    /** Visible only to specific roles */
    @Schema(description = "Visible only to specific roles") RESTRICTED,
    
    /** Not visible to any users */
    @Schema(description = "Not visible to any users") PRIVATE
}

/**
 * Represents the time taken to reach a location or service.
 */
@Schema(description = "Time taken to reach a location")
enum class TimeType {
    /** Less than 30 minutes */
    @Schema(description = "Less than 30 minutes") LESS_THAN_30_MIN,
    
    /** Between 30 minutes and 1 hour */
    @Schema(description = "Between 30 minutes and 1 hour") BETWEEN_30_MIN_1_HOUR,
    
    /** Between 1 hour and 2 hours */
    @Schema(description = "Between 1 hour and 2 hours") BETWEEN_1_2_HOURS,
    
    /** More than 2 hours */
    @Schema(description = "More than 2 hours") MORE_THAN_2_HOURS
}
