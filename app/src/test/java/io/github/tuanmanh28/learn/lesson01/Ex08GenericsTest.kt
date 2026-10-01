package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex08GenericsTest {
    @Test fun `8_1 secondOrNull`() {
        assertEquals("b", listOf("a", "b", "c").secondOrNull())
        assertNull(listOf(1).secondOrNull())
        assertNull(emptyList<Int>().secondOrNull())
    }

    @Test fun `8_2 countOf reified`() {
        val events = listOf(
            AdEvent.Impression("A", 1), AdEvent.Click("A"), AdEvent.Impression("B", 2), AdEvent.Closed,
        )
        assertEquals(2, events.countOf<AdEvent.Impression>())
        assertEquals(1, events.countOf<AdEvent.Click>())
        assertEquals(0, events.countOf<AdEvent.LoadFailed>())
    }

    @Test fun `8_3 TtlCache`() {
        var now = 0L
        val cache = TtlCache<String, Int>(ttlMs = 1_000, clock = { now })
        cache.put("ecpm", 5)
        assertEquals(5, cache.get("ecpm"))
        now = 999
        assertEquals(5, cache.get("ecpm"))
        now = 1_000
        assertNull("expires once ttlMs has passed", cache.get("ecpm"))
        assertEquals("an expired entry must be removed", 0, cache.size)
        assertNull(cache.get("missing"))
    }
}
