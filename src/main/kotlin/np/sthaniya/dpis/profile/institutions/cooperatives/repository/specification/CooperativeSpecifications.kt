
package np.sthaniya.dpis.profile.institutions.cooperatives.repository.specification

import jakarta.persistence.criteria.JoinType
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMedia
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Object providing specification builders for dynamic Cooperative entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Complex join handling
 * - Search and filter specifications
 */
object CooperativeSpecifications {
    
    /**
     * Creates a combined specification from search criteria.
     * 
     * @param criteria Search criteria for cooperatives
     * @return Combined specification reflecting all criteria filters
     */
    fun fromCriteria(criteria: CooperativeSearchCriteria): Specification<Cooperative> =
        Specification
            .where(withCode(criteria))
            .and(withSearchTerm(criteria))
            .and(withType(criteria))
            .and(withTypes(criteria))
            .and(withStatus(criteria))
            .and(withWard(criteria))
            .and(withWards(criteria))
            .and(withEstablishedDateRange(criteria))
            .and(withTranslationText(criteria))
            .and(withHasMediaOfType(criteria))
            .and(withGeoLocation(criteria))

    /**
     * Specification for filtering by exact code match.
     */
    private fun withCode(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.code?.let { code ->
                cb.equal(root.get<String>("code"), code)
            }
        }

    /**
     * Specification for searching across multiple text fields.
     */
    private fun withSearchTerm(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, query, cb ->
            criteria.searchTerm?.let { term ->
                if (term.isBlank()) return@let null
                
                query.distinct(true)
                val pattern = "%${term.lowercase()}%"
                
                // Join with translations to search in name and description
                val translationJoin = root.join<Cooperative, CooperativeTranslation>("translations", JoinType.LEFT)
                
                cb.or(
                    cb.like(cb.lower(root.get("code")), pattern),
                    cb.like(cb.lower(translationJoin.get("name")), pattern),
                    cb.like(cb.lower(translationJoin.get("description")), pattern),
                    cb.like(cb.lower(root.get("contactEmail")), pattern),
                    cb.like(cb.lower(root.get("contactPhone")), pattern),
                    cb.like(cb.lower(root.get("websiteUrl")), pattern)
                )
            }
        }

    /**
     * Specification for filtering by cooperative type.
     */
    private fun withType(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.type?.let { type ->
                cb.equal(root.get<Any>("type"), type)
            }
        }

    /**
     * Specification for filtering by multiple cooperative types.
     */
    private fun withTypes(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.types?.let { types ->
                if (types.isEmpty()) return@let null
                root.get<Any>("type").`in`(types)
            }
        }

    /**
     * Specification for filtering by cooperative status.
     */
    private fun withStatus(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.status?.let { status ->
                cb.equal(root.get<Any>("status"), status)
            }
        }

    /**
     * Specification for filtering by ward number.
     */
    private fun withWard(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.ward?.let { ward ->
                cb.equal(root.get<Int>("ward"), ward)
            }
        }

    /**
     * Specification for filtering by multiple ward numbers.
     */
    private fun withWards(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            criteria.wards?.let { wards ->
                if (wards.isEmpty()) return@let null
                root.get<Int>("ward").`in`(wards)
            }
        }

    /**
     * Specification for filtering by established date range.
     */
    private fun withEstablishedDateRange(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.establishedAfter?.let { date ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("establishedDate"), date))
            }
            
            criteria.establishedBefore?.let { date ->
                predicates.add(cb.lessThanOrEqualTo(root.get("establishedDate"), date))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification for filtering by translation content.
     */
    private fun withTranslationText(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, query, cb ->
            // Skip if no locale or name criteria provided
            if (criteria.locale == null && criteria.nameContains == null) {
                return@Specification null
            }
            
            query.distinct(true)
            val translationJoin = root.join<Cooperative, CooperativeTranslation>("translations", JoinType.LEFT)
            
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()
            
            // Filter by locale if provided
            criteria.locale?.let { locale ->
                predicates.add(cb.equal(translationJoin.get<String>("locale"), locale))
            }
            
            // Filter by name contains if provided
            criteria.nameContains?.let { namePattern ->
                if (namePattern.isNotBlank()) {
                    val pattern = "%${namePattern.lowercase()}%"
                    predicates.add(cb.like(cb.lower(translationJoin.get("name")), pattern))
                }
            }
            
            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification for filtering by media type.
     */
    private fun withHasMediaOfType(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, query, cb ->
            criteria.hasMediaOfType?.let { mediaTypeStr ->
                try {
                    // Try to parse as CooperativeMediaType
                    val mediaType = CooperativeMediaType.valueOf(mediaTypeStr.uppercase())
                    
                    query.distinct(true)
                    val mediaJoin = root.join<Cooperative, CooperativeMedia>("media", JoinType.LEFT)
                    cb.equal(mediaJoin.get<CooperativeMediaType>("type"), mediaType)
                } catch (e: IllegalArgumentException) {
                    // Invalid media type, return no results
                    cb.or() // Empty OR predicate - always false
                }
            }
        }

    /**
     * Specification for geographic proximity search.
     */
    private fun withGeoLocation(criteria: CooperativeSearchCriteria) =
        Specification<Cooperative> { root, query, cb ->
            // Check if all necessary geo parameters are provided
            if (criteria.longitude != null && criteria.latitude != null && criteria.radiusInMeters != null) {
                // Use a native SQL query to leverage PostGIS functions
                // This is not directly possible with JPA Criteria API, so we'll need to use a custom implementation
                // in the repository layer
                
                // This is a placeholder - implementation will be in a custom repository method
                null
            } else {
                null
            }
        }
}
