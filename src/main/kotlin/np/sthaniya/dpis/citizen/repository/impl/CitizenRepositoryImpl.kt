package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.repository.CitizenRepositoryCustom
import java.util.Optional
import java.util.UUID

/**
 * Implementation of custom repository operations for Citizen entities.
 *
 * This class provides:
 * - Custom address search functionality
 * - Optimized document-related queries
 * - Name search functionality
 *
 * Features:
 * - Complex address search across multiple embedded fields
 * - Criteria API based implementations
 * - Efficient fetch strategies for document relationships
 *
 * @property entityManager JPA EntityManager for query execution
 */
class CitizenRepositoryImpl(
    private val entityManager: EntityManager
) : CitizenRepositoryCustom {

    /**
     * Finds a citizen by ID and eagerly loads related document storage keys.
     *
     * @param id The UUID of the citizen to search for
     * @return An Optional containing the citizen with document information if found
     */
    override fun findByIdWithDocuments(id: UUID): Optional<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        query.where(
            cb.and(
                cb.equal(root.get<UUID>("id"), id),
                cb.equal(root.get<Boolean>("isDeleted"), false)
            )
        )

        return entityManager.createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }
    
    /**
     * Searches for citizens by name with partial matching.
     *
     * @param namePart Part of the citizen name to search for
     * @return List of citizens matching the name pattern
     */
    override fun searchByNameContaining(namePart: String): List<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // Case-insensitive like comparison
        val nameLike = "%${namePart.lowercase()}%"
        val predicate = cb.like(cb.lower(root.get("name")), nameLike)
        
        query.where(
            cb.and(
                predicate,
                cb.equal(root.get<Boolean>("isDeleted"), false)
            )
        )
        
        return entityManager.createQuery(query).resultList
    }
    
    /**
     * Finds citizens by address with partial matching for both
     * permanent and temporary addresses.
     *
     * This implementation searches across all address fields:
     * - Province
     * - District
     * - Municipality 
     * - Street Address
     *
     * @param addressPart Part of the address to search for
     * @return List of citizens matching the address pattern
     */
    override fun findByAddressContaining(addressPart: String): List<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // Case-insensitive like comparison
        val addressLike = "%${addressPart.lowercase()}%"
        
        // Create predicates for both permanent and temporary addresses
        val predicates = mutableListOf<Predicate>()
        
        // Permanent address fields
        predicates.add(
            cb.like(cb.lower(root.get<String>("permanentAddress").get<String>("province")), addressLike)
        )
        predicates.add(
            cb.like(cb.lower(root.get<String>("permanentAddress").get<String>("district")), addressLike)
        )
        predicates.add(
            cb.like(cb.lower(root.get<String>("permanentAddress").get<String>("municipality")), addressLike)
        )
        
        // Street address might be null, so handle it separately
        predicates.add(
            cb.and(
                cb.isNotNull(root.get<String>("permanentAddress").get<String>("streetAddress")),
                cb.like(
                    cb.lower(root.get<String>("permanentAddress").get<String>("streetAddress")), 
                    addressLike
                )
            )
        )
        
        // Temporary address fields (these might be null, so handle accordingly)
        val tempAddressPath = root.get<String>("temporaryAddress")
        
        // Check if temporary address exists before comparing
        val tempAddressNotNull = cb.isNotNull(tempAddressPath)
        
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(cb.lower(tempAddressPath.get<String>("province")), addressLike)
            )
        )
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(cb.lower(tempAddressPath.get<String>("district")), addressLike)
            )
        )
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(cb.lower(tempAddressPath.get<String>("municipality")), addressLike)
            )
        )
        
        // Street address in temporary address might be null
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.isNotNull(tempAddressPath.get<String>("streetAddress")),
                cb.like(cb.lower(tempAddressPath.get<String>("streetAddress")), addressLike)
            )
        )
        
        // Combine all predicates with OR
        query.where(
            cb.and(
                // At least one of the address fields should match
                cb.or(*predicates.toTypedArray()),
                // User should not be deleted
                cb.equal(root.get<Boolean>("isDeleted"), false)
            )
        )
        
        return entityManager.createQuery(query).resultList
    }
}
