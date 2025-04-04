package np.sthaniya.dpis.location.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import np.sthaniya.dpis.common.repository.BaseHibernateRepository
import np.sthaniya.dpis.location.domain.Province
import np.sthaniya.dpis.location.repository.CustomProvinceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*

/**
 * Custom implementation of Province repository operations.
 * 
 * Provides complex query functionality beyond the standard repository methods:
 * - Case-insensitive code lookups
 * - Advanced filtering with area and population criteria
 * - Optimized counting queries
 */
class CustomProvinceRepositoryImpl(
    entityManager: EntityManager,
) : BaseHibernateRepository(entityManager),
    CustomProvinceRepository {
    
    /**
     * Finds a province by its code, ignoring case.
     *
     * @param code The province code to search for (case-insensitive)
     * @return Optional containing the province if found
     */
    override fun findByCodeIgnoreCase(code: String): Optional<Province> {
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(Province::class.java)
        val province = criteriaQuery.from(Province::class.java)

        criteriaQuery.where(
            cb.equal(
                cb.lower(province.get<String>("code")),
                code.lowercase()
            )
        )

        return entityManager
            .createQuery(criteriaQuery)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * Finds provinces that meet minimum area and population criteria.
     *
     * @param minArea Minimum area threshold
     * @param minPopulation Minimum population threshold
     * @param pageable Pagination and sorting information
     * @return Page of provinces meeting the criteria
     */
    override fun findLargeProvinces(
        minArea: BigDecimal,
        minPopulation: Long,
        pageable: Pageable,
    ): Page<Province> {
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(Province::class.java)
        val province = criteriaQuery.from(Province::class.java)

        criteriaQuery.where(
            cb.and(
                cb.greaterThanOrEqualTo(province.get<BigDecimal>("area"), minArea),
                cb.greaterThanOrEqualTo(province.get<Long>("population"), minPopulation)
            )
        )

        // Add sorting
        val orders = pageable.sort
            .map { order ->
                if (order.isAscending) {
                    cb.asc(province.get<Any>(order.property))
                } else {
                    cb.desc(province.get<Any>(order.property))
                }
            }.toList()

        criteriaQuery.orderBy(orders)

        val results = entityManager
            .createQuery(criteriaQuery)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList

        val total = executeCountQuery(cb) { root ->
            cb.and(
                cb.greaterThanOrEqualTo(root.get<BigDecimal>("area"), minArea),
                cb.greaterThanOrEqualTo(root.get<Long>("population"), minPopulation)
            )
        }

        return PageImpl(results, pageable, total)
    }

    /**
     * Checks if a province with the given code exists (case-insensitive).
     *
     * @param code The province code to check for (case-insensitive)
     * @return true if a province with the code exists, false otherwise
     */
    override fun existsByCode(code: String): Boolean {
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(Long::class.java)
        val province = criteriaQuery.from(Province::class.java)

        criteriaQuery
            .select(cb.count(province))
            .where(
                cb.equal(
                    cb.lower(province.get<String>("code")),
                    code.lowercase()
                )
            )

        return entityManager
            .createQuery(criteriaQuery)
            .singleResult > 0
    }

    /**
     * Helper method to execute count query with a predicate.
     *
     * @param cb CriteriaBuilder instance
     * @param wherePredicate Function that builds the where clause predicate
     * @return Count of matching entities
     */
    private fun executeCountQuery(
        cb: CriteriaBuilder,
        wherePredicate: (jakarta.persistence.criteria.Root<Province>) -> jakarta.persistence.criteria.Predicate,
    ): Long {
        val criteriaQuery = cb.createQuery(Long::class.java)
        val root = criteriaQuery.from(Province::class.java)

        criteriaQuery
            .select(cb.count(root))
            .where(wherePredicate(root))

        return entityManager.createQuery(criteriaQuery).singleResult
    }
}
