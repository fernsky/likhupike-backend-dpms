package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Entity representing the distribution of household heads by gender in each ward.
 *
 * This entity stores demographic data about the gender of household heads for statistical analysis and
 * reporting purposes. Each record represents the number of households headed by a specific gender
 * in a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_househead_gender",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_gender",
                                columnNames = ["ward_number", "gender"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_househead_gender_ward",
                                columnList = "ward_number"
                        )]
)
@DynamicUpdate
class WardWiseHouseHeadGender : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Optional ward name for reference purposes.
     */
    @Column(name = "ward_name", length = 100) var wardName: String? = null

    /**
     * Gender of the household head. Defined as an enum to ensure data consistency.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "gender")
    var gender: Gender? = null

    /**
     * The number of households headed by people of the specified gender in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
