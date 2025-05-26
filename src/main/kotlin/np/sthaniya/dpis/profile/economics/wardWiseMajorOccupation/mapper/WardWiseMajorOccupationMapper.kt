package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.mapper

import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.CreateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.UpdateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.OccupationPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardOccupationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardWiseMajorOccupationResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.model.WardWiseMajorOccupation
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.repository.OccupationPopulationSummary
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.repository.WardOccupationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseMajorOccupation entity and DTOs. */
@Component
class WardWiseMajorOccupationMapper {

    /**
     * Maps a WardWiseMajorOccupation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseMajorOccupation): WardWiseMajorOccupationResponse {
        return WardWiseMajorOccupationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                occupation = entity.occupation!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseMajorOccupationDto to a WardWiseMajorOccupation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseMajorOccupationDto): WardWiseMajorOccupation {
        return WardWiseMajorOccupation().apply {
            wardNumber = dto.wardNumber
            occupation = dto.occupation
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseMajorOccupation entity with data from an
     * UpdateWardWiseMajorOccupationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseMajorOccupation,
            dto: UpdateWardWiseMajorOccupationDto
    ): WardWiseMajorOccupation {
        entity.apply {
            wardNumber = dto.wardNumber
            occupation = dto.occupation
            population = dto.population
        }
        return entity
    }

    /**
     * Maps OccupationPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toOccupationSummaryResponse(
            summary: OccupationPopulationSummary
    ): OccupationPopulationSummaryResponse {
        return OccupationPopulationSummaryResponse(
                occupation = summary.occupation,
                totalPopulation = summary.totalPopulation
        )
    }

    /**
     * Maps WardOccupationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardOccupationSummary): WardOccupationSummaryResponse {
        return WardOccupationSummaryResponse(
                wardNumber = summary.wardNumber,
                totalPopulation = summary.totalPopulation
        )
    }
}
