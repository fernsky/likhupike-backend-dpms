package np.sthaniya.dpis.common.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

/**
 * Abstract base class providing common auditing and versioning functionality for all entities in the system.
 *
 * This class implements JPA's entity listener pattern using Spring's [AuditingEntityListener] to automatically
 * manage audit fields (creation time, last modified time, etc.) for all derived entities.
 *
 * Features:
 * - Automatic timestamp management for creation and modification times
 * - User tracking for create and update operations
 * - Optimistic locking support using version control
 * 
 * All timestamps are stored in UTC using [Instant].
 *
 * Usage:
 * ```kotlin
 * @Entity
 * class MyEntity : BaseEntity() {
 *     // Entity specific fields
 * }
 * ```
 *
 * Note: To enable auditing, the application must be configured with [@EnableJpaAuditing][org.springframework.data.jpa.repository.config.EnableJpaAuditing]
 * and provide an implementation of [AuditorAware][org.springframework.data.domain.AuditorAware].
 *
 * @property createdAt The timestamp when the entity was created (UTC)
 * @property updatedAt The timestamp when the entity was last modified (UTC)
 * @property createdBy The UUID of the user who created the entity
 * @property updatedBy The UUID of the user who last modified the entity
 * @property version Optimistic locking version number
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    /**
     * Timestamp of entity creation.
     * This field is automatically set by Spring Data JPA's auditing feature
     * and cannot be modified after creation.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()

    /**
     * Timestamp of the last modification.
     * This field is automatically updated by Spring Data JPA's auditing feature
     * whenever the entity is modified.
     */
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

    /**
     * UUID of the user who created this entity.
     * This field is automatically set by Spring Data JPA's auditing feature
     * and cannot be modified after creation.
     */
    @CreatedBy
    @Column(updatable = false)
    var createdBy: UUID? = null

    /**
     * UUID of the user who last modified this entity.
     * This field is automatically updated by Spring Data JPA's auditing feature
     * whenever the entity is modified.
     */
    @LastModifiedBy
    @Column
    var updatedBy: UUID? = null

    /**
     * Version number used for optimistic locking.
     * This field is automatically incremented by JPA whenever the entity is updated,
     * helping prevent concurrent modification conflicts.
     */
    @Version
    var version: Long? = null
}
