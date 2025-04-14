package np.sthaniya.dpis.statistics.infrastructure.persistence.entity

import jakarta.persistence.*
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import org.hibernate.annotations.Type
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA entity for persisting ward-level absentee statistics.
 * 
 * This entity maps domain objects to database tables and includes additional
 * fields needed for efficient querying and persistence.
 */
@Entity
@Table(name = "ward_wise_absentee_statistics")
class WardWiseAbsenteeStatisticsEntity : Serializable {
    
    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID()
    
    @Column(name = "ward_id", nullable = false)
    var wardId: UUID? = null
    
    @Column(name = "ward_number")
    var wardNumber: Int? = null
    
    @Column(name = "municipality_id")
    var municipalityId: UUID? = null
    
    @Column(name = "district_id")
    var districtId: UUID? = null
    
    @Column(name = "statistical_group", nullable = false)
    var statisticalGroup: String = ""
    
    @Column(name = "statistical_category", nullable = false)
    var statisticalCategory: String = ""
    
    @Column(name = "reference_date")
    var referenceDate: LocalDateTime? = null
    
    @Column(name = "calculation_date", nullable = false)
    var calculationDate: LocalDateTime = LocalDateTime.now()
    
    @Column(name = "calculated_by")
    var calculatedBy: UUID? = null
    
    @Column(name = "methodology_version")
    var methodologyVersion: String = "1.0"
    
    @Column(name = "is_estimate")
    var isEstimate: Boolean = false
    
    @Column(name = "confidence_score")
    var confidenceScore: Int? = null
    
    @Column(name = "data_source")
    var dataSource: String? = null
    
    @Column(name = "is_valid")
    var isValid: Boolean = true
    
    @Column(name = "validation_notes")
    var validationNotes: String? = null
    
    @Column(name = "last_cached_at")
    var lastCachedAt: LocalDateTime? = null
    
    @Column(name = "cache_expiration_policy")
    var cacheExpirationPolicy: String = "TIME_BASED"
    
    @Column(name = "cache_ttl_seconds")
    var cacheTTLSeconds: Long = 3600
    
    @Column(name = "is_complete_ward")
    var isCompleteWard: Boolean = true
    
    @Column(name = "subset_description")
    var subsetDescription: String? = null
    
    @Column(name = "applicable_population")
    var applicablePopulation: Int? = null
    
    @Column(name = "applicable_households")
    var applicableHouseholds: Int? = null
    
    @Column(name = "is_comparable")
    var isComparable: Boolean = true
    
    @Column(name = "total_absentee_population")
    var totalAbsenteePopulation: Int = 0
    
    @Column(name = "absentee_percentage", precision = 5, scale = 2)
    var absenteePercentage: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "foreign_absentee_percentage", precision = 5, scale = 2)
    var foreignAbsenteePercentage: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "primary_absence_reason")
    @Enumerated(EnumType.STRING)
    var primaryAbsenceReason: AbsenceReason? = null
    
    @Column(name = "top_destination_country")
    var topDestinationCountry: String? = null
    
    @Column(name = "average_absentee_age", precision = 5, scale = 1)
    var averageAbsenteeAge: BigDecimal? = null
    
    @Column(name = "absentee_sex_ratio", precision = 7, scale = 2)
    var absenteeSexRatio: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "male_absentee_count")
    var maleAbsenteeCount: Int = 0
    
    @Column(name = "female_absentee_count")
    var femaleAbsenteeCount: Int = 0
    
    @Column(name = "other_absentee_count")
    var otherAbsenteeCount: Int = 0
    
    @Column(name = "absentee_literacy_rate", precision = 5, scale = 2)
    var absenteeLiteracyRate: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "absentee_higher_education_rate", precision = 5, scale = 2)
    var absenteeHigherEducationRate: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
    
    @Column(name = "created_by")
    var createdBy: UUID? = null
    
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
    
    @Column(name = "updated_by")
    var updatedBy: UUID? = null
    
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
    
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
    
    @Column(name = "deleted_by")
    var deletedBy: UUID? = null
    
    @Column(name = "version")
    @Version
    var version: Long = 0
    
    // Store JSON data for complex structures
    @Column(name = "age_gender_distribution", columnDefinition = "jsonb")
    var ageGenderDistribution: String = "{}"
    
    @Column(name = "reason_distribution", columnDefinition = "jsonb")
    var reasonDistribution: String = "{}"
    
    @Column(name = "location_distribution", columnDefinition = "jsonb")
    var locationDistribution: String = "{}"
    
    @Column(name = "education_level_distribution", columnDefinition = "jsonb")
    var educationLevelDistribution: String = "{}"
    
    @Column(name = "destination_country_distribution", columnDefinition = "jsonb")
    var destinationCountryDistribution: String = "{}"
}
