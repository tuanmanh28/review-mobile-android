package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

/**
 * 2. Reference strength — java.lang.ref
 *
 *  - Strong (a normal variable/field): keeps the object alive.
 *  - WeakReference: does NOT keep it alive. Cleared at the next GC once no strong ref remains.
 *    → listeners, callbacks to screens, caches keyed by an object (WeakHashMap).
 *  - SoftReference: kept as long as there is enough memory; cleared before an OutOfMemoryError.
 *    → memory-sensitive caches. (On Android, LruCache with a fixed size is usually preferred: more predictable.)
 *  - PhantomReference: get() is ALWAYS null. Only tells you (via a ReferenceQueue) that the object has been
 *    collected → cleanup of native resources (what java.lang.ref.Cleaner is built on).
 *
 * Swift: `strong` (default), `weak` (Optional, becomes nil), `unowned` (non-optional, crashes if used after dealloc).
 * Kotlin has no keyword: you wrap the object, and you must call .get() — which may return null.
 */
object ReferencesDemo : Demo {
    override val id = "D02"
    override val title = "strong / weak / soft reference"
    override val counterpart = "strong / weak / unowned"

    class AdCreative(val unitId: String) {
        val bytes = ByteArray(64 * 1024)
        override fun toString() = "AdCreative($unitId)"
    }

    private fun weakOnly() = WeakReference(AdCreative("weak"))
    private fun softOnly() = SoftReference(AdCreative("soft"))
    private fun phantomOnly(queue: ReferenceQueue<AdCreative>) = PhantomReference(AdCreative("phantom"), queue)

    override fun run(): List<String> = buildList {
        // Strong + weak to the same object: the strong one wins.
        val strong = AdCreative("strong")
        val weakToStrong = WeakReference(strong)
        add("weak ref, object still held strongly → ${Gc.status(weakToStrong)}")
        add("  (strong is still used: ${strong.unitId})")

        // Weak only.
        val weak = weakOnly()
        add("weak ref only → ${Gc.status(weak)}   // like a Swift weak var becoming nil")
        add("weak.get() = ${weak.get()}")

        // Soft only: a normal GC usually keeps it (there is plenty of free memory).
        val soft = softOnly()
        Gc.collected(weak) // a few more GC rounds
        add("soft ref only, after GC → ${if (soft.get() != null) "still alive (no memory pressure)" else "cleared"}")

        // Phantom: get() is always null; the queue tells us when the object is really gone.
        val queue = ReferenceQueue<AdCreative>()
        val phantom = phantomOnly(queue)
        add("phantom.get() = ${phantom.get()}   // always null, by design")
        var enqueued = queue.poll()
        val deadline = System.nanoTime() + 300_000_000
        while (enqueued == null && System.nanoTime() < deadline) {
            System.gc()
            enqueued = queue.remove(20)
        }
        add("phantom enqueued after GC → ${enqueued === phantom}   // time to free native memory")
    }
}
