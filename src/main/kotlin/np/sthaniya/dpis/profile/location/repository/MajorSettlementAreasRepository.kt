package np.sthaniya.dpis.profile.location.repository

import java.util.UUID
import np.sthaniya.dpis.profile.location.model.MajorSettlementAreas
import np.sthaniya.dpis.profile.location.model.ProfileWard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MajorSettlementAreasRepository : JpaRepository<MajorSettlementAreas, UUID> {
    fun findByWard(ward: ProfileWard): List<MajorSettlementAreas>
    fun findByNameContainingIgnoreCase(name: String): List<MajorSettlementAreas>
}
