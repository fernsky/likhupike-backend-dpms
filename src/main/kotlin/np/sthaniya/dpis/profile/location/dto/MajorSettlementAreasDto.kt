package np.sthaniya.dpis.profile.location.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class MajorSettlementAreasResponse(
    val id: UUID?,
    val name: String,
    val wardId: UUID?,
    val wardNumber: Int
)

data class MajorSettlementAreasCreateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotNull(message = "Ward ID is required")
    val wardId: UUID
)

data class MajorSettlementAreasUpdateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String
)
