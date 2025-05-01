package np.sthaniya.dpis.profile.institutions.cooperatives.service

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeWardStatistics

/** Service for retrieving statistical information about cooperatives. */
interface CooperativeStatisticsService {

    /**
     * Get statistics of cooperatives grouped by type.
     *
     * @return A map of cooperative types to their counts
     */
    fun getStatisticsByType(): CooperativeTypeStatistics

    /**
     * Get statistics of cooperatives grouped by ward.
     *
     * @return A map of ward numbers to cooperative counts
     */
    fun getStatisticsByWard(): CooperativeWardStatistics
}
