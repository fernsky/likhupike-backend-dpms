package np.sthaniya.dpis.profile.location.repository

import java.util.UUID
import np.sthaniya.dpis.profile.location.model.ProfileMunicipality
import np.sthaniya.dpis.profile.location.model.ProfileWard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WardRepository : JpaRepository<ProfileWard, UUID> {
    fun findByMunicipality(municipality: ProfileMunicipality): List<ProfileWard>
    fun findByNumberAndMunicipality(number: Int, municipality: ProfileMunicipality): ProfileWard?
}
