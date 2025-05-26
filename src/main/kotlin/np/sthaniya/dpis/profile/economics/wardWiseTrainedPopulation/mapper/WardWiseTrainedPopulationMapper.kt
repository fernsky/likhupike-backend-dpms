package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.mapper

import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.CreateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.UpdateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.TrainedPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardWiseTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.model.WardWiseTrainedPopulation
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.repository.WardTrainedPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseTrainedPopulation entity and DTOs. */
@Component
class WardWiseTrainedPopulationMapper {

    /**
     * Maps a WardWiseTrainedPopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseTrainedPopulation): WardWiseTrainedPopulationResponse {
        return WardWiseTrainedPopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                trainedPopulation = entity.trainedPopulation,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseTrainedPopulationDto to a WardWiseTrainedPopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseTrainedPopulationDto): WardWiseTrainedPopulation {
        return WardWiseTrainedPopulation().apply {
            wardNumber = dto.wardNumber
            trainedPopulation = dto.trainedPopulation
        }
    }

    /**
     * Updates an existing WardWiseTrainedPopulation entity with data from an
     * UpdateWardWiseTrainedPopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseTrainedPopulation,
            dto: UpdateWardWiseTrainedPopulationDto
    ): WardWiseTrainedPopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            trainedPopulation = dto.trainedPopulation
        }
        return entity
    }

    /**
     * Maps ward trained population summary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardTrainedPopulationSummary): WardTrainedPopulationResponse {
        return WardTrainedPopulationResponse(
                wardNumber = summary.wardNumber,
                trainedPopulation = summary.trainedPopulation
        )
    }

    /**
     * Creates a total trained population summary response.
     *
     * @param totalTrainedPopulation The total trained population
     * @return The summary response DTO
     */
    fun toTotalSummaryResponse(totalTrainedPopulation: Long): TrainedPopulationSummaryResponse {
        return TrainedPopulationSummaryResponse(
                totalTrainedPopulation = totalTrainedPopulation
        )
    }
}
