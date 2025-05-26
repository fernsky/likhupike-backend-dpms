package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request

/**
 * DTO for filtering ward-wise households on loan data.
 *
 * @property wardNumber Optional ward number filter
 */
data class WardWiseHouseholdsOnLoanFilterDto(
        val wardNumber: Int? = null
)
