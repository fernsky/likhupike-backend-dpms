package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import java.util.UUID

/**
 * Base class for Citizen repository implementations.
 * 
 * This abstract class provides common functionality and utilities for all
 * citizen repository implementations, reducing duplication and promoting
 * consistent patterns across the repository layer.
 *
 * @property entityManager JPA EntityManager for query execution
 */
abstract class BaseCitizenRepositoryImpl(
    protected val entityManager: EntityManager
) {
    /**
     * Creates a basic predicate to filter out deleted citizens
     */
    protected fun createNotDeletedPredicate(root: jakarta.persistence.criteria.Root<Citizen>) =
        entityManager.criteriaBuilder.equal(root.get<Boolean>("isDeleted"), false)
    
    /**
     * Creates a case-insensitive like predicate for string fields
     */
    protected fun createCaseInsensitiveLikePredicate(
        field: jakarta.persistence.criteria.Expression<String>,
        value: String
    ): Predicate {
        val cb = entityManager.criteriaBuilder
        return cb.like(cb.lower(field), "%${value.lowercase()}%")
    }
    
    /**
     * Utility to create a list of predicates for a query
     */
    protected fun createPredicateList() = mutableListOf<Predicate>()
    
    /**
     * Utility to get a citizen by ID with a non-deleted check
     */
    protected fun getByIdNotDeleted(id: UUID): Citizen? {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        query.where(
            cb.and(
                cb.equal(root.get<UUID>("id"), id),
                createNotDeletedPredicate(root)
            )
        )
        
        return entityManager.createQuery(query)
            .resultList
            .firstOrNull()
    }
}
