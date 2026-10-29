package io.github.tuanmanh28.learn.lesson02

import java.lang.ref.Reference

/**
 * Helpers for tests that depend on the garbage collector.
 *
 * System.gc() is only a HINT, and clearing/enqueueing references happens a little later on another thread.
 * So never assert right after one gc() call: retry gc + a short sleep until the condition holds or ~2 s pass.
 *
 * Important when writing such tests: create the object that should be collected inside a SEPARATE function that
 * returns only a WeakReference. A local variable in the test method itself can keep the object reachable.
 */
object GcTestSupport {
    /** Runs GC repeatedly until [condition] is true. Returns false on timeout. */
    fun awaitUntil(timeoutMs: Long = 2_000, condition: () -> Boolean): Boolean {
        val deadline = System.nanoTime() + timeoutMs * 1_000_000
        while (true) {
            if (condition()) return true
            if (System.nanoTime() > deadline) return false
            forceGc()
        }
    }

    /** Runs GC repeatedly until [ref] has been cleared. Returns false on timeout. */
    fun awaitCleared(ref: Reference<*>, timeoutMs: Long = 2_000): Boolean =
        awaitUntil(timeoutMs) { ref.get() == null }

    /** A few GC rounds, for checks that something must STAY alive (no need to wait the full timeout). */
    fun gcRounds(rounds: Int = 4) = repeat(rounds) { forceGc() }

    @Volatile private var garbageSink: ByteArray? = null

    private fun forceGc() {
        // A bit of garbage nudges the young generation, then ask for a full GC.
        repeat(16) { garbageSink = ByteArray(16 * 1024) }
        garbageSink = null
        System.gc()
        Thread.sleep(20)
    }
}
