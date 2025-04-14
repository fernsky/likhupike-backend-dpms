package np.sthaniya.dpis.statistics.domain.model.economics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.StatisticalComparison
import np.sthaniya.dpis.statistics.domain.event.EconomicStatsUpdatedEvent
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ward-level economic statistics aggregating employment and income data.
 */
class WardWiseEconomicStatistics : WardStatistics() {
    
    /**
     * Economically active population in the ward
     */
    var economicallyActivePopulation: Int = 0
    
    /**
     * Employment rate as a percentage
     */
    var employmentRate: BigDecimal = BigDecimal.ZERO
    
    /**
     * Average monthly income per household
     */
    var averageMonthlyIncome: BigDecimal = BigDecimal.ZERO
    
    /**
     * Average monthly expenditure per household
     */
    var averageMonthlyExpenditure: BigDecimal = BigDecimal.ZERO
    
    /**
     * Number of households with loans
     */
    var householdsWithLoans: Int = 0
    
    /**
     * Percentage of households with loans
     */
    var loanPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Map of income sources to percentage of households
     */
    var incomeSources: MutableMap<String, BigDecimal> = mutableMapOf()
    
    /**
     * Map of occupations to percentage of working population
     */
    var occupationDistribution: MutableMap<String, BigDecimal> = mutableMapOf()
    
    /**
     * Map of loan uses to percentage of households with loans
     */
    var loanUseDistribution: MutableMap<String, BigDecimal> = mutableMapOf()
    
    init {
        this.statisticalGroup = "economics"
        this.statisticalCategory = "ward_economy"
    }
    
    /**
     * Calculate loan percentage
     */
    fun calculateLoanPercentage(totalHouseholds: Int) {
        if (totalHouseholds > 0) {
            loanPercentage = BigDecimal(householdsWithLoans * 100)
                .divide(BigDecimal(totalHouseholds), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Calculate savings rate based on income and expenditure
     */
    fun calculateSavingsRate(): BigDecimal {
        return if (averageMonthlyIncome > BigDecimal.ZERO) {
            BigDecimal(100).subtract(
                averageMonthlyExpenditure
                    .multiply(BigDecimal(100))
                    .divide(averageMonthlyIncome, 2, RoundingMode.HALF_UP)
            )
        } else BigDecimal.ZERO
    }
    
    /**
     * Get income to expenditure ratio
     */
    fun getIncomeExpenditureRatio(): BigDecimal {
        return if (averageMonthlyExpenditure > BigDecimal.ZERO) {
            averageMonthlyIncome.divide(averageMonthlyExpenditure, 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Compare with another ward's economic statistics
     */
    fun compareWith(other: WardWiseEconomicStatistics): Map<String, StatisticalComparison> {
        return mapOf(
            "employmentRate" to StatisticalComparison.create(
                this.employmentRate, other.employmentRate, true
            ),
            "averageMonthlyIncome" to StatisticalComparison.create(
                this.averageMonthlyIncome, other.averageMonthlyIncome, true
            ),
            "loanPercentage" to StatisticalComparison.create(
                this.loanPercentage, other.loanPercentage, false
            )
        )
    }
    
    override fun getComparisonValue(): Double {
        // Use average monthly income as the standard comparison metric
        return averageMonthlyIncome.toDouble()
    }
    
    override fun getTotalWardPopulation(): Int {
        // This should be implemented to access the demographic repository
        // For now, return a placeholder
        return applicablePopulation ?: 0
    }
    
    override fun getKeyMetrics(): Map<String, Any> {
        return mapOf(
            "economicallyActivePopulation" to economicallyActivePopulation,
            "employmentRate" to employmentRate,
            "averageMonthlyIncome" to averageMonthlyIncome,
            "averageMonthlyExpenditure" to averageMonthlyExpenditure,
            "householdsWithLoans" to householdsWithLoans,
            "loanPercentage" to loanPercentage,
            "savingsRate" to calculateSavingsRate(),
            "incomeExpenditureRatio" to getIncomeExpenditureRatio()
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            EconomicStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                economicallyActivePopulation = this.economicallyActivePopulation,
                employmentRate = this.employmentRate.toDouble(),
                averageIncome = this.averageMonthlyIncome.toDouble(),
                householdsWithLoans = this.householdsWithLoans,
                calculationDate = this.calculationDate
            )
        )
    }
}
