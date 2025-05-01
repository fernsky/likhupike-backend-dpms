package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeWardStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeStatisticsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of the CooperativeStatisticsService interface. */
@Service
class CooperativeStatisticsServiceImpl(private val cooperativeRepository: CooperativeRepository) :
        CooperativeStatisticsService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Get statistics of cooperatives grouped by type. */
    @Transactional(readOnly = true)
    override fun getStatisticsByType(): CooperativeTypeStatistics {
        logger.debug("Retrieving cooperative statistics by type")

        val typeCountResults = cooperativeRepository.countByType()
        val statisticsMap = typeCountResults.associate { it.type to it.count }

        logger.debug("Found statistics for ${statisticsMap.size} cooperative types")
        return CooperativeTypeStatistics(statisticsMap)
    }

    /** Get statistics of cooperatives grouped by ward. */
    @Transactional(readOnly = true)
    override fun getStatisticsByWard(): CooperativeWardStatistics {
        logger.debug("Retrieving cooperative statistics by ward")

        val wardCountResults = cooperativeRepository.countByWard()
        val statisticsMap = wardCountResults.associate { it.ward to it.count }

        logger.debug("Found statistics for ${statisticsMap.size} wards")
        return CooperativeWardStatistics(statisticsMap)
    }
}
