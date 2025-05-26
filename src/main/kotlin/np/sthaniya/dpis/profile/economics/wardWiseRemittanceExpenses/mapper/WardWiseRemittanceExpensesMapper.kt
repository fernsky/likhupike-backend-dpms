package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.mapper

import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.CreateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.UpdateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.RemittanceExpenseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardRemittanceSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardWiseRemittanceExpensesResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.model.WardWiseRemittanceExpenses
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.repository.RemittanceExpenseSummary
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.repository.WardRemittanceSummary
import org.springframework.stereotype.Component

/** Mapper for WardWiseRemittanceExpenses entity and DTOs. */
@Component
class WardWiseRemittanceExpensesMapper {

    /**
     * Maps a WardWiseRemittanceExpenses entity to a response DTO.
     *
     * @param entity The entity to map
     * @return The mapped response DTO
     */
    fun toResponse(entity: WardWiseRemittanceExpenses): WardWiseRemittanceExpensesResponse {
        return WardWiseRemittanceExpensesResponse(
                id = entity.id!!,
                wardNumber = entity.wardNumber!!,
                remittanceExpense = entity.remittanceExpense!!,
                households = entity.households,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
        )
    }

    /**
     * Maps a CreateWardWiseRemittanceExpensesDto to a WardWiseRemittanceExpenses entity.
     *
     * @param dto The dto to map
     * @return The mapped entity
     */
    fun toEntity(dto: CreateWardWiseRemittanceExpensesDto): WardWiseRemittanceExpenses {
        return WardWiseRemittanceExpenses().apply {
            wardNumber = dto.wardNumber
            remittanceExpense = dto.remittanceExpense
            households = dto.households
        }
    }

    /**
     * Updates an existing WardWiseRemittanceExpenses entity with data from an
     * UpdateWardWiseRemittanceExpensesDto.
     *
     * @param entity The entity to update
     * @param dto The dto containing update data
     * @return The updated entity
     */
    fun updateEntity(
            entity: WardWiseRemittanceExpenses,
            dto: UpdateWardWiseRemittanceExpensesDto
    ): WardWiseRemittanceExpenses {
        entity.apply {
            wardNumber = dto.wardNumber
            remittanceExpense = dto.remittanceExpense
            households = dto.households
        }
        return entity
    }

    /**
     * Maps RemittanceExpenseSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toRemittanceExpenseSummaryResponse(
            summary: RemittanceExpenseSummary
    ): RemittanceExpenseSummaryResponse {
        return RemittanceExpenseSummaryResponse(
                remittanceExpense = summary.remittanceExpense,
                totalHouseholds = summary.totalHouseholds
        )
    }

    /**
     * Maps WardRemittanceSummary to a response DTO.
     *
     * @param summary The summary to map
     * @return The mapped response DTO
     */
    fun toWardRemittanceSummaryResponse(summary: WardRemittanceSummary): WardRemittanceSummaryResponse {
        return WardRemittanceSummaryResponse(
                wardNumber = summary.wardNumber,
                totalHouseholds = summary.totalHouseholds
        )
    }
}
