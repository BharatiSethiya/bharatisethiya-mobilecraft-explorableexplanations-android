package com.bharatisethiya.explorableexplanations.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ParkScenarioCalculatorTest {
    @Test
    fun defaultsMatchOriginalWebsiteState() {
        val result = ParkScenarioCalculator.calculate(ParkInputs())
        val explicitOriginal = ParkScenarioCalculator.calculate(
            ParkInputs(tax = 18f, admission = 0f, admissionForEveryone = false),
        )
        assertEquals(explicitOriginal.budgetMillions, result.budgetMillions, 0.1f)
        assertEquals(explicitOriginal.visitsMillions, result.visitsMillions, 0.1f)
    }

    @Test
    fun zeroChargeAndOriginalAdmissionReturnBaselineBudget() {
        val result = ParkScenarioCalculator.calculate(
            ParkInputs(tax = 0f, admission = 12f, admissionForEveryone = true),
        )
        assertEquals(400f, result.budgetMillions, 0.1f)
        assertEquals(75f, result.visitsMillions, 0.1f)
    }
}
