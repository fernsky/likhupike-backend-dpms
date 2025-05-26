package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType

/**
 * Response DTO for ward-wise households loan use data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property loanUse The type of loan use
 * @property households The number of households
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseHouseholdsLoanUseResponse(
        val id: UUID,
        val wardNumber: Int,
        val loanUse: LoanUseType,
        val households: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
