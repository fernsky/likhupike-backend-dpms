package np.sthaniya.dpis.profile.location.repository

import np.sthaniya.dpis.profile.location.model.Municipality
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MunicipalityRepository : JpaRepository<Municipality, UUID> {
    fun findFirstByOrderByCreatedAtAsc(): Municipality?
}
