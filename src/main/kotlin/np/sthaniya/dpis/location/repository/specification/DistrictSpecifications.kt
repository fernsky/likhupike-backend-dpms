package np.sthaniya.dpis.location.repository.specification

import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Order
import jakarta.persistence.criteria.Predicate
import np.sthaniya.dpis.location.api.dto.criteria.DistrictSearchCriteria
import np.sthaniya.dpis.location.domain.District
import org.springframework.data.jpa.domain.Specification

/**
 * Object providing specification builders for dynamic District entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Complex join handling
 * - Search and filter specifications
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for complex search criteria
 * - Province-based filtering
 * - Dynamic sorting
 */
object DistrictSpecifications {
    
    /**
     * Creates a specification from search criteria by combining all applicable filters.
     *
     * @param criteria Search criteria containing filter parameters
     * @return Combined specification with all filters applied
     */
    fun withSearchCriteria(criteria: DistrictSearchCriteria): Specification<District> =
        Specification
            .where(withSearchTerm(criteria))
            .and(withCode(criteria))
            .and(withProvinceCode(criteria))
            .and(withSorting(criteria))

    /**
     * Builds search term specification for pattern matching against name, nameNepali, and headquarter.
     *
     * @param criteria Search criteria containing search term
     * @return Specification for case-insensitive search across multiple fields
     */
    private fun withSearchTerm(criteria: DistrictSearchCriteria): Specification<District> =
        Specification { root, _, cb ->
            criteria.searchTerm?.let { term ->
                val searchTerm = "%${term.lowercase()}%"
                cb.or(
                    cb.like(cb.lower(root.get("name")), searchTerm),
                    cb.like(cb.lower(root.get("nameNepali")), searchTerm),
                    cb.like(cb.lower(root.get("headquarter")), searchTerm)
                )
            }
        }

    /**
     * Builds code exact match specification.
     *
     * @param criteria Search criteria containing district code
     * @return Specification for exact (case-insensitive) code matching
     */
    private fun withCode(criteria: DistrictSearchCriteria): Specification<District> =
        Specification { root, _, cb ->
            criteria.code?.let { code ->
                cb.equal(
                    cb.lower(root.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Builds province code specification with join to province entity.
     *
     * @param criteria Search criteria containing province code
     * @return Specification filtering districts by their province code
     */
    private fun withProvinceCode(criteria: DistrictSearchCriteria): Specification<District> =
        Specification { root, _, cb ->
            criteria.provinceCode?.let { provinceCode ->
                val province = root.join<District, Any>("province", JoinType.INNER)
                cb.equal(
                    cb.lower(province.get("code")),
                    provinceCode.lowercase()
                )
            }
        }

    /**
     * Applies sorting to the query based on criteria.
     *
     * @param criteria Search criteria containing sorting parameters
     * @return Specification applying appropriate sort order
     */
    private fun withSorting(criteria: DistrictSearchCriteria): Specification<District> =
        Specification { root, query, cb ->
            // Only apply sorting if it's not a count query and is a CriteriaQuery
            if (query is CriteriaQuery<*> && query.resultType != Long::class.java) {
                val sortField = criteria.sortBy.toEntityField()
                val order: Order =
                    if (criteria.sortDirection.isAscending) {
                        cb.asc(root.get<Any>(sortField))
                    } else {
                        cb.desc(root.get<Any>(sortField))
                    }
                query.orderBy(order)
            }
            null // Return null to avoid affecting the where clause
        }
}
