package np.sthaniya.dpis.profile.location.repository

import np.sthaniya.dpis.profile.location.model.MajorSettlementAreas
import np.sthaniya.dpis.profile.location.model.Ward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MajorSettlementAreasRepository : JpaRepository<MajorSettlementAreas, UUID> {
    fun findByWard(ward: Ward): List<MajorSettlementAreas>
    fun findByNameContainingIgnoreCase(name: String): List<MajorSettlementAreas>
}
