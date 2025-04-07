package np.sthaniya.dpis.citizen.repository.specification

import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import np.sthaniya.dpis.citizen.domain.entity.Address
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.dto.search.CitizenSearchCriteria
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID

/**
 * Object providing specification builders for dynamic Citizen entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Address handling
 * - State-based filtering
 * - Date range queries
 * - Document state filtering
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for comprehensive search criteria
 * - Address field searching
 * - Document state filtering
 *
 * Usage:
 * ```
 * val spec = CitizenSpecifications.fromCriteria(criteria)
 * citizenRepository.findAll(spec)
 * ```
 */
object CitizenSpecifications {

    /**
     * Creates a Specification from search criteria.
     *
     * @param criteria The search criteria to convert to specifications
     * @return Combined Specification for the search criteria
     */
    fun fromCriteria(criteria: CitizenSearchCriteria): Specification<Citizen> =
        Specification
            .where(withName(criteria))
            .and(withNameDevnagari(criteria))
            .and(withCitizenshipNumber(criteria))
            .and(withCitizenshipIssuedOffice(criteria))
            .and(withCitizenshipIssuedDateRange(criteria))
            .and(withEmail(criteria))
            .and(withPhoneNumber(criteria))
            .and(withState(criteria))
            .and(withStates(criteria))
            .and(withApprovalStatus(criteria))
            .and(withPhotoState(criteria))
            .and(withCitizenshipFrontState(criteria))
            .and(withCitizenshipBackState(criteria))
            .and(withDocumentStates(criteria))
            .and(withNotesSearch(criteria))
            .and(withAddressSearch(criteria))
            .and(withPermanentAddress(criteria))
            .and(withCreatedDateRange(criteria))
            .and(withStateUpdatedDateRange(criteria))
            .and(withUpdatedByUser(criteria))
            .and(notDeleted())

    /**
     * Specification to match citizens by name.
     */
    private fun withName(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.name?.let { name ->
                cb.like(cb.lower(root.get("name")), "%${name.lowercase()}%")
            }
        }

    /**
     * Specification to match citizens by Devanagari name.
     */
    private fun withNameDevnagari(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.nameDevnagari?.let { name ->
                cb.like(root.get("nameDevnagari"), "%${name}%")
            }
        }

    /**
     * Specification to match citizens by citizenship number.
     */
    private fun withCitizenshipNumber(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.citizenshipNumber?.let { number ->
                cb.equal(root.get<String>("citizenshipNumber"), number)
            }
        }

    /**
     * Specification to match citizens by citizenship issued office.
     */
    private fun withCitizenshipIssuedOffice(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.citizenshipIssuedOffice?.let { office ->
                cb.like(cb.lower(root.get("citizenshipIssuedOffice")), "%${office.lowercase()}%")
            }
        }

