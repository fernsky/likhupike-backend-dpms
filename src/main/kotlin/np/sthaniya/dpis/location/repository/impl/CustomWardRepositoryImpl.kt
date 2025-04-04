package np.sthaniya.dpis.location.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.Tuple
import np.sthaniya.dpis.common.repository.BaseHibernateRepository
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.Ward
import np.sthaniya.dpis.location.repository.CustomWardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Custom implementation of Ward repository operations.
 * 
 * Provides complex query functionality beyond the standard repository methods:
 * - Filtering by municipality, district, and province code
 * - Ward number range queries
 * - Population-based filtering
 * - Hierarchical location data access
 */
class CustomWardRepositoryImpl(
    entityManager: EntityManager,
) : BaseHibernateRepository(entityManager),
    CustomWardRepository {
    
    /**
     * Finds all wards belonging to a specific municipality.
     *
     * @param municipalityCode The municipality code to filter by
     * @return List of wards in the specified municipality
     */
    override fun findByMunicipalityCode(municipalityCode: String): List<Ward> {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")

        query.where(
            cb.equal(municipality.get<String>("code"), municipalityCode)
        )

        return session.createQuery(query).resultList
    }

    /**
     * Finds all wards belonging to municipalities in a specific district.
     *
     * @param districtCode The district code to filter by
     * @return List of wards in the specified district
     */
    override fun findByDistrictCode(districtCode: String): List<Ward> {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")
        val district = municipality.join<Any, Any>("district")

        query.where(
            cb.equal(district.get<String>("code"), districtCode)
        )

        return session.createQuery(query).resultList
    }

    /**
     * Finds all wards belonging to municipalities in a specific province.
     *
     * @param provinceCode The province code to filter by
     * @return List of wards in the specified province
     */
    override fun findByProvinceCode(provinceCode: String): List<Ward> {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")
        val district = municipality.join<Any, Any>("district")
        val province = district.join<Any, Any>("province")

        query.where(
            cb.equal(province.get<String>("code"), provinceCode)
        )

        return session.createQuery(query).resultList
    }

    /**
     * Finds a ward by its ward number and municipality code.
     *
     * @param wardNumber The ward number to search for
     * @param municipalityCode The municipality code to search within
     * @return Optional containing the ward if found
     */
    override fun findByWardNumberAndMunicipalityCode(
        wardNumber: Int,
        municipalityCode: String,
    ): Optional<Ward> {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")

        query.where(
            cb.and(
                cb.equal(ward.get<Int>("wardNumber"), wardNumber),
                cb.equal(municipality.get<String>("code"), municipalityCode)
            )
        )

        return session
            .createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * Finds wards within a specified ward number range in a municipality.
     *
     * @param municipalityCode The municipality code to filter by
     * @param fromWard The minimum ward number (inclusive)
     * @param toWard The maximum ward number (inclusive)
     * @return List of wards within the specified range
     */
    override fun findByWardNumberRange(
        municipalityCode: String,
        fromWard: Int,
        toWard: Int,
    ): List<Ward> {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")

        query.where(
            cb.and(
                cb.equal(municipality.get<String>("code"), municipalityCode),
                cb.between(ward.get<Int>("wardNumber"), fromWard, toWard)
            )
        )

        return session.createQuery(query).resultList
    }

    /**
     * Checks if a ward with the given ward number exists in a specific municipality.
     *
     * @param wardNumber The ward number to check for
     * @param municipalityCode The municipality code to check within
     * @return true if a matching ward exists, false otherwise
     */
    override fun existsByWardNumberAndMunicipality(
        wardNumber: Int,
        municipalityCode: String,
    ): Boolean {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Boolean::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")

        query
            .select(cb.literal(true))
            .where(
                cb.and(
                    cb.equal(ward.get<Int>("wardNumber"), wardNumber),
                    cb.equal(municipality.get<String>("code"), municipalityCode)
                )
            )

        return session
            .createQuery(query)
            .setMaxResults(1)
            .resultList
            .isNotEmpty()
    }

    /**
     * Finds wards within a specified population range.
     *
     * @param minPopulation The minimum population (inclusive)
     * @param maxPopulation The maximum population (inclusive)
     * @param pageable Pagination and sorting information
     * @return Page of wards within the specified population range
     */
    override fun findByPopulationRange(
        minPopulation: Long,
        maxPopulation: Long,
        pageable: Pageable,
    ): Page<Ward> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Ward::class.java)
        val ward = query.from(Ward::class.java)

        query.where(
            cb.between(ward.get<Long>("population"), minPopulation, maxPopulation)
        )

        // Add sorting
        val orders = pageable.sort
            .map { order ->
                if (order.isAscending) {
                    cb.asc(ward.get<Any>(order.property))
                } else {
                    cb.desc(ward.get<Any>(order.property))
                }
            }.toList()

        query.orderBy(orders)

        val typedQuery = entityManager
            .createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)

        // Count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Ward::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.between(countRoot.get<Long>("population"), minPopulation, maxPopulation)
        )

        val total = entityManager.createQuery(countQuery).singleResult

        return PageImpl(typedQuery.resultList, pageable, total)
    }

    /**
     * Counts the number of wards in a municipality.
     *
     * @param municipalityCode The municipality code to count wards for
     * @return Count of wards in the specified municipality
     */
    override fun countByMunicipalityCode(municipalityCode: String): Int {
        val cb = session.criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val ward = query.from(Ward::class.java)
        val municipality = ward.join<Ward, Any>("municipality")

        query
            .select(cb.count(ward))
            .where(
                cb.equal(municipality.get<String>("code"), municipalityCode)
            )

        return session.createQuery(query).singleResult.toInt()
    }

    /**
     * Converts a database Tuple to Ward entity.
     *
     * @param tuple Database result tuple
     * @return Mapped Ward entity
     */
    private fun mapTupleToWard(tuple: Tuple): Ward =
        Ward().apply {
            wardNumber = tuple.get(0) as Int
            population = tuple.get(1) as Long
            municipality = Municipality().apply {
                code = tuple.get(2) as String
            }
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
        wherePredicate: (jakarta.persistence.criteria.Root<Ward>) -> jakarta.persistence.criteria.Predicate,
    ): Long {
        val countQuery = cb.createQuery(Long::class.java)
        val root = countQuery.from(Ward::class.java)

        countQuery
            .select(cb.count(root))
            .where(wherePredicate(root))

        return session.createQuery(countQuery).singleResult
    }
}
