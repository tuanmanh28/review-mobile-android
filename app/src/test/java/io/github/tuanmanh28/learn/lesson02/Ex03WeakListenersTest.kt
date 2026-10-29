package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.lesson02.GcTestSupport.awaitUntil
import io.github.tuanmanh28.learn.lesson02.GcTestSupport.gcRounds
import io.github.tuanmanh28.learn.lesson02.exercises.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.Reference

class Ex03WeakListenersTest {
    class Screen(val name: String) {
        val viewTree = ByteArray(64 * 1024)
        override fun toString() = name
    }

    /** Adds screens that nobody else references (created here, so the test method cannot keep them alive). */
    private fun addTemporaryScreens(listeners: WeakListeners<Screen>, vararg names: String) {
        names.forEach { listeners.add(Screen(it)) }
    }

    @Test fun `3_1 add ignores the same object twice`() {
        val listeners = WeakListeners<Screen>()
        val home = Screen("home")
        val detail = Screen("detail")
        listeners.add(home)
        listeners.add(detail)
        listeners.add(home)
        assertEquals(2, listeners.slotCount)
        listeners.add(Screen("home"))       // a different object with the same name is a new listener
        assertEquals(3, listeners.slotCount)
        Reference.reachabilityFence(home)
        Reference.reachabilityFence(detail)
    }

    @Test fun `3_2 forEach visits live listeners in order`() {
        val listeners = WeakListeners<Screen>()
        val a = Screen("a")
        val b = Screen("b")
        listeners.add(a)
        listeners.add(b)
        val visited = mutableListOf<String>()
        listeners.forEach { visited += it.name }
        assertEquals(listOf("a", "b"), visited)
    }

    @Test fun `3_2 forEach skips collected listeners`() {
        val listeners = WeakListeners<Screen>()
        val kept = Screen("kept")
        listeners.add(kept)
        addTemporaryScreens(listeners, "gone1", "gone2")
        val onlyKeptLeft = awaitUntil {
            val visited = mutableListOf<String>()
            listeners.forEach { visited += it.name }
            visited == listOf("kept")
        }
        assertTrue("forEach must skip collected listeners (or the registry holds them strongly)", onlyKeptLeft)
        Reference.reachabilityFence(kept)
    }

    @Test fun `3_3 liveCount and compact`() {
        val listeners = WeakListeners<Screen>()
        val kept = Screen("kept")
        listeners.add(kept)
        addTemporaryScreens(listeners, "gone1", "gone2")
        assertEquals(3, listeners.slotCount)
        assertTrue("liveCount should drop to 1 after GC", awaitUntil { listeners.liveCount == 1 })
        assertEquals("dead entries stay until compact()", 3, listeners.slotCount)
        listeners.compact()
        assertEquals(1, listeners.slotCount)
        assertEquals(1, listeners.liveCount)
        Reference.reachabilityFence(kept)
    }

    @Test fun `3_3 live listeners survive GC`() {
        val listeners = WeakListeners<Screen>()
        val a = Screen("a")
        val b = Screen("b")
        listeners.add(a)
        listeners.add(b)
        gcRounds()
        listeners.compact()
        assertEquals(2, listeners.liveCount)
        assertEquals(2, listeners.slotCount)
        Reference.reachabilityFence(a)
        Reference.reachabilityFence(b)
    }
}
