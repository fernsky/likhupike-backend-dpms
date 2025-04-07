package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

/**
 * Implementation of state-related repository operations for citizens.
 *
 * This class handles operations related to citizen verification state management,
 * including searching for citizens in specific states or with specific document states.
 */
class CitizenStateRepositoryImpl(entityManager: EntityManager) : BaseCitizenRepositoryImpl(entityManager) {

    /**
     * Finds citizens by their current state in the verification workflow.
     *
     * @param state The state to filter by
     * @param pageable Pagination information
     * @return Page of citizens in the specified state
     */
    fun findByState(state: CitizenState, pageable: Pageable): Page<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        query.where(
            cb.and(
                cb.equal(root.get<CitizenState>("state"), state),
                createNotDeletedPredicate(root)
            )
        )
        
        // Apply sorting from pageable
        if (pageable.sort.isSorted) {
            val orders = pageable.sort.map { order ->
                if (order.isAscending) {
                    cb.asc(root.get<Any>(order.property))
                } else {
                    cb.desc(root.get<Any>(order.property))
                }
            }.toMutableList() // Convert to MutableList
            
            query.orderBy(orders)
        }
        
        // Execute count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.and(
                cb.equal(countRoot.get<CitizenState>("state"), state),
                createNotDeletedPredicate(countRoot)
            )
        )
        val total = entityManager.createQuery(countQuery).singleResult
        
        // Execute result query with pagination
        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
        
        return PageImpl(results, pageable, total)
    }
    
    /**
     * Finds citizens who need action from administrators.
     * This includes those in PENDING_REGISTRATION and ACTION_REQUIRED states.
     *
     * @param pageable Pagination information
     * @return Page of citizens requiring administrator action
     */
    fun findCitizensRequiringAction(pageable: Pageable): Page<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // Find citizens in PENDING_REGISTRATION or ACTION_REQUIRED states
        val actionStates = listOf(CitizenState.PENDING_REGISTRATION, CitizenState.ACTION_REQUIRED)
        
        query.where(
            cb.and(
                root.get<CitizenState>("state").`in`(actionStates),
                createNotDeletedPredicate(root)
            )
        )
        
        // Apply sorting from pageable
        if (pageable.sort.isSorted) {
            val orders = pageable.sort.map { order ->
                if (order.isAscending) {
                    cb.asc(root.get<Any>(order.property))
                } else {
                    cb.desc(root.get<Any>(order.property))
                }
            }.toMutableList() // Convert to MutableList
            
            query.orderBy(orders)
        } else {
            // Default sort by state updated time (newest first)
            query.orderBy(cb.desc(root.get<Any>("stateUpdatedAt")))
        }
        
        // Execute count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        countQuery.select(cb.count(countRoot))
        countQuery.where(
            cb.and(
                countRoot.get<CitizenState>("state").`in`(actionStates),
                createNotDeletedPredicate(countRoot)
            )
        )
        val total = entityManager.createQuery(countQuery).singleResult
        
        // Execute result query with pagination
        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
        
        return PageImpl(results, pageable, total)
    }
    
    /**
     * Finds citizens with missing or rejected documents.
     *
     * @param documentState The document state to filter by (e.g., NOT_UPLOADED, REJECTED_BLURRY)
     * @param pageable Pagination information
     * @return Page of citizens with documents in the specified state
     */
    fun findByDocumentState(documentState: DocumentState, pageable: Pageable): Page<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        val predicates = createPredicateList()
        
        // Check all document state fields
        predicates.add(cb.equal(root.get<DocumentState>("photoState"), documentState))
        predicates.add(cb.equal(root.get<DocumentState>("citizenshipFrontState"), documentState))
        predicates.add(cb.equal(root.get<DocumentState>("citizenshipBackState"), documentState))
        
        query.where(
            cb.and(
                cb.or(*predicates.toTypedArray()),
                createNotDeletedPredicate(root)
            )
        )
        
        // Apply sorting
        if (pageable.sort.isSorted) {
            val orders = pageable.sort.map { order ->
                if (order.isAscending) {
                    cb.asc(root.get<Any>(order.property))
                } else {
                    cb.desc(root.get<Any>(order.property))
                }
            }.toMutableList() // Convert to MutableList
            
            query.orderBy(orders)
        }
        
        // Count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        countQuery.select(cb.count(countRoot))
        
        val countPredicates = createPredicateList()
        countPredicates.add(cb.equal(countRoot.get<DocumentState>("photoState"), documentState))
        countPredicates.add(cb.equal(countRoot.get<DocumentState>("citizenshipFrontState"), documentState))
        countPredicates.add(cb.equal(countRoot.get<DocumentState>("citizenshipBackState"), documentState))
        
        countQuery.where(
            cb.and(
                cb.or(*countPredicates.toTypedArray()),
                createNotDeletedPredicate(countRoot)
            )
        )
        
        val total = entityManager.createQuery(countQuery).singleResult
        
        // Execute result query with pagination
        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
        
        return PageImpl(results, pageable, total)
    }
    
    /**
     * Finds citizens with specific issues in their documents, using a note keyword search.
     *
     * @param noteKeyword Keyword to search for in document state notes
     * @param pageable Pagination information
     * @return Page of citizens with matching document state notes
     */
    fun findByDocumentStateNote(noteKeyword: String, pageable: Pageable): Page<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        val noteLike = "%${noteKeyword.lowercase()}%"
        
        val predicates = createPredicateList()
        
        // Search all document note fields
        predicates.add(
            cb.and(
                cb.isNotNull(root.get<String>("photoStateNote")),
                cb.like(cb.lower(root.get<String>("photoStateNote")), noteLike)
            )
        )
        
        predicates.add(
            cb.and(
                cb.isNotNull(root.get<String>("citizenshipFrontStateNote")),
                cb.like(cb.lower(root.get<String>("citizenshipFrontStateNote")), noteLike)
            )
        )
        
        predicates.add(
            cb.and(
                cb.isNotNull(root.get<String>("citizenshipBackStateNote")),
                cb.like(cb.lower(root.get<String>("citizenshipBackStateNote")), noteLike)
            )
        )
        
        // Also search citizen state notes
        predicates.add(
            cb.and(
                cb.isNotNull(root.get<String>("stateNote")),
                cb.like(cb.lower(root.get<String>("stateNote")), noteLike)
            )
        )
        
        query.where(
            cb.and(
                cb.or(*predicates.toTypedArray()),
                createNotDeletedPredicate(root)
            )
        )
        
        // Apply sorting
        if (pageable.sort.isSorted) {
            val orders = pageable.sort.map { order ->
                if (order.isAscending) {
                    cb.asc(root.get<Any>(order.property))
                } else {
                    cb.desc(root.get<Any>(order.property))
                }
            }.toMutableList() // Convert to MutableList
            
            query.orderBy(orders)
        }
        
        // Count query
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Citizen::class.java)
        countQuery.select(cb.count(countRoot))
        
        val countPredicates = createPredicateList()
        
        countPredicates.add(
            cb.and(
                cb.isNotNull(countRoot.get<String>("photoStateNote")),
                cb.like(cb.lower(countRoot.get<String>("photoStateNote")), noteLike)
            )
        )
        
        countPredicates.add(
            cb.and(
                cb.isNotNull(countRoot.get<String>("citizenshipFrontStateNote")),
                cb.like(cb.lower(countRoot.get<String>("citizenshipFrontStateNote")), noteLike)
            )
        )
        
        countPredicates.add(
            cb.and(
                cb.isNotNull(countRoot.get<String>("citizenshipBackStateNote")),
                cb.like(cb.lower(countRoot.get<String>("citizenshipBackStateNote")), noteLike)
            )
        )
        
        countPredicates.add(
            cb.and(
                cb.isNotNull(countRoot.get<String>("stateNote")),
                cb.like(cb.lower(countRoot.get<String>("stateNote")), noteLike)
            )
        )
        
        countQuery.where(
            cb.and(
                cb.or(*countPredicates.toTypedArray()),
                createNotDeletedPredicate(countRoot)
            )
        )
        
        val total = entityManager.createQuery(countQuery).singleResult
        
        // Execute result query with pagination
        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
        
        return PageImpl(results, pageable, total)
    }
}
