package np.sthaniya.dpis.profile.institutions.cooperatives.dto.search

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.AssertTrue
import java.time.LocalDate
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * Search criteria for cooperative queries with pagination, filtering, and sorting support. Used to
 * filter and paginate cooperative data.
 *
 * Search Parameters:
 * - Exact match: code, type, status, ward
 * - Pattern match: searchTerm (matches across multiple text fields)
 * - Date range: establishedAfter, establishedBefore
 * - Geographic: nearPoint (longitude/latitude with radius)
 * - Translation-based: locale, nameContains
 *
 * Pagination/Sorting:
 * - Page number (1-based)
 * - Page size
 * - Sort field
 * - Sort direction
 *
 * Column Selection: Supports field selection via [columns]. Valid columns defined in
 * [ALLOWED_COLUMNS].
 */
@Schema(
        description = "Search criteria for filtering and paginating cooperatives",
        title = "Cooperative Search Criteria"
)
data class CooperativeSearchCriteria(
        @Schema(
                description = "Filter by exact cooperative code",
                example = "upali-agriculture-coop",
                nullable = true
        )
        val code: String? = null,
        @Schema(
                description =
                        "Generic search term that matches against name, description, and other text fields",
                example = "agriculture",
                nullable = true
        )
        val searchTerm: String? = null,
        @Schema(
                description = "Filter by cooperative type",
                example = "AGRICULTURE",
                nullable = true
        )
        val type: CooperativeType? = null,
        @Schema(
                description = "Filter by multiple cooperative types",
                example = "[\"AGRICULTURE\", \"DAIRY\"]",
                nullable = true
        )
        val types: Set<CooperativeType>? = null,
        @Schema(description = "Filter by cooperative status", example = "ACTIVE", nullable = true)
        val status: CooperativeStatus? = null,
        @Schema(
                description = "Filter by specific ward number",
                example = "5",
                minimum = "1",
                nullable = true
        )
        val ward: Int? = null,
        @Schema(
                description = "Filter by multiple ward numbers",
                example = "[1, 2, 3]",
                nullable = true
        )
        val wards: Set<Int>? = null,
        @Schema(
                description = "Filter cooperatives established after this date (inclusive)",
                example = "2015-01-01",
                nullable = true
        )
        val establishedAfter: LocalDate? = null,
        @Schema(
                description = "Filter cooperatives established before this date (inclusive)",
                example = "2022-12-31",
                nullable = true
        )
        val establishedBefore: LocalDate? = null,
        @Schema(
                description = "Filter by locale for translation content",
                example = "ne",
                nullable = true
        )
        val locale: String? = null,
        @Schema(
                description =
                        "Filter by text contained in cooperative name (in the specified locale)",
                example = "किसान",
                nullable = true
        )
        val nameContains: String? = null,
        @Schema(
                description = "Filter by geographic proximity (longitude)",
                example = "85.3240",
                nullable = true
        )
        val longitude: Double? = null,
        @Schema(
                description = "Filter by geographic proximity (latitude)",
                example = "27.7172",
                nullable = true
        )
        val latitude: Double? = null,
        @Schema(
                description = "Search radius in meters for geographic proximity search",
                example = "5000",
                minimum = "1",
                nullable = true
        )
        val radiusInMeters: Double? = null,
        @Schema(
                description = "Include cooperatives with media of specified type",
                example = "LOGO",
                nullable = true
        )
        val hasMediaOfType: String? = null,
        @Schema(
                description = "Specific columns to include in the response",
                example = "[\"code\", \"type\", \"ward\", \"translations\"]",
                nullable = true
        )
        val columns: Set<String>? = null,
        @Schema(
                description = "Include translations in response",
                example = "true",
                defaultValue = "false"
        )
        val includeTranslations: Boolean = false,
        @Schema(
                description = "Include primary media items in response",
                example = "true",
                defaultValue = "false"
        )
        val includePrimaryMedia: Boolean = false,
        @Schema(
                description = "Page number (1-based)",
                example = "1",
                minimum = "1",
                defaultValue = "1"
        )
        val page: Int = 1,
        @Schema(description = "Number of items per page", example = "10", defaultValue = "10")
        val size: Int = 10,
        @Schema(description = "Field to sort by", example = "createdAt", defaultValue = "createdAt")
        val sortBy: String = "createdAt",
        @Schema(
                description = "Sort direction",
                example = "DESC",
                allowableValues = ["ASC", "DESC"],
                defaultValue = "DESC"
        )
        val sortDirection: Sort.Direction = Sort.Direction.DESC,
) {
    /**
     * Converts the criteria to Spring Data's Pageable object. Handles 1-based to 0-based page
     * number conversion.
     */
    fun toPageable(): Pageable =
            PageRequest.of(
                    (page - 1).coerceAtLeast(0), // Convert 1-based to 0-based
                    size,
                    Sort.by(sortDirection, sortBy),
            )

    companion object {
        /**
         * Set of field names that can be included in query results. Used to validate and filter
         * requested columns.
         */
        val ALLOWED_COLUMNS =
                setOf(
                        "id",
                        "code",
                        "defaultLocale",
                        "establishedDate",
                        "ward",
                        "type",
                        "status",
                        "registrationNumber",
                        "point",
                        "contactEmail",
                        "contactPhone",
                        "websiteUrl",
                        "createdAt",
                        "updatedAt",
                        "translations",
                        "primaryMedia"
                )
    }

    /**
     * Returns validated set of columns to include in query results. Falls back to all allowed
     * columns if none specified.
     */
    fun getValidColumns(): Set<String> =
            columns?.filter { it in ALLOWED_COLUMNS }?.toSet() ?: ALLOWED_COLUMNS

    /** Validates that page number is positive. Required by Jakarta Validation. */
    @AssertTrue(message = "Page number must be greater than 0")
    fun isPageNumberValid(): Boolean = page > 0

    /** Validates that geographic search parameters are all provided if any are provided. */
    @AssertTrue(
            message =
                    "For geographic search, longitude, latitude, and radiusInMeters must all be provided"
    )
    fun isGeoSearchValid(): Boolean {
        // If any geo parameters are provided, all must be provided
        val geoParamsPresent = listOf(longitude, latitude, radiusInMeters)
        return geoParamsPresent.all { it != null } || geoParamsPresent.all { it == null }
    }
}
