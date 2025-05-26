package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.mapper

import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.CreateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.UpdateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.SkillPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardSkillsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardWiseMajorSkillsResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.model.WardWiseMajorSkills
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.repository.SkillPopulationSummary
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.repository.WardSkillsSummary
import org.springframework.stereotype.Component

/**
 * Mapper for WardWiseMajorSkills entity and DTOs.
 */
@Component
class WardWiseMajorSkillsMapper {

    /**
     * Maps a WardWiseMajorSkills entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseMajorSkills): WardWiseMajorSkillsResponse {
        return WardWiseMajorSkillsResponse(
            id = entity.id!!,
            wardNumber = entity.wardNumber!!,
            skill = entity.skill!!,
            population = entity.population,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseMajorSkillsDto to a WardWiseMajorSkills entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseMajorSkillsDto): WardWiseMajorSkills {
        return WardWiseMajorSkills().apply {
            wardNumber = dto.wardNumber
            skill = dto.skill
            population = dto.population
        }
    }

    /**
     * Updates an existing WardWiseMajorSkills entity with data from an
     * UpdateWardWiseMajorSkillsDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
        entity: WardWiseMajorSkills,
        dto: UpdateWardWiseMajorSkillsDto
    ): WardWiseMajorSkills {
        entity.apply {
            wardNumber = dto.wardNumber
            skill = dto.skill
            population = dto.population
        }
        return entity
    }

    /**
     * Maps SkillPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toSkillSummaryResponse(
        summary: SkillPopulationSummary
    ): SkillPopulationSummaryResponse {
        return SkillPopulationSummaryResponse(
            skill = summary.skill,
            totalPopulation = summary.totalPopulation
        )
    }

    /**
     * Maps WardSkillsSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardSkillsSummary): WardSkillsSummaryResponse {
        return WardSkillsSummaryResponse(
            wardNumber = summary.wardNumber,
            totalPopulation = summary.totalPopulation
        )
    }
}
