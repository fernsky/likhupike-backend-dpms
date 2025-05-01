package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType

/** DTO for cooperative statistics by type. Maps cooperative types to their counts. */
data class CooperativeTypeStatistics(val statistics: Map<CooperativeType, Long>)

/** DTO for cooperative statistics by ward. Maps ward numbers to cooperative counts. */
data class CooperativeWardStatistics(val statistics: Map<Int, Long>)
