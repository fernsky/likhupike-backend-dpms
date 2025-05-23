package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.mapper

import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.CreateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.UpdateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardAgeGenderWiseMarriedAgeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.WardAgeGenderWiseMarriedAge
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.repository.AgeGroupGenderPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardAgeGenderWiseMarriedAge entity and DTOs. */
@Component
class WardAgeGenderWiseMarriedAgeMapper {

    /**
     * Maps a WardAgeGenderWiseMarriedAge entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardAgeGenderWiseMarriedAge): WardAgeGenderWiseMarriedAgeResponse {
        return WardAgeGenderWiseMarriedAgeResponse(
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
     * Maps a CreateWardAgeGenderWiseMarriedAgeDto to a WardAgeGenderWiseMarriedAge entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardAgeGenderWiseMarriedAgeDto): WardAgeGenderWiseMarriedAge {
        return WardAgeGenderWiseMarriedAge().apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            gender = dto.gender
            population = dto.population
        }
    }

    /**
     * Updates an existing WardAgeGenderWiseMarriedAge entity with data from an
     * UpdateWardAgeGenderWiseMarriedAgeDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardAgeGenderWiseMarriedAge,
            dto: UpdateWardAgeGenderWiseMarriedAgeDto
    ): WardAgeGenderWiseMarriedAge {
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
     * Maps WardPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardPopulationSummary): WardPopulationSummaryResponse {
        return WardPopulationSummaryResponse(
                wardNumber = summary.wardNumber,
                totalPopulation = summary.totalPopulation
        )
    }
}
