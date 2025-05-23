package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.mapper

import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.CreateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.UpdateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardTimeSeriesPopulationResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.YearPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.model.WardTimeSeriesPopulation
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.repository.YearPopulationSummary
import org.springframework.stereotype.Component

/** Mapper for WardTimeSeriesPopulation entity and DTOs. */
@Component
class WardTimeSeriesPopulationMapper {

    /**
     * Maps a WardTimeSeriesPopulation entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardTimeSeriesPopulation): WardTimeSeriesPopulationResponse {
        return WardTimeSeriesPopulationResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                wardName = entity.wardName,
                year = entity.year!!,
                totalPopulation = entity.totalPopulation,
                malePopulation = entity.malePopulation,
                femalePopulation = entity.femalePopulation,
                otherPopulation = entity.otherPopulation,
                totalHouseholds = entity.totalHouseholds,
                averageHouseholdSize = entity.averageHouseholdSize,
                population0To14 = entity.population0To14,
                population15To59 = entity.population15To59,
                population60AndAbove = entity.population60AndAbove,
                literacyRate = entity.literacyRate,
                maleLiteracyRate = entity.maleLiteracyRate,
                femaleLiteracyRate = entity.femaleLiteracyRate,
                growthRate = entity.growthRate,
                areaSqKm = entity.areaSqKm,
                populationDensity = entity.populationDensity,
                sexRatio = entity.sexRatio,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardTimeSeriesPopulationDto to a WardTimeSeriesPopulation entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardTimeSeriesPopulationDto): WardTimeSeriesPopulation {
        return WardTimeSeriesPopulation().apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            year = dto.year
            totalPopulation = dto.totalPopulation
            malePopulation = dto.malePopulation
            femalePopulation = dto.femalePopulation
            otherPopulation = dto.otherPopulation
            totalHouseholds = dto.totalHouseholds
            averageHouseholdSize = dto.averageHouseholdSize
            population0To14 = dto.population0To14
            population15To59 = dto.population15To59
            population60AndAbove = dto.population60AndAbove
            literacyRate = dto.literacyRate
            maleLiteracyRate = dto.maleLiteracyRate
            femaleLiteracyRate = dto.femaleLiteracyRate
            growthRate = dto.growthRate
            areaSqKm = dto.areaSqKm
            populationDensity = dto.populationDensity
            sexRatio = dto.sexRatio
        }
    }

    /**
     * Updates an existing WardTimeSeriesPopulation entity with data from an
     * UpdateWardTimeSeriesPopulationDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardTimeSeriesPopulation,
            dto: UpdateWardTimeSeriesPopulationDto
    ): WardTimeSeriesPopulation {
        entity.apply {
            wardNumber = dto.wardNumber
            wardName = dto.wardName
            year = dto.year
            totalPopulation = dto.totalPopulation
            malePopulation = dto.malePopulation
            femalePopulation = dto.femalePopulation
            otherPopulation = dto.otherPopulation
            totalHouseholds = dto.totalHouseholds
            averageHouseholdSize = dto.averageHouseholdSize
            population0To14 = dto.population0To14
            population15To59 = dto.population15To59
            population60AndAbove = dto.population60AndAbove
            literacyRate = dto.literacyRate
            maleLiteracyRate = dto.maleLiteracyRate
            femaleLiteracyRate = dto.femaleLiteracyRate
            growthRate = dto.growthRate
            areaSqKm = dto.areaSqKm
            populationDensity = dto.populationDensity
            sexRatio = dto.sexRatio
        }
        return entity
    }

    /**
     * Maps WardTimeSeriesPopulation to a summary response DTO.
     *
     * @param entity The entity to map
     * @return The mapped summary response DTO
     */
    fun toWardSummaryResponse(entity: WardTimeSeriesPopulation): WardPopulationSummaryResponse {
        return WardPopulationSummaryResponse(
                wardNumber = entity.wardNumber!!,
                wardName = entity.wardName,
                totalPopulation = entity.totalPopulation,
                year = entity.year!!
        )
    }

    /**
     * Maps YearPopulationSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toYearSummaryResponse(summary: YearPopulationSummary): YearPopulationSummaryResponse {
        return YearPopulationSummaryResponse(
                year = summary.year,
                totalPopulation = summary.totalPopulation
        )
    }
}
