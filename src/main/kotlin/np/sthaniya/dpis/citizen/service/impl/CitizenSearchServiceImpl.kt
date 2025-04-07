package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.search.CitizenSearchCriteria
import np.sthaniya.dpis.citizen.exception.CitizenException
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.repository.specification.CitizenSpecifications
import np.sthaniya.dpis.citizen.service.CitizenSearchService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of CitizenSearchService that provides comprehensive 
 * citizen search functionality using specifications and projections.
 */
@Service
@Transactional(readOnly = true)
class CitizenSearchServiceImpl(
    private val citizenRepository: CitizenRepository
) : CitizenSearchService {
    
    /**
     * Search for citizens using detailed search criteria.
     * 
     * This method uses specifications to build dynamic queries and 
     * projections to efficiently return only the requested data fields.
     * 
     * Features:
     * - Dynamic query building based on provided criteria
     * - Field selection for efficient data retrieval
     * - Page-based data access with 1-based page numbering
     * - Efficient handling for complex filtering scenarios
     *
     * @param criteria The search criteria to use
     * @return A paginated result of citizen projections
     * @throws CitizenException.CitizenErrorCode.PAGE_NOT_FOUND if the requested page exceeds available data
     */
    override fun searchCitizens(criteria: CitizenSearchCriteria): Page<CitizenProjection> {
        val specification = CitizenSpecifications.fromCriteria(criteria)
        
        // Use distinct count when address fields are involved
        val totalElements = citizenRepository.countDistinct(specification)
        
        // Calculate total pages to validate page number
        val totalPages = if (totalElements == 0L) 0 else {
            (totalElements + criteria.size - 1) / criteria.size
        }
        
        // Check if the requested page is valid
        if (criteria.page > totalPages && totalPages > 0) {
            throw CitizenException(
                errorCode = CitizenException.CitizenErrorCode.PAGE_NOT_FOUND,
                message = "Page number ${criteria.page} is invalid. Total pages available: $totalPages"
            )
        }
        
        // Execute the search with projections
        return citizenRepository.findAllWithProjection(
            spec = specification,
            pageable = criteria.toPageable(),
            columns = criteria.getValidColumns()
        )
    }
}
