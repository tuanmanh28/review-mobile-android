package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * 6. LeakCanary — how it finds leaks
 *
 * Setup in a real app (debug builds only, no code needed):
 *     debugImplementation("com.squareup.leakcanary:leakcanary-android:<version>")
 *
 * What it does:
 *  1. Hooks into the lifecycle. When an Activity/Fragment/View/ViewModel is destroyed, it is "watched":
 *     wrapped in a WeakReference registered with a ReferenceQueue.
 *  2. Waits ~5 s, triggers a GC, then drains the queue. References found in the queue were collected → fine.
 *  3. A reference NOT in the queue = the object is "retained". After a few retained objects it dumps the heap
 *     (Debug.dumpHprofData) and analyses it (library "Shark").
 *  4. It finds the SHORTEST path of strong references from a GC root to the leaked object and shows it as a
 *     "leak trace" — the reference you must break is somewhere on that path.
 *
 * iOS: there is no LeakCanary; you use Xcode's Memory Graph debugger (also a path from roots) and the
 * Leaks instrument, or check `deinit` is called.
 *
 * Below: a tiny watcher with the same core idea (exercise 5 builds a fuller one).
 */
object LeakCanaryDemo : Demo {
    override val id = "D06"
    override val title = "LeakCanary"
    override val counterpart = "finding leaks"

    class Activity(val name: String) {
        val viewTree = ByteArray(256 * 1024)
    }

    /** Pretend this is a static list inside an SDK (a GC root). */
    object AdSdkListeners {
        val listeners = mutableListOf<Any>()
    }

    /** The core of LeakCanary's ObjectWatcher, in 15 lines. */
    class TinyWatcher {
        private val queue = ReferenceQueue<Any>()
        private val watched = mutableMapOf<WeakReference<Any>, String>()

        fun watch(obj: Any, name: String) {
            watched[WeakReference(obj, queue)] = name
        }

        /** Drains the queue: everything found there was collected. What's left is retained. */
        fun retained(): List<String> {
            while (true) {
                val ref = queue.poll() ?: break
                watched.remove(ref)
            }
            return watched.values.toList()
        }
    }

    private fun openAndDestroy(watcher: TinyWatcher, name: String, leak: Boolean) {
        val screen = Activity(name)
        if (leak) AdSdkListeners.listeners += screen      // the bug
        // ... onDestroy() ...
        watcher.watch(screen, name)
    }

    override fun run(): List<String> = buildList {
        AdSdkListeners.listeners.clear()
        val watcher = TinyWatcher()

        openAndDestroy(watcher, "HomeActivity", leak = false)
        openAndDestroy(watcher, "SettingsActivity", leak = true)
        add("watching 2 destroyed screens: ${watcher.retained()}")

        // Like LeakCanary: trigger GC, give the reference queue a moment, check again.
        val deadline = System.nanoTime() + 300_000_000
        while (watcher.retained().size > 1 && System.nanoTime() < deadline) {
            System.gc()
            Thread.sleep(15)
        }
        val retained = watcher.retained()
        add("after GC, retained = $retained")
        if (retained.isNotEmpty()) {
            add("→ LeakCanary would now dump the heap and print a leak trace like:")
            add("  ┬ GC Root: static field")
            add("  ├─ LeakCanaryDemo\$AdSdkListeners.listeners")
            add("  │    ↓ ArrayList.elementData")
            add("  ├─ Object[] array")
            add("  │    ↓ Object[0]")
            add("  ╰→ LeakCanaryDemo\$Activity  (destroyed, retained 262 KB)")
            add("  fix: remove it from the list in onDestroy / hold it weakly")
        }

        AdSdkListeners.listeners.clear()
    }
}
