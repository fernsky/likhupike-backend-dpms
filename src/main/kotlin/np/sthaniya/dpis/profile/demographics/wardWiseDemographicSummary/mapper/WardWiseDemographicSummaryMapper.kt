package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.mapper

import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.CreateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.UpdateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response.WardWiseDemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.model.WardWiseDemographicSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseDemographicSummary entity and DTOs. */
@Component
class WardWiseDemographicSummaryMapper {

    /**
     * Maps a WardWiseDemographicSummary entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseDemographicSummary): WardWiseDemographicSummaryResponse {
        return WardWiseDemographicSummaryResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                wardName = entity.wardName,
                totalPopulation = entity.totalPopulation,
                populationMale = entity.populationMale,
                populationFemale = entity.populationFemale,
                populationOther = entity.populationOther,
                totalHouseholds = entity.totalHouseholds,
                averageHouseholdSize = entity.averageHouseholdSize,
                sexRatio = entity.sexRatio,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseDemographicSummaryDto to a WardWiseDemographicSummary entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseDemographicSummaryDto): WardWiseDemographicSummary {
        return WardWiseDemographicSummary().apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            totalPopulation = dto.totalPopulation
            populationMale = dto.populationMale
            populationFemale = dto.populationFemale
            populationOther = dto.populationOther
            totalHouseholds = dto.totalHouseholds
            averageHouseholdSize = dto.averageHouseholdSize
            sexRatio = dto.sexRatio
        }
    }

    /**
     * Updates an existing WardWiseDemographicSummary entity with data from an
     * UpdateWardWiseDemographicSummaryDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseDemographicSummary,
            dto: UpdateWardWiseDemographicSummaryDto
    ): WardWiseDemographicSummary {
        entity.apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            totalPopulation = dto.totalPopulation
            populationMale = dto.populationMale
            populationFemale = dto.populationFemale
            populationOther = dto.populationOther
            totalHouseholds = dto.totalHouseholds
            averageHouseholdSize = dto.averageHouseholdSize
            sexRatio = dto.sexRatio
        }
        return entity
    }
}
