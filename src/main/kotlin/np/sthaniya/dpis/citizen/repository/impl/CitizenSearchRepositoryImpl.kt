package np.sthaniya.dpis.citizen.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import java.util.Optional
import java.util.UUID

/**
 * Implementation of name and address search operations for citizens.
 *
 * This class handles the search functionality for citizens, including
 * name-based searches and address-based searches.
 */
class CitizenSearchRepositoryImpl(entityManager: EntityManager) : BaseCitizenRepositoryImpl(entityManager) {

    /**
     * Finds a citizen by ID and eagerly loads related document storage keys.
     *
     * @param id The UUID of the citizen to search for
     * @return An Optional containing the citizen with document information if found
     */
    fun findByIdWithDocuments(id: UUID): Optional<Citizen> {
        val citizen = getByIdNotDeleted(id)
        return Optional.ofNullable(citizen)
    }
    
    /**
     * Searches for citizens by name with partial matching.
     *
     * @param namePart Part of the citizen name to search for
     * @return List of citizens matching the name pattern
     */
    fun searchByNameContaining(namePart: String): List<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // Create name search predicates (check both English and Devanagari names)
        val predicates = createPredicateList()
        predicates.add(createCaseInsensitiveLikePredicate(root.get("name"), namePart))
        predicates.add(createCaseInsensitiveLikePredicate(root.get("nameDevnagari"), namePart))
        
        query.where(
            cb.and(
                cb.or(*predicates.toTypedArray()),
                createNotDeletedPredicate(root)
            )
        )
        
        return entityManager.createQuery(query).resultList
    }
    
    /**
     * Finds citizens by address with partial matching for both
     * permanent and temporary addresses.
     *
     * @param addressPart Part of the address to search for
     * @return List of citizens matching the address pattern
     */
    fun findByAddressContaining(addressPart: String): List<Citizen> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Citizen::class.java)
        val root = query.from(Citizen::class.java)
        
        // Create address search predicates
        val predicates = createAddressPredicates(root, addressPart)
        
        // Combine all predicates with OR
        query.where(
            cb.and(
                // At least one of the address fields should match
                cb.or(*predicates.toTypedArray()),
                // Citizen should not be deleted
                createNotDeletedPredicate(root)
            )
        )
        
        return entityManager.createQuery(query).resultList
    }
    
    /**
     * Helper method to create address search predicates
     */
    private fun createAddressPredicates(
        root: jakarta.persistence.criteria.Root<Citizen>,
        addressPart: String
    ): List<Predicate> {
        val cb = entityManager.criteriaBuilder
        val addressLike = "%${addressPart.lowercase()}%"
        val predicates = createPredicateList()
        
        // Permanent address fields
        val permanentAddressPath = root.get<Address>("permanentAddress")
        
        // Add street address predicate if not null
        predicates.add(
            cb.and(
                cb.isNotNull(permanentAddressPath.get<String>("streetAddress")),
                cb.like(
                    cb.lower(permanentAddressPath.get<String>("streetAddress")), 
                    addressLike
                )
            )
        )
        
        // Check province codes, district codes, etc.
        predicates.add(
            cb.like(
                cb.lower(permanentAddressPath.get<String>("provinceCode")),
                addressLike
            )
        )
        predicates.add(
            cb.like(
                cb.lower(permanentAddressPath.get<String>("districtCode")),
                addressLike
            )
        )
        predicates.add(
            cb.like(
                cb.lower(permanentAddressPath.get<String>("municipalityCode")),
                addressLike
            )
        )
        
        // Temporary address fields (these might be null, so handle accordingly)
        val tempAddressPath = root.get<Address>("temporaryAddress")
        val tempAddressNotNull = cb.isNotNull(tempAddressPath)
        
        // Add temporary address predicates with null checks
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.isNotNull(tempAddressPath.get<String>("streetAddress")),
                cb.like(cb.lower(tempAddressPath.get<String>("streetAddress")), addressLike)
            )
        )
        
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(
                    cb.lower(tempAddressPath.get<String>("provinceCode")),
                    addressLike
                )
            )
        )
        
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(
                    cb.lower(tempAddressPath.get<String>("districtCode")),
                    addressLike
                )
            )
        )
        
        predicates.add(
            cb.and(
                tempAddressNotNull,
                cb.like(
                    cb.lower(tempAddressPath.get<String>("municipalityCode")),
                    addressLike
                )
            )
        )
        
        return predicates
    }
}
