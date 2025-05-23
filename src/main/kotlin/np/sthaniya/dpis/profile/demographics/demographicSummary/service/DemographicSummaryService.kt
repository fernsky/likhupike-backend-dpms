package np.sthaniya.dpis.profile.demographics.demographicSummary.service

import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse

/**
 * Service interface for managing demographic summary data.
 */
interface DemographicSummaryService {

    /**
     * Get the demographic summary.
     *
     * @return The demographic summary response
     * @throws DemographicDataNotFoundException if no demographic summary exists
     */
    fun getDemographicSummary(): DemographicSummaryResponse

    /**
     * Update the demographic summary with new data.
     * If no demographic summary exists, a new one will be created.
     *
     * @param updateDto DTO containing the update data
     * @return The updated demographic summary response
     */
    fun updateDemographicSummary(updateDto: UpdateDemographicSummaryDto): DemographicSummaryResponse

    /**
     * Update a single field in the demographic summary.
     * If no demographic summary exists, a new one will be created with just this field.
     *
     * @param field The name of the field to update
     * @param value The new value for the field
     * @return The updated demographic summary response
     */
    fun updateSingleField(field: String, value: Any?): DemographicSummaryResponse
}
