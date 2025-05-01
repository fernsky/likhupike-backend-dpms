package np.sthaniya.dpis.profile.institutions.cooperatives.service.impl

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import np.sthaniya.dpis.profile.institutions.cooperatives.exception.CooperativeException
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepository
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.specification.CooperativeSpecifications
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeSearchService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Implementation of the CooperativeSearchService interface. */
@Service
class CooperativeSearchServiceImpl(
        private val cooperativeRepository: CooperativeRepository,
) : CooperativeSearchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Searches for cooperatives based on specified criteria. */
    @Transactional(readOnly = true)
    override fun searchCooperatives(
            criteria: CooperativeSearchCriteria
    ): Page<CooperativeProjection> {
        logger.debug("Searching cooperatives with criteria: $criteria")

        try {
            // Special case for geographic proximity search
            if (criteria.longitude != null &&
                            criteria.latitude != null &&
                            criteria.radiusInMeters != null
            ) {
                return handleGeoSearch(criteria)
            }

            // Build specification from criteria
            val spec = CooperativeSpecifications.fromCriteria(criteria)

            // Get valid columns
            val columns = criteria.getValidColumns()

            // Use custom repository method to get projections
            return cooperativeRepository.findAllWithProjection(
                    spec,
                    criteria.toPageable(),
                    columns,
                    criteria.includeTranslations,
                    criteria.includePrimaryMedia
            )
        } catch (e: Exception) {
            logger.error("Error searching cooperatives", e)
            throw CooperativeException.ProximitySearchFailedException(
                    "Failed to execute cooperative search",
                    e
            )
        }
    }

    /** Handles geographic proximity search. */
    private fun handleGeoSearch(criteria: CooperativeSearchCriteria): Page<CooperativeProjection> {
        logger.debug("Executing geographic proximity search")

        // Find cooperatives within the specified distance
        val results =
                cooperativeRepository.findWithinDistance(
                        criteria.longitude!!,
                        criteria.latitude!!,
                        criteria.radiusInMeters!!,
                        criteria.toPageable()
                )

        // Map to projections
        val projections =
                results.content.map { cooperative ->
                    val data = mutableMapOf<String, Any?>()

                    // Add selected fields
                    criteria.getValidColumns().forEach { column ->
                        when (column) {
                            "id" -> data["id"] = cooperative.id
                            "code" -> data["code"] = cooperative.code
                            "defaultLocale" -> data["defaultLocale"] = cooperative.defaultLocale
                            "establishedDate" ->
                                    data["establishedDate"] = cooperative.establishedDate
                            "ward" -> data["ward"] = cooperative.ward
                            "type" -> data["type"] = cooperative.type
                            "status" -> data["status"] = cooperative.status
                        // Add more fields as needed
                        }
                    }

                    CooperativeProjection(data)
                }

        return PageImpl(projections, criteria.toPageable(), results.totalElements)
    }
}
