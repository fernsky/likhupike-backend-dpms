package np.sthaniya.dpis.profile.institutions.cooperatives.model

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Enumeration of cooperative types.
 */
@Schema(description = "Types of cooperatives")
enum class CooperativeType {
    /** Agricultural cooperative */
    @Schema(description = "Agricultural cooperative") AGRICULTURE,
    
    /** Animal husbandry cooperative */
    @Schema(description = "Animal husbandry cooperative") ANIMAL_HUSBANDRY,
    
    /** Dairy cooperative */
    @Schema(description = "Dairy cooperative") DAIRY,
    
    /** Savings and credit cooperative */
    @Schema(description = "Savings and credit cooperative") SAVINGS_AND_CREDIT,
    
    /** Multipurpose cooperative */
    @Schema(description = "Multipurpose cooperative") MULTIPURPOSE,
    
    /** Consumer cooperative */
    @Schema(description = "Consumer cooperative") CONSUMER,
    
    /** Coffee cooperative */
    @Schema(description = "Coffee cooperative") COFFEE,
    
    /** Tea cooperative */
    @Schema(description = "Tea cooperative") TEA,
    
    /** Handicraft cooperative */
    @Schema(description = "Handicraft cooperative") HANDICRAFT,
    
    /** Fruits and vegetables cooperative */
    @Schema(description = "Fruits and vegetables cooperative") FRUITS_AND_VEGETABLES,
    
    /** Bee keeping cooperative */
    @Schema(description = "Bee keeping cooperative") BEE_KEEPING,
    
    /** Health cooperative */
    @Schema(description = "Health cooperative") HEALTH,
    
    /** Electricity cooperative */
    @Schema(description = "Electricity cooperative") ELECTRICITY,
    
    /** Communication cooperative */
    @Schema(description = "Communication cooperative") COMMUNICATION,
    
    /** Tourism cooperative */
    @Schema(description = "Tourism cooperative") TOURISM,
    
    /** Environment conservation cooperative */
    @Schema(description = "Environment conservation cooperative") ENVIRONMENT_CONSERVATION,
    
    /** Herbs processing cooperative */
    @Schema(description = "Herbs processing cooperative") HERBS_PROCESSING,
    
    /** Sugarcane cooperative */
    @Schema(description = "Sugarcane cooperative") SUGARCANE,
    
    /** Junar processing cooperative */
    @Schema(description = "Junar processing cooperative") JUNAR_PROCESSING,
    
    /** Small farmers cooperative */
    @Schema(description = "Small farmers cooperative") SMALL_FARMERS,
    
    /** Women's cooperative */
    @Schema(description = "Women's cooperative") WOMEN,
    
    /** Transportation cooperative */
    @Schema(description = "Transportation cooperative") TRANSPORTATION,
    
    /** Energy cooperative */
    @Schema(description = "Energy cooperative") ENERGY;
}