    /**
     * Specification to match citizens by citizenship issued date range.
     */
    private fun withCitizenshipIssuedDateRange(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.citizenshipIssuedDateStart?.let { startDate ->
                predicates.add(cb.greaterThanOrEqualTo(root.get("citizenshipIssuedDate"), startDate))
            }

            criteria.citizenshipIssuedDateEnd?.let { endDate ->
                predicates.add(cb.lessThanOrEqualTo(root.get("citizenshipIssuedDate"), endDate))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification to match citizens by exact email address.
     */
    private fun withEmail(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.email?.let { email ->
                cb.equal(cb.lower(root.get("email")), email.lowercase())
            }
        }

    /**
     * Specification to match citizens by phone number (partial match).
     */
    private fun withPhoneNumber(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.phoneNumber?.let { phone ->
                cb.like(root.get("phoneNumber"), "%${phone}%")
            }
        }

    /**
     * Specification to match citizens by state.
     */
    private fun withState(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.state?.let { state ->
                cb.equal(root.get<Any>("state"), state)
            }
        }

    /**
     * Specification to match citizens by multiple states.
     */
    private fun withStates(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.states?.let { states ->
                if (states.isNotEmpty()) {
                    root.get<Any>("state").`in`(states)
                } else null
            }
        }

    /**
     * Specification to match citizens by approval status.
     */
    private fun withApprovalStatus(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.isApproved?.let { isApproved ->
                cb.equal(root.get<Boolean>("isApproved"), isApproved)
            }
        }

    /**
     * Specification to match citizens by photo state.
     */
    private fun withPhotoState(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.photoState?.let { state ->
                cb.equal(root.get<Any>("photoState"), state)
            }
        }

    /**
     * Specification to match citizens by citizenship front state.
     */
    private fun withCitizenshipFrontState(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.citizenshipFrontState?.let { state ->
                cb.equal(root.get<Any>("citizenshipFrontState"), state)
            }
        }

    /**
     * Specification to match citizens by citizenship back state.
     */
    private fun withCitizenshipBackState(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.citizenshipBackState?.let { state ->
                cb.equal(root.get<Any>("citizenshipBackState"), state)
            }
        }

    /**
     * Specification to match citizens by any document state.
     */
    private fun withDocumentStates(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.documentStates?.let { states ->
                if (states.isNotEmpty()) {
                    val predicates = mutableListOf<Predicate>()
                    predicates.add(root.get<Any>("photoState").`in`(states))
                    predicates.add(root.get<Any>("citizenshipFrontState").`in`(states))
                    predicates.add(root.get<Any>("citizenshipBackState").`in`(states))
                    cb.or(*predicates.toTypedArray())
                } else null
            }
        }

    /**
     * Specification to match citizens by notes field content.
     */
    private fun withNotesSearch(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.notesSearch?.let { search ->
                val noteLike = "%${search.lowercase()}%"
                val predicates = mutableListOf<Predicate>()
                
                // State note search
                predicates.add(
                    cb.and(
                        cb.isNotNull(root.get<String>("stateNote")),
                        cb.like(cb.lower(root.get<String>("stateNote")), noteLike)
                    )
                )
                
                // Photo state note search
                predicates.add(
                    cb.and(
                        cb.isNotNull(root.get<String>("photoStateNote")),
                        cb.like(cb.lower(root.get<String>("photoStateNote")), noteLike)
                    )
                )
                
                // Citizenship front state note search
                predicates.add(
                    cb.and(
                        cb.isNotNull(root.get<String>("citizenshipFrontStateNote")),
                        cb.like(cb.lower(root.get<String>("citizenshipFrontStateNote")), noteLike)
                    )
                )
                
                // Citizenship back state note search
                predicates.add(
                    cb.and(
                        cb.isNotNull(root.get<String>("citizenshipBackStateNote")),
                        cb.like(cb.lower(root.get<String>("citizenshipBackStateNote")), noteLike)
                    )
                )
                
                cb.or(*predicates.toTypedArray())
            }
        }

    /**
     * Specification to match citizens by address field content.
     */
    private fun withAddressSearch(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.addressTerm?.let { term ->
                val addressLike = "%${term.lowercase()}%"
                val predicates = mutableListOf<Predicate>()
                
                // Permanent address fields
                val permanentAddressPath = root.get<Address>("permanentAddress")
                
                // Search in street address if not null
                predicates.add(
                    cb.and(
                        cb.isNotNull(permanentAddressPath.get<String>("streetAddress")),
                        cb.like(cb.lower(permanentAddressPath.get<String>("streetAddress")), addressLike)
                    )
                )
                
                // Search in address code fields
                predicates.add(cb.like(cb.lower(permanentAddressPath.get<String>("provinceCode")), addressLike))
                predicates.add(cb.like(cb.lower(permanentAddressPath.get<String>("districtCode")), addressLike))
                predicates.add(cb.like(cb.lower(permanentAddressPath.get<String>("municipalityCode")), addressLike))
                predicates.add(cb.like(cb.lower(permanentAddressPath.get<String>("wardMunicipalityCode")), addressLike))
                
                // Temporary address search
                val tempAddressPath = root.get<Address>("temporaryAddress")
                val tempAddressExists = cb.isNotNull(tempAddressPath)
                
                // Search in street address if not null
                predicates.add(
                    cb.and(
                        tempAddressExists,
                        cb.isNotNull(tempAddressPath.get<String>("streetAddress")),
                        cb.like(cb.lower(tempAddressPath.get<String>("streetAddress")), addressLike)
                    )
                )
                
                // Search in address code fields
                predicates.add(cb.and(
                    tempAddressExists,
                    cb.like(cb.lower(tempAddressPath.get<String>("provinceCode")), addressLike)
                ))
                predicates.add(cb.and(
                    tempAddressExists,
                    cb.like(cb.lower(tempAddressPath.get<String>("districtCode")), addressLike)
                ))
                predicates.add(cb.and(
                    tempAddressExists,
                    cb.like(cb.lower(tempAddressPath.get<String>("municipalityCode")), addressLike)
                ))
                predicates.add(cb.and(
                    tempAddressExists,
                    cb.like(cb.lower(tempAddressPath.get<String>("wardMunicipalityCode")), addressLike)
                ))
                
                cb.or(*predicates.toTypedArray())
            }
        }

    /**
     * Specification to match citizens by permanent address details.
     */
    private fun withPermanentAddress(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            val permanentAddressPath = root.get<Address>("permanentAddress")
            
            criteria.permanentProvinceCode?.let { code ->
                predicates.add(cb.equal(permanentAddressPath.get<String>("provinceCode"), code))
            }
            
            criteria.permanentDistrictCode?.let { code ->
                predicates.add(cb.equal(permanentAddressPath.get<String>("districtCode"), code))
            }
            
            criteria.permanentMunicipalityCode?.let { code ->
                predicates.add(cb.equal(permanentAddressPath.get<String>("municipalityCode"), code))
            }
            
            criteria.permanentWardNumber?.let { number ->
                predicates.add(cb.equal(permanentAddressPath.get<Int>("wardNumber"), number))
            }
            
            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification to match citizens by creation date range.
     */
    private fun withCreatedDateRange(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.createdAfter?.let { date ->
                val startInstant = date.atStartOfDay().toInstant(ZoneOffset.UTC)
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startInstant))
            }

            criteria.createdBefore?.let { date ->
                val endInstant = date.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC)
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endInstant))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification to match citizens by state updated date range.
     */
    private fun withStateUpdatedDateRange(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.stateUpdatedAfter?.let { date ->
                val startInstant = date.atStartOfDay().toInstant(ZoneOffset.UTC)
                predicates.add(cb.greaterThanOrEqualTo(root.get("stateUpdatedAt"), startInstant))
            }

            criteria.stateUpdatedBefore?.let { date ->
                val endInstant = date.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC)
                predicates.add(cb.lessThanOrEqualTo(root.get("stateUpdatedAt"), endInstant))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    /**
     * Specification to match citizens by updated by user ID.
     */
    private fun withUpdatedByUser(criteria: CitizenSearchCriteria) =
        Specification<Citizen> { root, _, cb ->
            criteria.updatedByUserId?.let { userId ->
                try {
                    val uuid = UUID.fromString(userId)
                    cb.equal(root.get<UUID>("stateUpdatedBy"), uuid)
                } catch (e: IllegalArgumentException) {
                    // Return always-false predicate for invalid UUID
                    cb.disjunction()
                }
            }
        }

    /**
     * Default specification to exclude deleted citizens.
     */
    private fun notDeleted() =
        Specification<Citizen> { root, _, cb ->
            cb.equal(root.get<Boolean>("isDeleted"), false)
        }
}
