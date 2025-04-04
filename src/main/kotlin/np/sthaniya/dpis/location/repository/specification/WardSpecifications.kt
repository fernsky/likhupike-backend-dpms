package np.sthaniya.dpis.location.repository.specification

import jakarta.persistence.criteria.JoinType
import np.sthaniya.dpis.location.api.dto.criteria.WardSearchCriteria
import np.sthaniya.dpis.location.domain.Ward
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import kotlin.math.*

/**
 * Object providing specification builders for dynamic Ward entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Complex join handling
 * - Geographic bounding box calculations
 * - Search and filter specifications
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for complex search criteria
 * - Municipality, district, and province-based filtering
 * - Geographic area search with bounding box optimization
 */
object WardSpecifications {
    
    /**
     * Creates a specification from search criteria by combining all applicable filters.
     *
     * @param criteria Search criteria containing filter parameters
     * @return Combined specification with all filters applied
     */
    fun withSearchCriteria(criteria: WardSearchCriteria): Specification<Ward> =
        Specification
            .where(withMunicipalityCode(criteria))
            .and(withDistrictCode(criteria))
            .and(withProvinceCode(criteria))
            .and(withWardNumber(criteria))
            .and(withPopulationRange(criteria))
            .and(withAreaRange(criteria))
            .and(withWardNumberRange(criteria))
            .and(withGeographicSearch(criteria))

    /**
     * Builds municipality code specification with join to municipality entity.
     *
     * @param criteria Search criteria containing municipality code
     * @return Specification filtering wards by their municipality code
     */
    private fun withMunicipalityCode(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            criteria.municipalityCode?.let { code ->
                val municipality = root.join<Ward, Any>("municipality", JoinType.INNER)
                cb.equal(
                    cb.lower(municipality.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Builds district code specification with joins to municipality and district entities.
     *
     * @param criteria Search criteria containing district code
     * @return Specification filtering wards by their district code
     */
    private fun withDistrictCode(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            criteria.districtCode?.let { code ->
                val municipality = root.join<Ward, Any>("municipality", JoinType.INNER)
                val district = municipality.join<Any, Any>("district", JoinType.INNER)
                cb.equal(
                    cb.lower(district.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Builds province code specification with joins to municipality, district, and province entities.
     *
     * @param criteria Search criteria containing province code
     * @return Specification filtering wards by their province code
     */
    private fun withProvinceCode(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            criteria.provinceCode?.let { code ->
                val municipality = root.join<Ward, Any>("municipality", JoinType.INNER)
                val district = municipality.join<Any, Any>("district", JoinType.INNER)
                val province = district.join<Any, Any>("province", JoinType.INNER)
                cb.equal(
                    cb.lower(province.get("code")),
                    code.lowercase()
                )
            }
        }

    /**
     * Builds ward number exact match specification.
     *
     * @param criteria Search criteria containing ward number
     * @return Specification for exact ward number matching
     */
    private fun withWardNumber(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            criteria.wardNumber?.let { number ->
                cb.equal(root.get<Int>("wardNumber"), number)
            }
        }

    /**
     * Builds population range specification.
     *
     * @param criteria Search criteria containing min/max population
     * @return Specification filtering wards by their population range
     */
    private fun withPopulationRange(criteria: WardSearchCriteria): Specification<Ward> =
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
     * @return Specification filtering wards by their area range
     */
    private fun withAreaRange(criteria: WardSearchCriteria): Specification<Ward> =
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
     * Builds ward number range specification.
     *
     * @param criteria Search criteria containing ward number range
     * @return Specification filtering wards by their ward number range
     */
    private fun withWardNumberRange(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.wardNumberFrom?.let { from ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("wardNumber"), from))
            }

            criteria.wardNumberTo?.let { to ->
                predicates.add(cb.lessThanOrEqualTo(root.get("wardNumber"), to))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Builds geographic search specification using a bounding box approach.
     *
     * @param criteria Search criteria containing coordinates and radius
     * @return Specification filtering wards within the geographic bounding box
     */
    private fun withGeographicSearch(criteria: WardSearchCriteria): Specification<Ward> =
        Specification { root, _, cb ->
            if (criteria.isGeographicSearch()) {
                val (minLat, maxLat, minLon, maxLon) =
                    calculateBoundingBox(
                        criteria.latitude!!.toDouble(),
                        criteria.longitude!!.toDouble(),
                        criteria.radiusKm!!
                    )

                cb.and(
                    cb.greaterThanOrEqualTo(root.get<BigDecimal>("latitude"), BigDecimal.valueOf(minLat)),
                    cb.lessThanOrEqualTo(root.get<BigDecimal>("latitude"), BigDecimal.valueOf(maxLat)),
                    cb.greaterThanOrEqualTo(root.get<BigDecimal>("longitude"), BigDecimal.valueOf(minLon)),
                    cb.lessThanOrEqualTo(root.get<BigDecimal>("longitude"), BigDecimal.valueOf(maxLon))
                )
            } else {
                null
            }
        }

    /**
     * Calculates a bounding box for geographical searches.
     * This approximation is more efficient than precise distance calculations for initial filtering.
     *
     * @param lat Center latitude
     * @param lon Center longitude
     * @param radiusKm Search radius in kilometers
     * @return BoundingBox with min/max latitude and longitude values
     */
    private fun calculateBoundingBox(
        lat: Double,
        lon: Double,
        radiusKm: Double
    ): BoundingBox {
        val earthRadiusKm = 6371.0
        val latRadians = Math.toRadians(lat)

        val latKm = radiusKm / earthRadiusKm
        val lonKm = radiusKm / (earthRadiusKm * cos(latRadians))

        val latDelta = Math.toDegrees(latKm)
        val lonDelta = Math.toDegrees(lonKm)

        return BoundingBox(
            minLat = lat - latDelta,
            maxLat = lat + latDelta,
            minLon = lon - lonDelta,
            maxLon = lon + lonDelta
        )
    }

    /**
     * Value class representing geographic bounding box coordinates.
     */
    private data class BoundingBox(
        val minLat: Double,
        val maxLat: Double,
        val minLon: Double,
        val maxLon: Double
    )
}
