package np.sthaniya.dpis.location.repository.specification

import np.sthaniya.dpis.location.api.dto.criteria.ProvinceSearchCriteria
import np.sthaniya.dpis.location.domain.Province
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal

/**
 * Object providing specification builders for dynamic Province entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Search and filter specifications
 * - Reusable population and area specifications
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for complex search criteria
 * - Support for minimum population and area filtering
 */
object ProvinceSpecifications {
    
    /**
     * Creates a specification from search criteria by combining all applicable filters.
     *
     * @param criteria Search criteria containing filter parameters
     * @return Combined specification with all filters applied
     */
    fun withSearchCriteria(criteria: ProvinceSearchCriteria): Specification<Province> =
        Specification
            .where(withSearchTerm(criteria))
            .and(withCode(criteria))

    /**
     * Builds search term specification for pattern matching against name, nameNepali, and headquarter.
     *
     * @param criteria Search criteria containing search term
     * @return Specification for case-insensitive search across multiple fields
     */
    private fun withSearchTerm(criteria: ProvinceSearchCriteria): Specification<Province> =
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
     * @param criteria Search criteria containing province code
     * @return Specification for exact (case-insensitive) code matching
     */
    private fun withCode(criteria: ProvinceSearchCriteria): Specification<Province> =
        Specification { root, _, cb ->
            criteria.code?.let { code ->
                cb.equal(
                    cb.lower(root.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Creates a specification to filter provinces by minimum population.
     *
     * @param minPopulation Minimum population threshold
     * @return Specification filtering provinces with population >= minPopulation
     */
    fun hasMinimumPopulation(minPopulation: Long): Specification<Province> =
        Specification { root, _, cb ->
            cb.greaterThanOrEqualTo(root.get("population"), minPopulation)
        }

    /**
     * Creates a specification to filter provinces by minimum area.
     *
     * @param minArea Minimum area threshold
     * @return Specification filtering provinces with area >= minArea
     */
    fun hasMinimumArea(minArea: BigDecimal): Specification<Province> =
        Specification { root, _, cb ->
            cb.greaterThanOrEqualTo(root.get("area"), minArea)
        }
}
