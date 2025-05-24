package np.sthaniya.dpis.profile.economics.exportedProducts.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing exported products from the municipality.
 *
 * This entity stores data about products that are exported from the municipality for economic
 * analysis and reporting purposes. Each record represents a specific exported product.
 */
@Entity
@Table(
    name = "exported_products",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_exported_product_name",
            columnNames = ["product_name"]
        )
    ]
)
@DynamicUpdate
class ExportedProduct : UuidBaseEntity() {

    /**
     * Name of the exported product.
     */
    @Column(name = "product_name", nullable = false) var productName: String? = null
}
