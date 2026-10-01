package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex07ScopeFunctionTest {
    @Test fun `7_1 buildRequest`() {
        val r = buildRequest("banner", listOf("puzzle", "casual"))
        assertEquals("banner", r.unitId)
        assertEquals(listOf("puzzle", "casual"), r.keywords)
        assertTrue(r.testMode)
    }

    @Test fun `7_2 normalizeKeyword`() {
        assertEquals("casual game", normalizeKeyword("  Casual Game "))
        assertNull(normalizeKeyword("   "))
        assertNull(normalizeKeyword(null))
    }

    @Test fun `7_3 createAndLog`() {
        val log = mutableListOf<String>()
        val config = createAndLog("reward_1", log)
        assertEquals(AdConfig("reward_1"), config)
        assertEquals(listOf("created:reward_1"), log)
    }
}
