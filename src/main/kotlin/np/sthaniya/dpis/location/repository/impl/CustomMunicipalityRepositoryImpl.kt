package np.sthaniya.dpis.location.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.Tuple
import np.sthaniya.dpis.common.repository.BaseHibernateRepository
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.MunicipalityType
import np.sthaniya.dpis.location.repository.CustomMunicipalityRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*

/**
 * Custom implementation of Municipality repository operations.
 * 
 * Provides complex query functionality beyond the standard repository methods:
 * - Filtering by district code
 * - Case-insensitive code lookups
 * - Geospatial searches
 * - Municipality type statistics
 * - Advanced filtering with complex criteria
 */
class CustomMunicipalityRepositoryImpl(
    entityManager: EntityManager,
) : BaseHibernateRepository(entityManager),
    CustomMunicipalityRepository {

    /**
     * Finds all municipalities belonging to a specific district.
     *
     * @param districtCode The district code to filter by
     * @return List of municipalities in the specified district
     */
    override fun findByDistrictCode(districtCode: String): List<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        query.where(
            cb.equal(district.get<String>("code"), districtCode)
        )

        return entityManager.createQuery(query).resultList
    }

    /**
     * Finds a municipality by its code and district code.
     *
     * @param code The municipality code to search for
     * @param districtCode The district code to filter by
     * @return Optional containing the municipality if found
     */
    override fun findByCodeAndDistrictCode(
        code: String,
        districtCode: String,
    ): Optional<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        query.where(
            cb.and(
                cb.equal(municipality.get<String>("code"), code),
                cb.equal(district.get<String>("code"), districtCode)
            )
        )

        return entityManager
            .createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * Finds municipalities within a specified radius of a geographic point.
     *
     * @param latitude The center latitude
     * @param longitude The center longitude
     * @param radiusInMeters The search radius in meters
     * @param pageable Pagination and sorting information
     * @return Page of municipalities near the specified point, sorted by distance
     */
    override fun findNearby(
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusInMeters: Double,
        pageable: Pageable,
    ): Page<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)

        // Create spatial distance calculation using native SQL function
        val distanceFunction = cb.function(
            "ST_Distance_Sphere",
            Double::class.java,
            cb.function(
                "point", 
                Any::class.java, 
                municipality.get<BigDecimal>("longitude"), 
                municipality.get<BigDecimal>("latitude")
            ),
            cb.function(
                "point", 
                Any::class.java, 
                cb.literal(longitude), 
                cb.literal(latitude)
            )
        )

        query.where(
            cb.lessThanOrEqualTo(distanceFunction, radiusInMeters)
        )

        query.orderBy(cb.asc(distanceFunction))

        val results = entityManager
            .createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList

        val total = executeCountQuery(cb) { root ->
            cb.lessThanOrEqualTo(
                cb.function(
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
                ),
                radiusInMeters
            )
        }

        return PageImpl(results, pageable, total)
    }

    /**
     * Finds municipalities by type and district code.
     *
     * @param type The municipality type to filter by
     * @param districtCode The district code to filter by
     * @return List of municipalities of the specified type in the district
     */
    override fun findByTypeAndDistrict(
        type: MunicipalityType,
        districtCode: String,
    ): List<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        query.where(
            cb.and(
                cb.equal(municipality.get<MunicipalityType>("type"), type),
                cb.equal(district.get<String>("code"), districtCode)
            )
        )

        return entityManager.createQuery(query).resultList
    }

    /**
     * Finds municipalities that meet minimum population and area criteria.
     *
     * @param minPopulation Minimum population threshold
     * @param minArea Minimum area threshold
     * @param pageable Pagination and sorting information
     * @return Page of municipalities meeting the criteria
     */
    override fun findLargeMunicipalities(
        minPopulation: Long,
        minArea: BigDecimal,
        pageable: Pageable,
    ): Page<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)

        // Create predicates directly
        query.where(
            cb.and(
                cb.greaterThanOrEqualTo(municipality.get<Long>("population"), minPopulation),
                cb.greaterThanOrEqualTo(municipality.get<BigDecimal>("area"), minArea)
            )
        )

        // Add sorting
        if (pageable.sort.isSorted) {
            val orders = mutableListOf<jakarta.persistence.criteria.Order>()
            pageable.sort.forEach { order ->
                val orderExpression = when (order.property) {
                    "population" -> municipality.get<Long>("population")
                    "area" -> municipality.get<BigDecimal>("area")
                    else -> municipality.get<Any>(order.property)
                }
                orders.add(
                    if (order.isAscending) cb.asc(orderExpression) else cb.desc(orderExpression)
                )
            }
            query.orderBy(orders)
        }

        // Execute main query
        val results = entityManager
            .createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList

        // Count query with fresh criteria
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Municipality::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.and(
                cb.greaterThanOrEqualTo(countRoot.get<Long>("population"), minPopulation),
                cb.greaterThanOrEqualTo(countRoot.get<BigDecimal>("area"), minArea)
            )
        )

        val total = entityManager.createQuery(countQuery).singleResult

        return PageImpl(results, pageable, total)
    }

    /**
     * Checks if a municipality with the given code exists within a specific district.
     *
     * @param code The municipality code to check for
     * @param districtCode The district code to check within
     * @return true if a matching municipality exists, false otherwise
     */
    override fun existsByCodeAndDistrict(
        code: String,
        districtCode: String,
    ): Boolean {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Boolean::class.java)
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        val predicates = mutableListOf(
            cb.equal(municipality.get<String>("code"), code),
            cb.equal(district.get<String>("code"), districtCode)
        )

        query
            .select(cb.literal(true))
            .where(*predicates.toTypedArray())

        return entityManager
            .createQuery(query)
            .setMaxResults(1)
            .resultList
            .isNotEmpty()
    }

    /**
     * Counts municipalities by type within a district.
     *
     * @param districtCode The district code to filter by
     * @return Map of municipality types to their counts
     */
    override fun countByTypeAndDistrict(districtCode: String): Map<MunicipalityType, Long> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createTupleQuery()
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        query
            .multiselect(
                municipality.get<MunicipalityType>("type"),
                cb.count(municipality)
            ).where(
                cb.equal(district.get<String>("code"), districtCode)
            ).groupBy(
                municipality.get<MunicipalityType>("type")
            )

        return entityManager
            .createQuery(query)
            .resultList
            .associate { tuple: Tuple ->
                tuple.get(0, MunicipalityType::class.java) to tuple.get(1, Long::class.java)
            }
    }

    /**
     * Gets the total population of all municipalities in a district.
     *
     * @param districtCode The district code to filter by
     * @return Total population or null if no data available
     */
    override fun getTotalPopulationByDistrict(districtCode: String): Long? {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val municipality = query.from(Municipality::class.java)
        val district = municipality.join<Municipality, Any>("district")

        query
            .select(cb.sum(municipality.get<Long>("population")))
            .where(
                cb.equal(district.get<String>("code"), districtCode)
            )

        return entityManager.createQuery(query).singleResult
    }

    /**
     * Finds municipalities that meet a minimum ward count.
     *
     * @param minWards Minimum number of wards
     * @param pageable Pagination and sorting information
     * @return Page of municipalities meeting the criteria
     */
    override fun findByMinimumWards(
        minWards: Int,
        pageable: Pageable,
    ): Page<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)

        query.where(
            cb.greaterThanOrEqualTo(municipality.get<Int>("totalWards"), minWards)
        )

        // Add sorting
        val orders = pageable.sort
            .map { order ->
                if (order.isAscending) {
                    cb.asc(municipality.get<Any>(order.property))
                } else {
                    cb.desc(municipality.get<Any>(order.property))
                }
            }.toList()

        query.orderBy(orders)

        val typedQuery = entityManager
            .createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)

        // Count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Municipality::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.greaterThanOrEqualTo(countRoot.get<Int>("totalWards"), minWards)
        )

        val total = entityManager.createQuery(countQuery).singleResult

        return PageImpl(typedQuery.resultList, pageable, total)
    }

    /**
     * Finds a municipality by its code, ignoring case.
     *
     * @param code The municipality code to search for (case-insensitive)
     * @return Optional containing the municipality if found
     */
    override fun findByCodeIgnoreCase(code: String): Optional<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)

        query.where(
            cb.equal(cb.lower(municipality.get<String>("code")), code.lowercase())
        )

        return entityManager
            .createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * Checks if a municipality with the given code exists (case-insensitive).
     *
     * @param code The municipality code to check for (case-insensitive)
     * @return true if a municipality with the code exists, false otherwise
     */
    override fun existsByCodeIgnoreCase(code: String): Boolean {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Boolean::class.java)
        val municipality = query.from(Municipality::class.java)

        query
            .select(cb.literal(true))
            .where(
                cb.equal(cb.lower(municipality.get<String>("code")), code.lowercase())
            )

        return entityManager
            .createQuery(query)
            .setMaxResults(1)
            .resultList
            .isNotEmpty()
    }

    /**
     * Finds all municipalities of a specific type.
     *
     * @param type The municipality type to filter by
     * @return List of municipalities of the specified type
     */
    override fun findByType(type: MunicipalityType): List<Municipality> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Municipality::class.java)
        val municipality = query.from(Municipality::class.java)

        query.where(
            cb.equal(municipality.get<MunicipalityType>("type"), type)
        )

        return entityManager.createQuery(query).resultList
    }

    /**
     * Converts a database Tuple to Municipality entity.
     *
     * @param tuple Database result tuple
     * @return Mapped Municipality entity
     */
    private fun mapTupleToMunicipality(tuple: Tuple): Municipality =
        Municipality().apply {
            name = tuple.get("name") as String
            code = tuple.get("code") as String
            // Map other fields based on the query selection
            population = (tuple.get("population") as Number?)?.toLong()
            area = (tuple.get("area") as Number?)?.let { BigDecimal(it.toString()) }
            totalWards = (tuple.get("totalWards") as Number?)?.toInt()
        }

    /**
     * Helper method to execute count query with a predicate.
     *
     * @param cb CriteriaBuilder instance
     * @param wherePredicate Function that builds the where clause predicate
     * @return Count of matching entities
     */
    private fun executeCountQuery(
        cb: jakarta.persistence.criteria.CriteriaBuilder,
        wherePredicate: (jakarta.persistence.criteria.Root<Municipality>) -> jakarta.persistence.criteria.Predicate,
    ): Long {
        val countQuery = cb.createQuery(Long::class.java)
        val root = countQuery.from(Municipality::class.java)

        countQuery
            .select(cb.count(root))
            .where(wherePredicate(root))

        return entityManager.createQuery(countQuery).singleResult
    }
}
