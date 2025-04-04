package np.sthaniya.dpis.location.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.common.repository.BaseHibernateRepository
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.repository.CustomDistrictRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*

/**
 * Custom implementation of District repository operations.
 * 
 * Provides complex query functionality beyond the standard repository methods:
 * - Filtering by province code
 * - Case-insensitive code lookups
 * - Geospatial searches
 * - Advanced filtering with complex criteria
 */
class CustomDistrictRepositoryImpl(
    entityManager: EntityManager,
) : BaseHibernateRepository(entityManager),
    CustomDistrictRepository {
    
    /**
     * Finds all districts belonging to a specific province.
     *
     * @param provinceCode The province code to filter by
     * @return List of districts in the specified province
     */
    override fun findByProvinceCode(provinceCode: String): List<District> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(District::class.java)
        val district = query.from(District::class.java)
        val province = district.join<District, Any>("province")

        query.where(
            cb.equal(province.get<String>("code"), provinceCode)
        )

        return entityManager.createQuery(query).resultList
    }

    /**
     * Finds a district by its exact code.
     *
     * @param code The district code to search for
     * @return Optional containing the district if found
     */
    override fun findByCode(code: String): Optional<District> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(District::class.java)
        val district = query.from(District::class.java)

        query.where(
            cb.equal(district.get<String>("code"), code)
        )

        return entityManager
            .createQuery(query)
            .resultList
            .firstOrNull()
            ?.let { Optional.of(it) }
            ?: Optional.empty()
    }

    /**
     * Finds a district by its code, ignoring case.
     *
     * @param code The district code to search for (case-insensitive)
     * @return Optional containing the district if found
     */
    override fun findByCodeIgnoreCase(code: String): Optional<District> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(District::class.java)
        val district = query.from(District::class.java)

        query.where(
            cb.equal(cb.lower(district.get<String>("code")), code.lowercase())
        )

        return entityManager
            .createQuery(query)
            .resultList
            .firstOrNull()
            ?.let { Optional.of(it) }
            ?: Optional.empty()
    }

    /**
     * Checks if a district with the given code exists.
     *
     * @param code The district code to check for (case-insensitive)
     * @return true if a district with the code exists, false otherwise
     */
    override fun existsByCode(code: String): Boolean {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val district = query.from(District::class.java)

        query
            .select(cb.count(district))
            .where(cb.equal(cb.lower(district.get<String>("code")), code.lowercase()))

        return entityManager.createQuery(query).singleResult > 0
    }

    /**
     * Checks if a district with the given code exists within a specific province.
     *
     * @param code The district code to check for (case-insensitive)
     * @param provinceCode The province code to check within (case-insensitive)
     * @return true if a matching district exists, false otherwise
     */
    override fun existsByCodeAndProvince(
        code: String,
        provinceCode: String,
    ): Boolean {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val district = query.from(District::class.java)
        val province = district.join<District, Any>("province")

        query
            .select(cb.count(district))
            .where(
                cb.and(
                    cb.equal(cb.lower(district.get<String>("code")), code.lowercase()),
                    cb.equal(cb.lower(province.get<String>("code")), provinceCode.lowercase())
                )
            )

        return entityManager.createQuery(query).singleResult > 0
    }

    /**
     * Finds districts that meet minimum population and area criteria.
     *
     * @param minPopulation Minimum population threshold
     * @param minArea Minimum area threshold
     * @param pageable Pagination and sorting information
     * @return Page of districts meeting the criteria
     */
    override fun findLargeDistricts(
        minPopulation: Long,
        minArea: BigDecimal,
        pageable: Pageable,
    ): Page<District> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(District::class.java)
        val district = query.from(District::class.java)

        // Create predicates
        val populationPredicate = cb.greaterThanOrEqualTo(district.get<Long>("population"), minPopulation)
        val areaPredicate = cb.greaterThanOrEqualTo(district.get<BigDecimal>("area"), minArea)
        query.where(cb.and(populationPredicate, areaPredicate))

        // Handle sorting
        if (pageable.sort.isSorted) {
            val orders = mutableListOf<jakarta.persistence.criteria.Order>()
            pageable.sort.forEach { order ->
                val path = district.get<Any>(order.property)
                orders.add(
                    if (order.isAscending) cb.asc(path) else cb.desc(path)
                )
            }
            query.orderBy(orders)
        }

        // Execute query with pagination
        val typedQuery =
            entityManager
                .createQuery(query)
                .setFirstResult(pageable.offset.toInt())
                .setMaxResults(pageable.pageSize)

        // Count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(District::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.and(
                cb.greaterThanOrEqualTo(countRoot.get<Long>("population"), minPopulation),
                cb.greaterThanOrEqualTo(countRoot.get<BigDecimal>("area"), minArea)
            )
        )

        val total = entityManager.createQuery(countQuery).singleResult

        return PageImpl(typedQuery.resultList, pageable, total)
    }

    /**
     * Finds districts that have municipalities within a specified radius of a geographic point.
     * Uses native spatial query for performance optimization.
     *
     * @param latitude The center latitude
     * @param longitude The center longitude
     * @param radiusInMeters The search radius in meters
     * @param pageable Pagination and sorting information
     * @return Page of districts near the specified point
     */
    override fun findNearbyDistricts(
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusInMeters: Double,
        pageable: Pageable,
    ): Page<District> {
        val query = """
            from District d 
            where exists (
                select 1 from Municipality m 
                where m.district = d 
                and st_distance_sphere(
                    point(m.longitude, m.latitude),
                    point(?1, ?2)
                ) <= ?3
            )
        """
        val countQuery = """
            select count(distinct d) 
            from District d 
            where exists (
                select 1 from Municipality m 
                where m.district = d 
                and st_distance_sphere(
                    point(m.longitude, m.latitude),
                    point(?1, ?2)
                ) <= ?3
            )
        """
        val params =
            mapOf(
                1 to longitude,
                2 to latitude,
                3 to radiusInMeters
            )
        return executePagedQuery(query, countQuery, District::class.java, params, pageable, null)
    }
}
