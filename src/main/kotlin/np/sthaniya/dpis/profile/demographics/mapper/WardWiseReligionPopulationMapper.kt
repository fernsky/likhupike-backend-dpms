package np.sthaniya.dpis.profile.demographics.mapper

import np.sthaniya.dpis.profile.demographics.dto.request.CreateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.UpdateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.response.ReligionPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardWiseReligionPopulationResponse
import np.sthaniya.dpis.profile.demographics.model.WardWiseReligionPopulation
import np.sthaniya.dpis.profile.demographics.repository.ReligionPopulationSummary
import np.sthaniya.dpis.profile.demographics.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseReligionPopulation entity and DTOs. */
@Component
class WardWiseReligionPopulationMapper {

    /**
     * Maps a WardWiseReligionPopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseReligionPopulation): WardWiseReligionPopulationResponse {
        return WardWiseReligionPopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                religionType = entity.religionType!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseReligionPopulationDto to a WardWiseReligionPopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseReligionPopulationDto): WardWiseReligionPopulation {
        return WardWiseReligionPopulation().apply {
            wardNumber = dto.wardNumber
            religionType = dto.religionType
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseReligionPopulation entity with data from an
     * UpdateWardWiseReligionPopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseReligionPopulation,
            dto: UpdateWardWiseReligionPopulationDto
    ): WardWiseReligionPopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            religionType = dto.religionType
            population = dto.population
        }
        return entity
    }

    /**
     * Maps ReligionPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toReligionSummaryResponse(
            summary: ReligionPopulationSummary
    ): ReligionPopulationSummaryResponse {
        return ReligionPopulationSummaryResponse(
                religionType = summary.religionType,
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
