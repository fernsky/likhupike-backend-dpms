package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.mapper

import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.CreateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.UpdateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.AgeGroupPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeGenderSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeWisePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.WardAgeWisePopulation
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository.AgeGroupPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository.GenderPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository.WardAgeGenderSummary
import org.springframework.stereotype.Component

/** Mapper for WardAgeWisePopulation entity and DTOs. */
@Component
class WardAgeWisePopulationMapper {

    /**
     * Maps a WardAgeWisePopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardAgeWisePopulation): WardAgeWisePopulationResponse {
        return WardAgeWisePopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                ageGroup = entity.ageGroup!!,
                gender = entity.gender!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardAgeWisePopulationDto to a WardAgeWisePopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardAgeWisePopulationDto): WardAgeWisePopulation {
        return WardAgeWisePopulation().apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            gender = dto.gender
            population = dto.population
        }
    }

    /**
     * Updates an existing WardAgeWisePopulation entity with data from an
     * UpdateWardAgeWisePopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardAgeWisePopulation,
            dto: UpdateWardAgeWisePopulationDto
    ): WardAgeWisePopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            gender = dto.gender
            population = dto.population
        }
        return entity
    }

    /**
     * Maps AgeGroupPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toAgeGroupSummaryResponse(
            summary: AgeGroupPopulationSummary
    ): AgeGroupPopulationSummaryResponse {
        return AgeGroupPopulationSummaryResponse(
                ageGroup = summary.ageGroup,
                totalPopulation = summary.totalPopulation
        )
    }

    /**
     * Maps GenderPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toGenderSummaryResponse(
            summary: GenderPopulationSummary
    ): GenderPopulationSummaryResponse {
        return GenderPopulationSummaryResponse(
                gender = summary.gender,
                totalPopulation = summary.totalPopulation
        )
    }

    /**
     * Maps WardAgeGenderSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardAgeGenderSummaryResponse(
            summary: WardAgeGenderSummary
    ): WardAgeGenderSummaryResponse {
        return WardAgeGenderSummaryResponse(
                wardNumber = summary.wardNumber,
                ageGroup = summary.ageGroup,
                gender = summary.gender,
                totalPopulation = summary.totalPopulation
        )
    }
}
