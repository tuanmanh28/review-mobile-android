package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo
import java.lang.ref.WeakReference

/**
 * 1. Garbage collector & GC roots
 *
 * JVM / Android (ART): there is NO reference count. From time to time the GC starts from the "GC roots" and marks
 * everything it can reach. Whatever is not reached is garbage, no matter how many references point to it.
 *
 * GC roots are, roughly:
 *  - static fields (companion object / `object` / top-level vals) of loaded classes
 *  - local variables and parameters on the stack of every live thread
 *  - JNI references held by native code, monitors in use, …
 *
 * An unreachable object becomes ELIGIBLE for collection — it is freed at some later GC, not immediately.
 * There is no `deinit`: `finalize()` is deprecated, runs on another thread, maybe late, maybe never.
 * Clean up explicitly (close(), onDestroy(), `use {}`), never in a finalizer.
 *
 * Swift (ARC): every object has a retain count, the compiler inserts retain/release, and the object is freed
 * EXACTLY when the count reaches 0 → `deinit` runs right away, deterministically.
 */
object GcRootsDemo : Demo {
    override val id = "D01"
    override val title = "GC & GC roots"
    override val counterpart = "ARC & deinit"

    class AdBanner(val unitId: String) {
        val pixels = ByteArray(128 * 1024)
        override fun toString() = "AdBanner($unitId)"
    }

    /** A static field = a GC root. Everything reachable from here stays alive. */
    object BannerCache {
        val banners = mutableListOf<AdBanner>()
    }

    // Each object is created in a separate function: after the function returns,
    // its local variable is gone from the thread stack → no longer a root.
    private fun cachedBanner(): WeakReference<AdBanner> {
        val banner = AdBanner("banner_home")
        BannerCache.banners += banner
        return WeakReference(banner)
    }

    private fun temporaryBanner(): WeakReference<AdBanner> = WeakReference(AdBanner("banner_detail"))

    override fun run(): List<String> = buildList {
        BannerCache.banners.clear()

        val cached = cachedBanner()
        val temporary = temporaryBanner()

        add("banner_detail: only referenced by a finished function → ${Gc.status(temporary)}")
        add("banner_home: reachable from BannerCache (static = GC root) → ${Gc.status(cached)}")

        // The local variable `banner` below is ON THE STACK of this thread → also a root while it is in use.
        val banner = AdBanner("inter_level_end")
        val onStack = WeakReference(banner)
        add("inter_level_end: held by a local variable → ${Gc.status(onStack)}")
        add("  (still used here: ${banner.unitId})")

        BannerCache.banners.clear()
        add("BannerCache cleared → banner_home ${Gc.status(cached)}")

        add("No refcount on the JVM: an object has no 'retain count' you could print.")
        add("Swift: deinit runs the moment the last strong ref goes away; Kotlin: 'some time after', or never.")
    }
}
