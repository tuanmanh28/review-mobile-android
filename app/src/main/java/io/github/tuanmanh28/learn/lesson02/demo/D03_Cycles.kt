package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo
import java.lang.ref.WeakReference

/**
 * 3. Reference cycles are NOT a problem for a tracing GC
 *
 * AdLoader → AdView and AdView → AdLoader: a cycle.
 *  - Swift (ARC): each keeps the other's retain count at 1 → neither is ever freed = "retain cycle".
 *    You must break it with `weak` / `unowned`.
 *  - JVM/ART: the GC asks "can I reach it from a root?", not "how many refs point to it?".
 *    If the whole cycle is unreachable, the whole cycle is collected.
 *
 * So on Android, a "memory leak" means: something you no longer need is STILL REACHABLE from a GC root
 * (a static field, a singleton, a running thread, a registered listener…). Cycles alone never leak.
 */
object CyclesDemo : Demo {
    override val id = "D03"
    override val title = "GC collects cycles"
    override val counterpart = "retain cycle"

    class AdLoader(val unitId: String) {
        var view: AdView? = null
    }

    class AdView {
        var loader: AdLoader? = null             // back reference → cycle
        val pixels = ByteArray(128 * 1024)
    }

    /** A long-lived registry (static = GC root). */
    object LoaderRegistry {
        val loaders = mutableListOf<AdLoader>()
    }

    private fun makeCycle(register: Boolean): Pair<WeakReference<AdLoader>, WeakReference<AdView>> {
        val loader = AdLoader("banner_home")
        val view = AdView()
        loader.view = view
        view.loader = loader
        if (register) LoaderRegistry.loaders += loader
        return WeakReference(loader) to WeakReference(view)
    }

    override fun run(): List<String> = buildList {
        LoaderRegistry.loaders.clear()

        val (loader1, view1) = makeCycle(register = false)
        add("cycle AdLoader ⇄ AdView, unreachable:")
        add("  loader → ${Gc.status(loader1)}")
        add("  view   → ${Gc.status(view1)}   // with ARC both would leak")

        val (loader2, view2) = makeCycle(register = true)
        add("same cycle, but the loader is in LoaderRegistry (GC root):")
        add("  loader → ${Gc.status(loader2)}")
        add("  view   → ${Gc.status(view2)}   // ← THIS is a leak on Android")

        LoaderRegistry.loaders.clear()
        add("registry cleared → loader ${Gc.status(loader2)}, view ${Gc.status(view2)}")
    }
}
