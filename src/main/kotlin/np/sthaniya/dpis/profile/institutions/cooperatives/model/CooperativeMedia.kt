package np.sthaniya.dpis.profile.institutions.cooperatives.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.time.Instant
import java.util.UUID

/**
 * Represents media assets for cooperatives with localization support.
 *
 * These assets include images, documents, videos, and other media that showcase
 * the cooperative's activities, facilities, members, products, and services.
 */
@Entity
@Table(
    name = "cooperative_media",
    indexes = [
        Index(name = "idx_coop_media_cooperative", columnList = "cooperative_id"),
        Index(name = "idx_coop_media_type", columnList = "type"),
        Index(name = "idx_coop_media_primary", columnList = "is_primary"),
        Index(name = "idx_coop_media_featured", columnList = "is_featured")
    ]
)
class CooperativeMedia : UuidBaseEntity() {

    /**
     * Reference to the Cooperative entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    var cooperative: Cooperative? = null

    /**
     * Reference to Locale (null if not language-specific).
     */
    @Column(name = "locale", length = 10)
    var locale: String? = null

    /**
     * Type of media.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: CooperativeMediaType = CooperativeMediaType.GALLERY_IMAGE

    /**
     * Title/caption of the media - should be descriptive and keyword-rich for SEO.
     */
    @Column(name = "title", nullable = false)
    var title: String? = null

    /**
     * Detailed description of the media content - opportunity for keyword integration.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    /**
     * Alternative text for accessibility and SEO - should describe image content concisely.
     */
    @Column(name = "alt_text", length = 255)
    var altText: String? = null

    /**
     * Path to the stored file in the system.
     */
    @Column(name = "file_path", nullable = false)
    var filePath: String? = null

    /**
     * Public URL to access the media.
     */
    @Column(name = "file_url")
    var fileUrl: String? = null

    /**
     * URL to thumbnail version (for images/videos).
     */
    @Column(name = "thumbnail_url")
    var thumbnailUrl: String? = null

    /**
     * MIME type of the media file (e.g., image/jpeg, application/pdf).
     */
    @Column(name = "mime_type", length = 100)
    var mimeType: String? = null

    /**
     * Size of the file in bytes.
     */
    @Column(name = "file_size")
    var fileSize: Long? = null

    /**
     * Dimensions for image/video files (width x height).
     */
    @Column(name = "dimensions", length = 100)
    var dimensions: String? = null

    /**
     * Duration for audio/video files (in seconds).
     */
    @Column(name = "duration")
    var duration: Int? = null

    /**
     * Additional metadata in JSON format (camera info, location, etc.).
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    var metadata: String? = null

    /**
     * Searchable tags associated with this media.
     */
    @Column(name = "tags", length = 500)
    var tags: String? = null

    /**
     * License information for the media.
     */
    @Column(name = "license", length = 255)
    var license: String? = null

    /**
     * Attribution information if required.
     */
    @Column(name = "attribution", length = 255)
    var attribution: String? = null

    /**
     * Display order for multiple media items.
     */
    @Column(name = "sort_order")
    var sortOrder: Int = 0

    /**
     * Whether this is the primary media item.
     */
    @Column(name = "is_primary")
    var isPrimary: Boolean = false

    /**
     * Whether this media should be featured prominently.
     */
    @Column(name = "is_featured")
    var isFeatured: Boolean = false

    /**
     * Public, private, or restricted visibility.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_status", nullable = false)
    var visibilityStatus: MediaVisibilityStatus = MediaVisibilityStatus.PUBLIC

    /**
     * User who uploaded this media.
     */
    @Column(name = "uploaded_by")
    var uploadedBy: UUID? = null

    /**
     * When this media was uploaded.
     */
    @Column(name = "uploaded_at")
    var uploadedAt: Instant = Instant.now()

    /**
     * When this media was last accessed/viewed.
     */
    @Column(name = "last_accessed")
    var lastAccessed: Instant? = null

    /**
     * Number of times this media has been viewed.
     */
    @Column(name = "view_count")
    var viewCount: Long = 0

    /**
     * Increments the view count for this media item.
     */
    fun incrementViewCount() {
        this.viewCount++
        this.lastAccessed = Instant.now()
    }
    
    /**
     * Updates file information.
     */
    fun updateFileInfo(filePath: String, fileUrl: String?, thumbnailUrl: String?, mimeType: String?, fileSize: Long?) {
        this.filePath = filePath
        this.fileUrl = fileUrl
        this.thumbnailUrl = thumbnailUrl
        this.mimeType = mimeType
        this.fileSize = fileSize
        this.uploadedAt = Instant.now()
    }
}
