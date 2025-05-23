package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.mapper

import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.CreateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.UpdateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardWiseHouseHeadGenderResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.model.WardWiseHouseHeadGender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.repository.GenderPopulationSummary
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.repository.WardPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseHouseHeadGender entity and DTOs. */
@Component
class WardWiseHouseHeadGenderMapper {

    /**
     * Maps a WardWiseHouseHeadGender entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseHouseHeadGender): WardWiseHouseHeadGenderResponse {
        return WardWiseHouseHeadGenderResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                wardName = entity.wardName,
                gender = entity.gender!!,
                population = entity.population,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseHouseHeadGenderDto to a WardWiseHouseHeadGender entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseHouseHeadGenderDto): WardWiseHouseHeadGender {
        return WardWiseHouseHeadGender().apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            gender = dto.gender
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseHouseHeadGender entity with data from an
     * UpdateWardWiseHouseHeadGenderDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseHouseHeadGender,
            dto: UpdateWardWiseHouseHeadGenderDto
    ): WardWiseHouseHeadGender {
        entity.apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            gender = dto.gender
            population = dto.population
        }
        return entity
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
     * Maps WardPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardPopulationSummary): WardPopulationSummaryResponse {
        return WardPopulationSummaryResponse(
                wardNumber = summary.wardNumber,
                wardName = summary.wardName,
                totalPopulation = summary.totalPopulation
        )
    }
}
