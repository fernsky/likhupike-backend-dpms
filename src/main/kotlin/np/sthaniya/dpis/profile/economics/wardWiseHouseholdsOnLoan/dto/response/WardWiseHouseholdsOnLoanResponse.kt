package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response

import java.time.Instant
import java.util.UUID

/**
 * Response DTO for ward-wise households on loan data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property households The number of households with loans
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseHouseholdsOnLoanResponse(
        val id: UUID,
        val wardNumber: Int,
        val households: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
