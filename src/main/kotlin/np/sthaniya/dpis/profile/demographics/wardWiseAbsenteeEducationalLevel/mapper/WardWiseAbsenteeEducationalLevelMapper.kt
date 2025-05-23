package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.mapper

import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.CreateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.UpdateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.EducationalLevelPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardWiseAbsenteeEducationalLevelResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.model.WardWiseAbsenteeEducationalLevel
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.repository.EducationalLevelPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseAbsenteeEducationalLevel entity and DTOs. */
@Component
class WardWiseAbsenteeEducationalLevelMapper {

    /**
     * Maps a WardWiseAbsenteeEducationalLevel entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseAbsenteeEducationalLevel): WardWiseAbsenteeEducationalLevelResponse {
        return WardWiseAbsenteeEducationalLevelResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                educationalLevel = entity.educationalLevel!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseAbsenteeEducationalLevelDto to a WardWiseAbsenteeEducationalLevel entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseAbsenteeEducationalLevelDto): WardWiseAbsenteeEducationalLevel {
        return WardWiseAbsenteeEducationalLevel().apply {
            wardNumber = dto.wardNumber
            educationalLevel = dto.educationalLevel
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseAbsenteeEducationalLevel entity with data from an
     * UpdateWardWiseAbsenteeEducationalLevelDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseAbsenteeEducationalLevel,
            dto: UpdateWardWiseAbsenteeEducationalLevelDto
    ): WardWiseAbsenteeEducationalLevel {
        entity.apply {
            wardNumber = dto.wardNumber
            educationalLevel = dto.educationalLevel
            population = dto.population
        }
        return entity
    }

    /**
     * Maps EducationalLevelPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toEducationalLevelSummaryResponse(
            summary: EducationalLevelPopulationSummary
    ): EducationalLevelPopulationSummaryResponse {
        return EducationalLevelPopulationSummaryResponse(
                educationalLevel = summary.educationalLevel,
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
