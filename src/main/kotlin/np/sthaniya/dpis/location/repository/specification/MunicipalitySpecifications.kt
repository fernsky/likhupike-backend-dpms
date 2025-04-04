package np.sthaniya.dpis.location.repository.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import np.sthaniya.dpis.location.api.dto.criteria.MunicipalitySearchCriteria
import np.sthaniya.dpis.location.api.dto.enums.MunicipalitySortField
import np.sthaniya.dpis.location.domain.Municipality
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal

/**
 * Object providing specification builders for dynamic Municipality entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Complex join handling
 * - Search and filter specifications
 * - Geo-spatial distance calculations
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for complex search criteria
 * - District and province-based filtering
 * - Location-based search with distance calculations
 * - Dynamic sorting including distance-based sorting
 */
object MunicipalitySpecifications {
    
    /**
     * Creates a specification from search criteria by combining all applicable filters.
     *
     * @param criteria Search criteria containing filter parameters
     * @return Combined specification with all filters applied
     */
    fun withSearchCriteria(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification
            .where(withSearchTerm(criteria))
            .and(withCode(criteria))
            .and(withDistrictCode(criteria))
            .and(withProvinceCode(criteria))
            .and(withTypes(criteria))
            .and(withWardCountRange(criteria))
            .and(withPopulationRange(criteria))
            .and(withAreaRange(criteria))
            .and(withGeoRadius(criteria))
            .and(withSorting(criteria))

    /**
     * Builds search term specification for pattern matching against name, nameNepali, and code.
     *
     * @param criteria Search criteria containing search term
     * @return Specification for case-insensitive search across multiple fields
     */
    private fun withSearchTerm(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            criteria.searchTerm?.let { term ->
                val searchTerm = "%${term.lowercase()}%"
                cb.or(
                    cb.like(cb.lower(root.get("name")), searchTerm),
                    cb.like(cb.lower(root.get("nameNepali")), searchTerm),
                    cb.like(cb.lower(root.get("code")), searchTerm)
                )
            }
        }

    /**
     * Builds code exact match specification.
     *
     * @param criteria Search criteria containing municipality code
     * @return Specification for exact (case-insensitive) code matching
     */
    private fun withCode(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            criteria.code?.let { code ->
                cb.equal(
                    cb.lower(root.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Builds district code specification with join to district entity.
     *
     * @param criteria Search criteria containing district code
     * @return Specification filtering municipalities by their district code
     */
    private fun withDistrictCode(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            criteria.districtCode?.let { districtCode ->
                val district = root.join<Municipality, Any>("district", JoinType.INNER)
                cb.equal(
                    cb.lower(district.get("code")),
                    districtCode.lowercase()
                )
            }
        }

    /**
     * Builds province code specification with joins to district and province entities.
     *
     * @param criteria Search criteria containing province code
     * @return Specification filtering municipalities by their province code
     */
    private fun withProvinceCode(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            criteria.provinceCode?.let { provinceCode ->
                val district = root.join<Municipality, Any>("district", JoinType.INNER)
                val province = district.join<Any, Any>("province", JoinType.INNER)
                cb.equal(
                    cb.lower(province.get("code")),
                    provinceCode.lowercase()
                )
            }
        }

    /**
     * Builds municipality type filter specification.
     *
     * @param criteria Search criteria containing municipality types
     * @return Specification filtering municipalities by their type
     */
    private fun withTypes(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            criteria.types?.let { types ->
                if (types.isNotEmpty()) {
                    root.get<Any>("type").`in`(types)
                } else {
                    null
                }
            }
        }

    /**
     * Builds ward count range specification.
     *
     * @param criteria Search criteria containing min/max ward counts
     * @return Specification filtering municipalities by their ward count range
     */
    private fun withWardCountRange(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.minWards?.let { min ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalWards"), min))
            }

            criteria.maxWards?.let { max ->
                predicates.add(cb.lessThanOrEqualTo(root.get("totalWards"), max))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Builds population range specification.
     *
     * @param criteria Search criteria containing min/max population
     * @return Specification filtering municipalities by their population range
     */
    private fun withPopulationRange(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.minPopulation?.let { min ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("population"), min))
            }

            criteria.maxPopulation?.let { max ->
                predicates.add(cb.lessThanOrEqualTo(root.get("population"), max))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Builds area range specification.
     *
     * @param criteria Search criteria containing min/max area
     * @return Specification filtering municipalities by their area range
     */
    private fun withAreaRange(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.minArea?.let { min ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"), min))
            }

            criteria.maxArea?.let { max ->
                predicates.add(cb.lessThanOrEqualTo(root.get("area"), max))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Builds geo-spatial radius search specification.
     *
     * @param criteria Search criteria containing coordinates and radius
     * @return Specification filtering municipalities by distance from a point
     */
    private fun withGeoRadius(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, _, cb ->
            if (criteria.latitude != null && criteria.longitude != null && criteria.radiusKm != null) {
                val distanceInMeters = criteria.radiusKm * 1000 // Convert km to meters
                createDistancePredicate(
                    cb,
                    root,
                    criteria.latitude,
                    criteria.longitude,
                    distanceInMeters
                )
            } else {
                null
            }
        }

    /**
     * Applies sorting to the query based on criteria.
     *
     * @param criteria Search criteria containing sorting parameters
     * @return Specification applying appropriate sort order
     */
    private fun withSorting(criteria: MunicipalitySearchCriteria): Specification<Municipality> =
        Specification { root, query, cb ->
            // Only apply sorting if it's not a count query and is a CriteriaQuery
            if (query is CriteriaQuery<*> && query.resultType != Long::class.java) {
                when (criteria.sortBy) {
                    MunicipalitySortField.DISTANCE -> {
                        // Handle distance-based sorting with null check
                        if (criteria.latitude != null && criteria.longitude != null) {
                            val distanceExpression = createDistanceExpression(
                                cb,
                                root,
                                criteria.latitude,
                                criteria.longitude
                            )
                            val order = if (criteria.sortDirection.isAscending) {
                                cb.asc(distanceExpression)
                            } else {
                                cb.desc(distanceExpression)
                            }
                            query.orderBy(order)
                        } else {
                            // Fallback sort if coordinates are missing
                            val order = if (criteria.sortDirection.isAscending) {
                                cb.asc(root.get<Any>("id"))
                            } else {
                                cb.desc(root.get<Any>("id"))
                            }
                            query.orderBy(order)
                        }
                    }
                    else -> {
                        val sortField = criteria.sortBy.toEntityField()
                        val order = if (criteria.sortDirection.isAscending) {
                            cb.asc(root.get<Any>(sortField))
                        } else {
                            cb.desc(root.get<Any>(sortField))
                        }
                        query.orderBy(order)
                    }
                }
            }
            null // Return null to avoid affecting the where clause
        }

    /**
     * Creates a predicate for geo-spatial distance filtering.
     *
     * @param cb CriteriaBuilder instance
     * @param root Root entity for the query
     * @param latitude Target latitude
     * @param longitude Target longitude
     * @param radiusInMeters Maximum distance in meters
     * @return Predicate for distance-based filtering
     */
    private fun createDistancePredicate(
        cb: CriteriaBuilder,
        root: Root<Municipality>,
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusInMeters: Double
    ): jakarta.persistence.criteria.Predicate {
        val distanceFunction = cb.function(
            "ST_Distance_Sphere",
            Double::class.java,
            cb.function(
                "point",
                Any::class.java,
                root.get<BigDecimal>("longitude"),
                root.get<BigDecimal>("latitude")
            ),
            cb.function(
                "point",
                Any::class.java,
                cb.literal(longitude),
                cb.literal(latitude)
            )
        )
        return cb.lessThanOrEqualTo(distanceFunction, radiusInMeters)
    }

    /**
     * Creates an expression for calculating distance between points.
     *
     * @param cb CriteriaBuilder instance
     * @param root Root entity for the query
     * @param latitude Target latitude
     * @param longitude Target longitude
     * @return Expression representing the calculated distance
     */
    private fun createDistanceExpression(
        cb: CriteriaBuilder,
        root: Root<Municipality>,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Expression<Double> = cb.function(
        "ST_Distance_Sphere",
        Double::class.java,
        cb.function(
            "point",
            Any::class.java,
            root.get<BigDecimal>("longitude"),
            root.get<BigDecimal>("latitude")
        ),
        cb.function(
            "point",
            Any::class.java,
            cb.literal(longitude),
            cb.literal(latitude)
        )
    )
}
