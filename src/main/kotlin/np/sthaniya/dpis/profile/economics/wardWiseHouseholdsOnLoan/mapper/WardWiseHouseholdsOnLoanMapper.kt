package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.mapper

import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.CreateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.UpdateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.model.WardWiseHouseholdsOnLoan
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.repository.HouseholdsOnLoanSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseHouseholdsOnLoan entity and DTOs. */
@Component
class WardWiseHouseholdsOnLoanMapper {

    /**
     * Maps a WardWiseHouseholdsOnLoan entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseHouseholdsOnLoan): WardWiseHouseholdsOnLoanResponse {
        return WardWiseHouseholdsOnLoanResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                households = entity.households,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseHouseholdsOnLoanDto to a WardWiseHouseholdsOnLoan entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseHouseholdsOnLoanDto): WardWiseHouseholdsOnLoan {
        return WardWiseHouseholdsOnLoan().apply {
            wardNumber = dto.wardNumber
            households = dto.households
        }
    }

    /**
     * Updates an existing WardWiseHouseholdsOnLoan entity with data from an UpdateWardWiseHouseholdsOnLoanDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseHouseholdsOnLoan,
            dto: UpdateWardWiseHouseholdsOnLoanDto
    ): WardWiseHouseholdsOnLoan {
        entity.apply {
            wardNumber = dto.wardNumber
            households = dto.households
        }
        return entity
    }

    /**
     * Maps HouseholdsOnLoanSummary to a summary response DTO.
     *
     * @param summary The summary to map
     * @return The summary response DTO
     */
    fun toSummaryResponse(summary: HouseholdsOnLoanSummary): WardWiseHouseholdsOnLoanSummaryResponse {
        return WardWiseHouseholdsOnLoanSummaryResponse(
                totalHouseholdsOnLoan = summary.totalHouseholdsOnLoan
        )
    }
}
