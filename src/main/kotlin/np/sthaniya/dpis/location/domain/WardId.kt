package np.sthaniya.dpis.location.domain

import java.io.Serializable

/**
 * Composite primary key class for Ward entity.
 * Contains wardNumber and municipality code that together form the primary key.
 */
class WardId : Serializable {
    var wardNumber: Int? = null
    var municipality: String? = null

    // Default constructor required by JPA
    constructor()

    constructor(wardNumber: Int?, municipalityCode: String?) {
        this.wardNumber = wardNumber
        this.municipality = municipalityCode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WardId) return false

        if (wardNumber != other.wardNumber) return false
        if (municipality != other.municipality) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wardNumber ?: 0
        result = 31 * result + (municipality?.hashCode() ?: 0)
        return result
    }
}
