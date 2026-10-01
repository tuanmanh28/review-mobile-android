package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex04SealedTest {
    private val events = listOf(
        AdEvent.Impression("AdMob", 1_500_000),
        AdEvent.Click("AdMob"),
        AdEvent.Impression("Meta", 250_000),
        AdEvent.LoadFailed("Meta", 3),
        AdEvent.Closed,
    )

    @Test fun `4_1 logName`() {
        assertEquals(
            listOf("ad_impression", "ad_click", "ad_impression", "ad_load_failed", "ad_closed"),
            events.map { it.logName() },
        )
    }

    @Test fun `4_2 totalRevenueUsd`() {
        assertEquals(1.75, totalRevenueUsd(events), 1e-9)
        assertEquals(0.0, totalRevenueUsd(emptyList()), 1e-9)
    }

    @Test fun `4_3 shouldRetry`() {
        assertTrue(shouldRetry(AdEvent.LoadFailed("AdMob", 0)))
        assertTrue(shouldRetry(AdEvent.LoadFailed("AdMob", 2)))
        assertFalse(shouldRetry(AdEvent.LoadFailed("AdMob", 3)))
        assertFalse(shouldRetry(AdEvent.Click("AdMob")))
    }
}
