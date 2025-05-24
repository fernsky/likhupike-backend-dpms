package np.sthaniya.dpis.profile.economics.importedProducts.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing imported products to the municipality.
 *
 * This entity stores data about products that are imported to the municipality for economic
 * analysis and reporting purposes. Each record represents a specific imported product.
 */
@Entity
@Table(
    name = "imported_products",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_imported_product_name",
            columnNames = ["product_name"]
        )
    ]
)
@DynamicUpdate
class ImportedProduct : UuidBaseEntity() {

    /**
     * Name of the imported product.
     */
    @Column(name = "product_name", nullable = false) var productName: String? = null
}
