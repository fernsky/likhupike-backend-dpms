package np.sthaniya.dpis.common.entity

import jakarta.persistence.*
import java.util.*

/**
 * Abstract base class that extends [BaseEntity] to provide UUID-based primary key functionality.
 *
 * This class serves as the foundation for all entities that require UUID-based identification in the system.
 * It implements proper equals and hashCode methods following JPA best practices for entity identity comparison.
 *
 * Features:
 * - Automatically generated UUID primary key
 * - Proper entity equality comparison
 * - Inherits all auditing features from [BaseEntity]
 *
 * Usage:
 * ```kotlin
 * @Entity
 * class MyEntity : UuidBaseEntity() {
 *     // Entity specific fields
 * }
 * ```
 *
 * Note: The equals and hashCode implementations follow the JPA specification guidelines for entity identity,
 * comparing only the UUID and ensuring proper behavior even when IDs are not yet assigned (e.g., for new entities).
 *
 * @property id The UUID primary key of the entity, automatically generated on persistence
 */
@MappedSuperclass
abstract class UuidBaseEntity : BaseEntity() {
    /**
     * The unique identifier for the entity.
     * Generated automatically by JPA using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    /**
     * Implements equals method following JPA best practices.
     * Two entities are considered equal if:
     * 1. They are the same instance
     * 2. They are of the same type and have the same non-null ID
     *
     * Note: Entities with null IDs are only equal to themselves (same instance).
     *
     * @param other The object to compare with
     * @return true if the entities are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UuidBaseEntity) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    /**
     * Implements hashCode consistent with the equals method.
     * Uses the entity's ID if available, otherwise falls back to the class's hash code.
     *
     * @return hash code value for this entity
     */
    override fun hashCode(): Int = id?.hashCode() ?: javaClass.hashCode()
}
