package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.SkillType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of major skills among the population in each ward.
 *
 * This entity stores data about the number of people with specific skills for economic analysis and
 * reporting purposes. Each record represents the number of people with a specific skill in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_major_skills",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_skill",
            columnNames = ["ward_number", "skill"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_major_skills_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_major_skills_skill",
            columnList = "skill"
        )
    ]
)
@DynamicUpdate
class WardWiseMajorSkills : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Skill type for this population data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "skill", nullable = false, columnDefinition = "skill_type")
    var skill: SkillType? = null

    /**
     * The number of people with the specified skill in the specified ward.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
