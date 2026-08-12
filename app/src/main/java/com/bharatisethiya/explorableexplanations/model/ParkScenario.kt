package com.bharatisethiya.explorableexplanations.model

import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.roundToInt

data class ParkInputs(
    val tax: Float = 18f,
    val compliancePercent: Float = 100f,
    val taxPerVehicle: Boolean = true,
    val admission: Float = 0f,
    val admissionForEveryone: Boolean = false,
)

data class ParkOutcome(
    val budgetMillions: Float,
    val budgetDeltaMillions: Float,
    val taxCollectedMillions: Float,
    val deltaRevenueMillions: Float,
    val deltaBudgetPositive: Boolean,
    val deltaRevenuePositive: Boolean,
    val visitsMillions: Float,
    val visitDeltaPercent: Float,
    val visitDeltaPercentSigned: Float,
    val isRisingVisitors: Boolean,
    val summary: String,
    val detailedSummary: String,
    val attendanceSentence: String,
    val scenarioIndex: Int,
    val closedParkCount: Int,
    val restorationTime: Int,
    val surplusMillions: Float,
)

object ParkScenarioCalculator {
    private const val OLD_ADMISSION = 12f
    private const val OLD_VISITS = 75_000_000f
    private const val OLD_BUDGET = 400_000_000f

    fun calculate(input: ParkInputs): ParkOutcome {
        val taxCount = if (input.taxPerVehicle) 28_000_000f else 13_657_632f
        val taxCollected = input.tax * input.compliancePercent / 100f * taxCount
        val eligibleFraction = if (input.admissionForEveryone) 1f else {
            0.85f * if (input.taxPerVehicle) 0.95f else 1f
        }
        val averageAdmission = OLD_ADMISSION + eligibleFraction * (input.admission - OLD_ADMISSION)
        val visits = OLD_VISITS * maxOf(0.2f, 1f + 0.5f * atan(1f - averageAdmission / OLD_ADMISSION))
        val oldRevenue = OLD_VISITS * OLD_ADMISSION * 0.1f
        val newRevenue = visits * averageAdmission * 0.1f
        val deltaRevenue = newRevenue - oldRevenue
        val delta = taxCollected + deltaRevenue
        val budget = OLD_BUDGET + delta

        val deltaBudgetPositive = delta >= 0f
        val deltaRevenuePositive = deltaRevenue >= 0f
        val isRising = visits >= OLD_VISITS
        val signedPercent = (visits - OLD_VISITS) / OLD_VISITS * 100f

        var closedCount = 0
        var restoration = 0
        var surplus = 0f

        val scenario = when {
            budget < 600_000_000f -> {
                closedCount = (150f * (600_000_000f - budget) / 200_000_000f).roundToInt()
                0
            }
            budget < 750_000_000f -> 1
            budget < 1_000_000_000f -> {
                restoration = (10f - 9f * (budget - 750_000_000f) / 250_000_000f).roundToInt()
                2
            }
            else -> {
                surplus = (budget - 1_000_000_000f) / 1_000_000f
                3
            }
        }

        val scenarioText = when (scenario) {
            0 -> "This is not sufficient to maintain the parks, and $closedCount parks would be shut down at least part-time."
            1 -> "This is sufficient to maintain the parks in their current state, but not fund a program to bring safety and cleanliness up to acceptable standards."
            2 -> "This is sufficient to maintain the parks in their current state, plus fund a program to bring safety and cleanliness up to acceptable standards over the next $restoration years."
            else -> "This is sufficient to maintain the parks and bring safety and cleanliness up to acceptable standards, leaving a $${"%.1f".format(surplus)} million per year surplus."
        }

        // Original phrasing fidelity for short summary (still contains keywords for tests)
        val summary = when (scenario) {
            0 -> "Funding remains insufficient; $closedCount parks would be shut down at least part-time."
            1 -> "The parks can be maintained, but restoration is not yet funded."
            2 -> "Maintenance and restoration are funded over about $restoration years."
            else -> "Maintenance and restoration are funded with a ${"%.1f".format(surplus)}M annual surplus."
        }

        val budgetVerb = if (deltaBudgetPositive) "collect an extra" else "lose"
        val revenueJoin = if (deltaRevenuePositive) "plus" else "minus"
        val revenueType = if (deltaRevenuePositive) "additional" else "lost"
        val detailed = "This would ${budgetVerb} $${"%.1f".format(abs(delta) / 1_000_000f)}M " +
            "(${"%.1f".format(taxCollected / 1_000_000f)}M from the tax, $revenueJoin $${"%.1f".format(abs(deltaRevenue) / 1_000_000f)}M $revenueType revenue from admission) " +
            "for a total budget of $${"%.1f".format(budget / 1_000_000f)}M. $scenarioText"

        val attendanceVerb = if (isRising) "rise" else "fall"
        val attendanceSentence = "Park attendance would $attendanceVerb by ${"%.1f".format(abs(signedPercent))}%, to ${"%.1f".format(visits / 1_000_000f)}M visits each year."

        return ParkOutcome(
            budgetMillions = budget / 1_000_000f,
            budgetDeltaMillions = delta / 1_000_000f,
            taxCollectedMillions = taxCollected / 1_000_000f,
            deltaRevenueMillions = deltaRevenue / 1_000_000f,
            deltaBudgetPositive = deltaBudgetPositive,
            deltaRevenuePositive = deltaRevenuePositive,
            visitsMillions = visits / 1_000_000f,
            visitDeltaPercent = abs((visits - OLD_VISITS) / OLD_VISITS * 100f),
            visitDeltaPercentSigned = signedPercent,
            isRisingVisitors = isRising,
            summary = summary,
            detailedSummary = detailed,
            attendanceSentence = attendanceSentence,
            scenarioIndex = scenario,
            closedParkCount = closedCount,
            restorationTime = restoration,
            surplusMillions = surplus,
        )
    }
}
