package np.likhupikemun.dpis.common.entity

import jakarta.persistence.*
import java.util.*

@MappedSuperclass
abstract class UuidBaseEntity : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UuidBaseEntity) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: javaClass.hashCode()
}
