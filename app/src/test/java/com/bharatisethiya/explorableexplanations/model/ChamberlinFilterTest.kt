package com.bharatisethiya.explorableexplanations.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChamberlinFilterTest {
    @Test
    fun defaultOriginalExampleIsStableAndFinite() {
        val result = ChamberlinFilter.calculate(FilterState())
        assertTrue(result.stable)
        assertEquals(160, result.response.size)
        assertTrue(result.response.all(Float::isFinite))
    }

    @Test
    fun highCutoffLowResonanceExposesInstability() {
        val result = ChamberlinFilter.calculate(FilterState(cutoffHz = 10_000f, resonance = 0.5f))
        assertFalse(result.stable)
    }
}
