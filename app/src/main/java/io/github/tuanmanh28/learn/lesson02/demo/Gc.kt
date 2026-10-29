package io.github.tuanmanh28.learn.lesson02.demo

import java.lang.ref.Reference

/**
 * Small helper shared by the Lesson 2 demos.
 *
 * System.gc() is only a HINT: the runtime may ignore it, and clearing references happens slightly later.
 * So we ask a few times with a short pause. Kept short (≈ 300 ms max) because the app runs demos on the main thread.
 */
internal object Gc {
    @Volatile private var sink: Any? = null

    /** Asks for GC until [ref] is cleared or [maxMs] has passed. Returns true if it was collected. */
    fun collected(ref: Reference<*>, maxMs: Long = 300): Boolean {
        val deadline = System.nanoTime() + maxMs * 1_000_000
        while (ref.get() != null && System.nanoTime() < deadline) {
            repeat(8) { sink = ByteArray(16 * 1024) }   // a little garbage so the young generation has work
            sink = null
            System.gc()
            Thread.sleep(15)
        }
        return ref.get() == null
    }

    fun status(ref: Reference<*>): String =
        if (collected(ref)) "collected" else "still alive"
}
