package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjectionImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Order

/**
 * Implementation handling the projection operations for citizens.
 * 
 * This class is responsible for:
 * - Converting standard queries to projections
 * - Efficient data fetching with specific fields
 * - Pagination of projection results
 * - Distinct counting for complex queries
 */
class CitizenProjectionRepositoryImpl(entityManager: EntityManager) : BaseCitizenRepositoryImpl(entityManager) {
    
    /**
     * Finds all citizens that match the given specification and returns them as projections
     * with only the requested fields included.
     *
     * @param spec Specification to filter citizens
     * @param pageable Pagination information with 1-based page numbering
     * @param columns Set of column names to include in the projection
     * @return Page of citizen projections with only the requested fields
     */
    fun findAllWithProjection(
        spec: Specification<Citizen>,
        pageable: Pageable,
        columns: Set<String>
    ): Page<CitizenProjection> {
        val cb = entityManager.criteriaBuilder
        
        // Count query - always use distinct to handle potential address joins correctly
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        
        countQuery.select(cb.countDistinct(countRoot))
        
        // Apply specification to count query
        spec.toPredicate(countRoot, countQuery, cb)?.let { countQuery.where(it) }
        val total = entityManager.createQuery(countQuery).singleResult
        
        // If no results or invalid page (beyond available results), return empty page
        val zeroBasedPage = pageable.pageNumber
        val offset = zeroBasedPage * pageable.pageSize.toLong()
        if (total == 0L || offset >= total) {
            return PageImpl(emptyList(), pageable, total)
        }
        
        // Main query to fetch data
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // If we need address fields, fetch the associations
        if (columns.any { it.startsWith("permanent") || it.startsWith("temporary") }) {
            // No need for explicit joins as these are embedded objects
        }
        
        // Apply distinct if necessary
        val needsDistinct = columns.any {
            it.startsWith("permanent") || it.startsWith("temporary")
        }
        
        if (needsDistinct) {
            query.distinct(true)
        }
        
        // Apply specification to main query
        spec.toPredicate(root, query, cb)?.let { query.where(it) }
        
        // Apply sorting
        if (pageable.sort.isSorted) {
            val orders = ArrayList<Order>()
            pageable.sort.forEach { order ->
                val path = root.get<Any>(order.property)
                if (order.isAscending) {
                    orders.add(cb.asc(path))
                } else {
                    orders.add(cb.desc(path))
                }
            }
            query.orderBy(orders)
        }
        
        // Execute query with pagination
        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            
        // Convert results to projections
        val projections = results.map { citizen ->
            CitizenProjectionImpl(citizen as Citizen, columns)
        }
        
        return PageImpl(projections, pageable, total)
    }
    
    /**
     * Counts the number of citizens that match the given specification with distinct handling.
     *
     * @param spec Specification to filter citizens
     * @return Count of matching citizens
     */
    fun countDistinct(spec: Specification<Citizen>): Long {
        val cb = entityManager.criteriaBuilder
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        
        countQuery.select(cb.countDistinct(countRoot))
        
        spec.toPredicate(countRoot, countQuery, cb)?.let { countQuery.where(it) }
        
        return entityManager.createQuery(countQuery).singleResult
    }
}
