package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response

/**
 * Response DTO for summarized ward remittance data.
 *
 * @property wardNumber The ward number
 * @property totalHouseholds The total number of households for this ward
 */
data class WardRemittanceSummaryResponse(
        val wardNumber: Int,
        val totalHouseholds: Long
)
