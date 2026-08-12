package com.bharatisethiya.explorableexplanations.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContextRepositoryTest {
    @Test
    fun searchMatchesTitleAndSummaryCaseInsensitively() {
        assertEquals("Altamont Pass", ContextRepository.search("ALTAMONT").single().title)
        assertTrue(ContextRepository.search("southern california").size >= 2)
    }

    @Test
    fun unknownTermReturnsNoFabricatedResult() {
        assertTrue(ContextRepository.search("nuclear fusion").isEmpty())
    }
}
