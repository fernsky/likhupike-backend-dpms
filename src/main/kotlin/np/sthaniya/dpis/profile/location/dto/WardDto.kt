package np.sthaniya.dpis.profile.location.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.util.UUID

data class WardResponse(
    val id: UUID?,
    val number: Int,
    val area: BigDecimal,
    val formingLocalBodies: Array<String>,
    val formingConstituentWards: Array<String>,
    val settlementCount: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WardResponse

        if (id != other.id) return false
        if (number != other.number) return false
        if (area != other.area) return false
        if (!formingLocalBodies.contentEquals(other.formingLocalBodies)) return false
        if (!formingConstituentWards.contentEquals(other.formingConstituentWards)) return false
        if (settlementCount != other.settlementCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + number
        result = 31 * result + area.hashCode()
        result = 31 * result + formingLocalBodies.contentHashCode()
        result = 31 * result + formingConstituentWards.contentHashCode()
        result = 31 * result + settlementCount
        return result
    }
}

data class WardCreateRequest(
    @field:NotNull(message = "Ward number is required")
    @field:Min(value = 1, message = "Ward number must be at least 1")
    val number: Int,
    
    @field:NotNull(message = "Area is required")
    @field:PositiveOrZero(message = "Area must be positive or zero")
    val area: BigDecimal,
    
    val formingLocalBodies: Array<String> = arrayOf(),
    
    val formingConstituentWards: Array<String> = arrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WardCreateRequest

        if (number != other.number) return false
        if (area != other.area) return false
        if (!formingLocalBodies.contentEquals(other.formingLocalBodies)) return false
        if (!formingConstituentWards.contentEquals(other.formingConstituentWards)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + area.hashCode()
        result = 31 * result + formingLocalBodies.contentHashCode()
        result = 31 * result + formingConstituentWards.contentHashCode()
        return result
    }
}

data class WardUpdateRequest(
    @field:NotNull(message = "Area is required")
    @field:PositiveOrZero(message = "Area must be positive or zero")
    val area: BigDecimal,
    
    val formingLocalBodies: Array<String> = arrayOf(),
    
    val formingConstituentWards: Array<String> = arrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WardUpdateRequest

        if (area != other.area) return false
        if (!formingLocalBodies.contentEquals(other.formingLocalBodies)) return false
        if (!formingConstituentWards.contentEquals(other.formingConstituentWards)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = area.hashCode()
        result = 31 * result + formingLocalBodies.contentHashCode()
        result = 31 * result + formingConstituentWards.contentHashCode()
        return result
    }
}
