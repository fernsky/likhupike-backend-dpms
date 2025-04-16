package np.sthaniya.dpis.profile.location.repository

import java.util.UUID
import np.sthaniya.dpis.profile.location.model.ProfileMunicipality
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MunicipalityRepository : JpaRepository<ProfileMunicipality, UUID> {
    fun findFirstByOrderByCreatedAtAsc(): ProfileMunicipality?
}
