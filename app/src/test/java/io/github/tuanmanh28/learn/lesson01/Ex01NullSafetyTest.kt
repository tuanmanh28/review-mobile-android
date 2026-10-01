package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex01NullSafetyTest {
    @Test fun `1_1 displayName`() {
        assertEquals("Tuan", displayName(AdUser("Tuan", null)))
        assertEquals("Guest", displayName(AdUser(null, "VN")))
        assertEquals("Guest", displayName(AdUser("   ", "VN")))
        assertEquals("Guest", displayName(null))
    }

    @Test fun `1_2 parseBid`() {
        assertEquals(0.35, parseBid("0.35")!!, 1e-9)
        assertEquals(0.0, parseBid("0")!!, 1e-9)
        assertNull(parseBid(null))
        assertNull(parseBid("abc"))
        assertNull(parseBid("-1.2"))
    }

    @Test fun `1_3 countryOrDefault`() {
        assertEquals("US", countryOrDefault(AdUser("A", "US")))
        assertEquals("VN", countryOrDefault(AdUser("A", null)))
        assertEquals("VN", countryOrDefault(null))
    }
}
