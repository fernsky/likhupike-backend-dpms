package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.mapper

import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.CreateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.UpdateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.CastePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardWiseCastePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.WardWiseCastePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.repository.CastePopulationSummary
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseCastePopulation entity and DTOs. */
@Component
class WardWiseCastePopulationMapper {

    /**
     * Maps a WardWiseCastePopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseCastePopulation): WardWiseCastePopulationResponse {
        return WardWiseCastePopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                casteType = entity.casteType!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseCastePopulationDto to a WardWiseCastePopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseCastePopulationDto): WardWiseCastePopulation {
        return WardWiseCastePopulation().apply {
            wardNumber = dto.wardNumber
            casteType = dto.casteType
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseCastePopulation entity with data from an
     * UpdateWardWiseCastePopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseCastePopulation,
            dto: UpdateWardWiseCastePopulationDto
    ): WardWiseCastePopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            casteType = dto.casteType
            population = dto.population
        }
        return entity
    }

    /**
     * Maps CastePopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toCasteSummaryResponse(
            summary: CastePopulationSummary
    ): CastePopulationSummaryResponse {
        return CastePopulationSummaryResponse(
                casteType = summary.casteType,
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
