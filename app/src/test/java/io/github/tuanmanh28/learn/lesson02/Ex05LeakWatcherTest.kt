package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.lesson02.GcTestSupport.awaitUntil
import io.github.tuanmanh28.learn.lesson02.GcTestSupport.gcRounds
import io.github.tuanmanh28.learn.lesson02.exercises.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.Reference

class Ex05LeakWatcherTest {
    private val app = Application()

    /** Opens and destroys screens that nobody keeps → they should be collected. */
    private fun destroyTemporaryScreens(watcher: LeakWatcher, vararg names: String) {
        names.forEach { name ->
            val screen = Activity(name, app)
            screen.onDestroy()
            watcher.watch(screen, name)
        }
    }

    @Test fun `5_1 watch stores a reference`() {
        val watcher = LeakWatcher()
        val home = Activity("Home", app)
        assertEquals(0, watcher.watchedCount)
        watcher.watch(home, "Home")
        watcher.watch(Activity("Detail", app), "Detail")
        assertEquals(2, watcher.watchedCount)
        Reference.reachabilityFence(home)
    }

    @Test fun `5_1 watch does not keep the object alive`() {
        val watcher = LeakWatcher()
        destroyTemporaryScreens(watcher, "Home")
        assertTrue("the watcher itself keeps the object alive", awaitUntil { watcher.retainedNames().isEmpty() })
    }

    @Test fun `5_2 retainedNames reports only the leaked screens`() {
        val watcher = LeakWatcher()
        val leakyStatic = mutableListOf<Activity>()       // pretend this is a static field = a GC root
        val leaked = Activity("Settings", app).also { it.onDestroy() }
        leakyStatic += leaked

        destroyTemporaryScreens(watcher, "Home")
        watcher.watch(leaked, "Settings")
        destroyTemporaryScreens(watcher, "Detail")

        assertTrue(
            "after GC only the leaked Settings screen should be retained",
            awaitUntil { watcher.retainedNames() == listOf("Settings") },
        )
        assertEquals("collected references must be removed", 1, watcher.watchedCount)
        Reference.reachabilityFence(leakyStatic)
    }

    @Test fun `5_2 retainedNames keeps watch order while all are alive`() {
        val watcher = LeakWatcher()
        val a = Activity("A", app)
        val b = Activity("B", app)
        watcher.watch(a, "A")
        watcher.watch(b, "B")
        gcRounds()
        assertEquals(listOf("A", "B"), watcher.retainedNames())
        Reference.reachabilityFence(a)
        Reference.reachabilityFence(b)
    }
}
