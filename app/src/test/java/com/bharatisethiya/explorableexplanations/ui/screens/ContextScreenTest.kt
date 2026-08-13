package com.bharatisethiya.explorableexplanations.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContextScreenTest {
    @Test
    fun passageWordsExposeEveryDistinctWordForAccessibility() {
        val words = passageWords("California's wind, wind — NREL 2004.")

        assertEquals(listOf("California's", "wind", "NREL", "2004"), words)
        assertTrue(words.none { it.isBlank() })
    }
}
