package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex02ExtensionTest {
    @Test fun `2_1 toCompactString`() {
        assertEquals("999", 999.toCompactString())
        assertEquals("1K", 1000.toCompactString())
        assertEquals("1.5K", 1500.toCompactString())
        assertEquals("12.3K", 12_300.toCompactString())
        assertEquals("2M", 2_000_000.toCompactString())
        assertEquals("3.4M", 3_400_000.toCompactString())
    }

    @Test fun `2_2 isValidAdUnitId`() {
        assertTrue("ca-app-pub-3940256099942544/6300978111".isValidAdUnitId())
        assertFalse("ca-app-pub-3940256099942544".isValidAdUnitId())
        assertFalse("ca-app-pub-39402560999425/6300978111".isValidAdUnitId())
        assertFalse("xx-ca-app-pub-3940256099942544/6300978111".isValidAdUnitId())
    }

    @Test fun `2_3 orDash`() {
        val missing: String? = null
        assertEquals("-", missing.orDash())
        assertEquals("-", "  ".orDash())
        assertEquals("AdMob", "AdMob".orDash())
    }
}
