
package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import org.springframework.data.domain.Page

/**
 * Service for searching cooperatives with advanced filtering.
 */
interface CooperativeSearchService {

    /**
     * Searches for cooperatives based on specified criteria.
     *
     * @param criteria The search criteria to filter cooperatives
     * @return A paginated list of [CooperativeProjection] matching the criteria
     */
    fun searchCooperatives(criteria: CooperativeSearchCriteria): Page<CooperativeProjection>
}
