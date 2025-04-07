package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.search.CitizenSearchCriteria
import org.springframework.data.domain.Page

/**
 * Service interface for searching citizens with complex criteria.
 */
interface CitizenSearchService {
    
    /**
     * Search for citizens using detailed search criteria.
     *
     * @param criteria The search criteria to use
     * @return A paginated result of citizen projections
     */
    fun searchCitizens(criteria: CitizenSearchCriteria): Page<CitizenProjection>
}
