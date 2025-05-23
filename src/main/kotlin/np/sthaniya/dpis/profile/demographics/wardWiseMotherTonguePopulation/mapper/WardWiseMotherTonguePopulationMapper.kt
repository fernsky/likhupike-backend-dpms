package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.mapper

import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.CreateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.UpdateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.LanguagePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardWiseMotherTonguePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.WardWiseMotherTonguePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.repository.LanguagePopulationSummary
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseMotherTonguePopulation entity and DTOs. */
@Component
class WardWiseMotherTonguePopulationMapper {

    /**
     * Maps a WardWiseMotherTonguePopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseMotherTonguePopulation): WardWiseMotherTonguePopulationResponse {
        return WardWiseMotherTonguePopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                languageType = entity.languageType!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseMotherTonguePopulationDto to a WardWiseMotherTonguePopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseMotherTonguePopulationDto): WardWiseMotherTonguePopulation {
        return WardWiseMotherTonguePopulation().apply {
            wardNumber = dto.wardNumber
            languageType = dto.languageType
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseMotherTonguePopulation entity with data from an
     * UpdateWardWiseMotherTonguePopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseMotherTonguePopulation,
            dto: UpdateWardWiseMotherTonguePopulationDto
    ): WardWiseMotherTonguePopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            languageType = dto.languageType
            population = dto.population
        }
        return entity
    }

    /**
     * Maps LanguagePopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toLanguageSummaryResponse(
            summary: LanguagePopulationSummary
    ): LanguagePopulationSummaryResponse {
        return LanguagePopulationSummaryResponse(
                languageType = summary.languageType,
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
