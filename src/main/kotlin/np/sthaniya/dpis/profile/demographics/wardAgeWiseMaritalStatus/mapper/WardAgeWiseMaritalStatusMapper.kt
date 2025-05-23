package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.mapper

import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.CreateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.UpdateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.AgeGroupSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.MaritalStatusSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.WardAgeWiseMaritalStatusResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.WardWiseMaritalStatus
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.repository.AgeGroupSummary
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.repository.MaritalStatusSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseMaritalStatus entity and DTOs. */
@Component
class WardAgeWiseMaritalStatusMapper {

    /**
     * Maps a WardWiseMaritalStatus entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseMaritalStatus): WardAgeWiseMaritalStatusResponse {
        return WardAgeWiseMaritalStatusResponse(
            id = entity.id!!,
            wardNumber = entity.wardNumber!!,
            ageGroup = entity.ageGroup!!,
            maritalStatus = entity.maritalStatus!!,
            population = entity.population,
            malePopulation = entity.malePopulation,
            femalePopulation = entity.femalePopulation,
            otherPopulation = entity.otherPopulation,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardAgeWiseMaritalStatusDto to a WardWiseMaritalStatus entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardAgeWiseMaritalStatusDto): WardWiseMaritalStatus {
        return WardWiseMaritalStatus().apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            maritalStatus = dto.maritalStatus
            population = dto.population
            malePopulation = dto.malePopulation
            femalePopulation = dto.femalePopulation
            otherPopulation = dto.otherPopulation
        }
    }

    /**
     * Updates an existing WardWiseMaritalStatus entity with data from an
     * UpdateWardAgeWiseMaritalStatusDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
        entity: WardWiseMaritalStatus,
        dto: UpdateWardAgeWiseMaritalStatusDto
    ): WardWiseMaritalStatus {
        entity.apply {
            wardNumber = dto.wardNumber
            ageGroup = dto.ageGroup
            maritalStatus = dto.maritalStatus
            population = dto.population
            malePopulation = dto.malePopulation
            femalePopulation = dto.femalePopulation
            otherPopulation = dto.otherPopulation
        }
        return entity
    }

    /**
     * Maps MaritalStatusSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toMaritalStatusSummaryResponse(
        summary: MaritalStatusSummary
    ): MaritalStatusSummaryResponse {
        return MaritalStatusSummaryResponse(
            maritalStatus = summary.maritalStatus,
            totalPopulation = summary.totalPopulation,
            malePopulation = summary.malePopulation,
            femalePopulation = summary.femalePopulation,
            otherPopulation = summary.otherPopulation
        )
    }

    /**
     * Maps AgeGroupSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toAgeGroupSummaryResponse(
        summary: AgeGroupSummary
    ): AgeGroupSummaryResponse {
        return AgeGroupSummaryResponse(
            ageGroup = summary.ageGroup,
            totalPopulation = summary.totalPopulation,
            malePopulation = summary.malePopulation,
            femalePopulation = summary.femalePopulation,
            otherPopulation = summary.otherPopulation
        )
    }
}
