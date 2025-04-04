package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.Province
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import java.math.BigDecimal
import java.util.*

interface CustomProvinceRepository {
    @EntityGraph(
        attributePaths = [
            "districts",
            "districts.municipalities",
        ],
    )
    fun findByCodeIgnoreCase(code: String): Optional<Province>

    fun findLargeProvinces(
        minArea: BigDecimal,
        minPopulation: Long,
        pageable: Pageable,
    ): Page<Province>

    fun existsByCode(code: String): Boolean
}
