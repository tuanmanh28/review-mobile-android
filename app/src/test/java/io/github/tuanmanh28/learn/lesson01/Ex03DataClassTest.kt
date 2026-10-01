package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex03DataClassTest {
    @Test fun `3_1 forTesting returns a new copy`() {
        val original = AdConfig("banner_home")
        val test = original.forTesting()
        assertTrue(test.testMode)
        assertEquals(10_000L, test.timeoutMs)
        assertEquals("banner_home", test.unitId)
        assertFalse("the original object must not be modified", original.testMode)
        assertNotSame(original, test)
    }

    @Test fun `3_2 mergeUnique`() {
        val a = AdConfig("a")
        val b = AdConfig("b", timeoutMs = 5_000)
        val result = mergeUnique(listOf(a, AdConfig("a"), b, AdConfig("b", timeoutMs = 5_000), AdConfig("b")))
        assertEquals(listOf(a, b, AdConfig("b")), result)
    }

    @Test fun `3_3 describe`() {
        assertEquals("unit=inter_1, timeout=4500ms", describe(AdConfig("inter_1", timeoutMs = 4_500)))
    }
}
