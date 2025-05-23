package np.sthaniya.dpis.profile.demographics.demographicSummary.mapper

import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.demographicSummary.model.DemographicSummary
import org.springframework.stereotype.Component
import java.math.BigDecimal

/**
 * Mapper for DemographicSummary entity and DTOs.
 */
@Component
class DemographicSummaryMapper {

    /**
     * Maps a DemographicSummary entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: DemographicSummary): DemographicSummaryResponse {
        return DemographicSummaryResponse(
            id = entity.id!!,
            totalPopulation = entity.totalPopulation,
            populationMale = entity.populationMale,
            populationFemale = entity.populationFemale,
            populationOther = entity.populationOther,
            populationAbsenteeTotal = entity.populationAbsenteeTotal,
            populationMaleAbsentee = entity.populationMaleAbsentee,
            populationFemaleAbsentee = entity.populationFemaleAbsentee,
            populationOtherAbsentee = entity.populationOtherAbsentee,
            sexRatio = entity.sexRatio,
            totalHouseholds = entity.totalHouseholds,
            averageHouseholdSize = entity.averageHouseholdSize,
            populationDensity = entity.populationDensity,
            population0To14 = entity.population0To14,
            population15To59 = entity.population15To59,
            population60AndAbove = entity.population60AndAbove,
            growthRate = entity.growthRate,
            literacyRateAbove15 = entity.literacyRateAbove15,
            literacyRate15To24 = entity.literacyRate15To24,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * Updates an existing DemographicSummary entity with data from an UpdateDemographicSummaryDto.
     * Only updates fields that are non-null in the DTO.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
        entity: DemographicSummary,
        dto: UpdateDemographicSummaryDto
    ): DemographicSummary {
        entity.apply {
            dto.totalPopulation?.let { totalPopulation = it }
            dto.populationMale?.let { populationMale = it }
            dto.populationFemale?.let { populationFemale = it }
            dto.populationOther?.let { populationOther = it }
            dto.populationAbsenteeTotal?.let { populationAbsenteeTotal = it }
            dto.populationMaleAbsentee?.let { populationMaleAbsentee = it }
            dto.populationFemaleAbsentee?.let { populationFemaleAbsentee = it }
            dto.populationOtherAbsentee?.let { populationOtherAbsentee = it }
            dto.sexRatio?.let { sexRatio = it }
            dto.totalHouseholds?.let { totalHouseholds = it }
            dto.averageHouseholdSize?.let { averageHouseholdSize = it }
            dto.populationDensity?.let { populationDensity = it }
            dto.population0To14?.let { population0To14 = it }
            dto.population15To59?.let { population15To59 = it }
            dto.population60AndAbove?.let { population60AndAbove = it }
            dto.growthRate?.let { growthRate = it }
            dto.literacyRateAbove15?.let { literacyRateAbove15 = it }
            dto.literacyRate15To24?.let { literacyRate15To24 = it }
        }
        return entity
    }

    /**
     * Updates a single field in a DemographicSummary entity.
     *
     * @param entity The entity to update
     * @param field The field name to update
     * @param value The new value for the field
     * @return The updated entity
     */
    fun updateSingleField(
        entity: DemographicSummary,
        field: String,
        value: Any?
    ): DemographicSummary {
        entity.apply {
            when (field) {
                "totalPopulation" -> totalPopulation = value as? Int
                "populationMale" -> populationMale = value as? Int
                "populationFemale" -> populationFemale = value as? Int
                "populationOther" -> populationOther = value as? Int
                "populationAbsenteeTotal" -> populationAbsenteeTotal = value as? Int
                "populationMaleAbsentee" -> populationMaleAbsentee = value as? Int
                "populationFemaleAbsentee" -> populationFemaleAbsentee = value as? Int
                "populationOtherAbsentee" -> populationOtherAbsentee = value as? Int
                "sexRatio" -> sexRatio = parseAsBigDecimal(value)
                "totalHouseholds" -> totalHouseholds = value as? Int
                "averageHouseholdSize" -> averageHouseholdSize = parseAsBigDecimal(value)
                "populationDensity" -> populationDensity = parseAsBigDecimal(value)
                "population0To14" -> population0To14 = value as? Int
                "population15To59" -> population15To59 = value as? Int
                "population60AndAbove" -> population60AndAbove = value as? Int
                "growthRate" -> growthRate = parseAsBigDecimal(value)
                "literacyRateAbove15" -> literacyRateAbove15 = parseAsBigDecimal(value)
                "literacyRate15To24" -> literacyRate15To24 = parseAsBigDecimal(value)
            }
        }
        return entity
    }

    /**
     * Helper method to parse a value to BigDecimal.
     */
    private fun parseAsBigDecimal(value: Any?): BigDecimal? {
        return when(value) {
            is BigDecimal -> value
            is Double -> BigDecimal.valueOf(value)
            is Int -> BigDecimal(value)
            is String -> try { BigDecimal(value) } catch(e: Exception) { null }
            else -> null
        }
    }
}
