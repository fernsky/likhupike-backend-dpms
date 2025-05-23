package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Response DTO for ward-wise house head gender data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property wardName Optional ward name
 * @property gender The gender of household heads
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseHouseHeadGenderResponse(
        val id: UUID,
        val wardNumber: Int,
        val wardName: String?,
        val gender: Gender,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
