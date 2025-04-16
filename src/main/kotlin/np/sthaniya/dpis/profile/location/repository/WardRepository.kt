package np.sthaniya.dpis.profile.location.repository

import java.util.UUID
import np.sthaniya.dpis.profile.location.model.ProfileMunicipality
import np.sthaniya.dpis.profile.location.model.Ward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WardRepository : JpaRepository<Ward, UUID> {
    fun findByMunicipality(municipality: ProfileMunicipality): List<Ward>
    fun findByNumberAndMunicipality(number: Int, municipality: ProfileMunicipality): Ward?
}
