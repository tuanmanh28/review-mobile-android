package io.github.tuanmanh28.learn.lesson02.exercises

// Exercise 3 — Weak listener registry
//
// Many screens can listen to the same ad SDK. The SDK must not keep any of them alive.
// iOS: NSHashTable.weakObjects() or an array of `struct Weak { weak var value }`.
// Android/JVM: a list of WeakReference<T>.

/**
 * A list of listeners held weakly.
 * Hint: `private val refs = mutableListOf<WeakReference<T>>()`
 */
class WeakListeners<T : Any> {
    /**
     * 3.1 Add a listener. Adding the SAME object (===) twice must not create a second entry.
     */
    fun add(listener: T) {
        TODO("Ex03.1")
    }

    /**
     * 3.2 Call [action] on every listener that is still alive, in the order they were added.
     * Collected listeners are skipped (no crash, no null passed in).
     */
    fun forEach(action: (T) -> Unit) {
        TODO("Ex03.2")
    }

    /** 3.3 Number of listeners that are still alive. */
    val liveCount: Int get() = TODO("Ex03.3 liveCount")

    /** 3.3 Number of stored entries, dead or alive (so the tests can check compact()). */
    val slotCount: Int get() = TODO("Ex03.3 slotCount")

    /** 3.3 Remove the entries whose listener has been collected. */
    fun compact() {
        TODO("Ex03.3 compact")
    }
}
