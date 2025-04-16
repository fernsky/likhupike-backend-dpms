package np.sthaniya.dpis.profile.location.repository

import np.sthaniya.dpis.profile.location.model.Municipality
import np.sthaniya.dpis.profile.location.model.Ward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WardRepository : JpaRepository<Ward, UUID> {
    fun findByMunicipality(municipality: Municipality): List<Ward>
    fun findByNumberAndMunicipality(number: Int, municipality: Municipality): Ward?
}
