package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.mapper

import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.CreateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.UpdateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAbsenteePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAgeGenderWiseAbsenteeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.WardAgeGenderWiseAbsentee
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.repository.AgeGroupGenderPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.repository.WardAbsenteePopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardAgeGenderWiseAbsentee entity and DTOs. */
@Component
class WardAgeGenderWiseAbsenteeMapper {

    /**
     * Maps a WardAgeGenderWiseAbsentee entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardAgeGenderWiseAbsentee): WardAgeGenderWiseAbsenteeResponse {
        return WardAgeGenderWiseAbsenteeResponse(
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
     * Maps a CreateWardAgeGenderWiseAbsenteeDto to a WardAgeGenderWiseAbsentee entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardAgeGenderWiseAbsenteeDto): WardAgeGenderWiseAbsentee {
        return WardAgeGenderWiseAbsentee().apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            gender = dto.gender
            population = dto.population
        }
    }

    /**
     * Updates an existing WardAgeGenderWiseAbsentee entity with data from an
     * UpdateWardAgeGenderWiseAbsenteeDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardAgeGenderWiseAbsentee,
            dto: UpdateWardAgeGenderWiseAbsenteeDto
    ): WardAgeGenderWiseAbsentee {
        entity.apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            gender = dto.gender
            population = dto.population
        }
        return entity
    }

    /**
     * Maps AgeGroupGenderPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toAgeGroupGenderSummaryResponse(
            summary: AgeGroupGenderPopulationSummary
    ): AgeGroupGenderPopulationSummaryResponse {
        return AgeGroupGenderPopulationSummaryResponse(
                ageGroup = summary.ageGroup,
                gender = summary.gender,
                totalPopulation = summary.totalPopulation
        )
    }

    /**
     * Maps WardAbsenteePopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardAbsenteePopulationSummary): WardAbsenteePopulationSummaryResponse {
        return WardAbsenteePopulationSummaryResponse(
                wardNumber = summary.wardNumber,
                totalPopulation = summary.totalPopulation
        )
    }
}
