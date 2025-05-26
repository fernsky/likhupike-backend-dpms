package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.mapper

import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.CreateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.UpdateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.LoanUseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardHouseholdsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardWiseHouseholdsLoanUseResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.model.WardWiseHouseholdsLoanUse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.repository.LoanUseSummary
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.repository.WardHouseholdsSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseHouseholdsLoanUse entity and DTOs. */
@Component
class WardWiseHouseholdsLoanUseMapper {

    /**
     * Maps a WardWiseHouseholdsLoanUse entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseHouseholdsLoanUse): WardWiseHouseholdsLoanUseResponse {
        return WardWiseHouseholdsLoanUseResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                loanUse = entity.loanUse!!,
                households = entity.households,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseHouseholdsLoanUseDto to a WardWiseHouseholdsLoanUse entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseHouseholdsLoanUseDto): WardWiseHouseholdsLoanUse {
        return WardWiseHouseholdsLoanUse().apply {
            wardNumber = dto.wardNumber
            loanUse = dto.loanUse
            households = dto.households
        }
    }

    /**
     * Updates an existing WardWiseHouseholdsLoanUse entity with data from an
     * UpdateWardWiseHouseholdsLoanUseDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseHouseholdsLoanUse,
            dto: UpdateWardWiseHouseholdsLoanUseDto
    ): WardWiseHouseholdsLoanUse {
        entity.apply {
            wardNumber = dto.wardNumber
            loanUse = dto.loanUse
            households = dto.households
        }
        return entity
    }

    /**
     * Maps LoanUseSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toLoanUseSummaryResponse(
            summary: LoanUseSummary
    ): LoanUseSummaryResponse {
        return LoanUseSummaryResponse(
                loanUse = summary.loanUse,
                totalHouseholds = summary.totalHouseholds
        )
    }

    /**
     * Maps WardHouseholdsSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardSummaryResponse(summary: WardHouseholdsSummary): WardHouseholdsSummaryResponse {
        return WardHouseholdsSummaryResponse(
                wardNumber = summary.wardNumber,
                totalHouseholds = summary.totalHouseholds
        )
    }
}
