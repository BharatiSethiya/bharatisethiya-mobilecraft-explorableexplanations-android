package com.bharatisethiya.explorableexplanations.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParkScenarioCalculatorTest {
    @Test
    fun originalDefaultsProduceFundedScenario() {
        val result = ParkScenarioCalculator.calculate(ParkInputs())
        assertTrue(result.budgetMillions > 750f)
        assertTrue(result.visitsMillions > 75f)
        assertTrue(result.summary.contains("restoration"))
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
