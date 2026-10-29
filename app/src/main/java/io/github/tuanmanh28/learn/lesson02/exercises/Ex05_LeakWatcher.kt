package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

// Exercise 5 — Mini LeakCanary
//
// How LeakCanary works (simplified):
//  1. When an Activity is destroyed, it is "watched": wrapped in a WeakReference registered with a ReferenceQueue.
//  2. After the GC clears a WeakReference, the JVM puts that reference object into the queue.
//  3. Later: drain the queue → every reference found there was collected = fine.
//     Whatever is still watched and NOT in the queue is "retained" = a probable leak → heap dump.

/** A WeakReference that also remembers a name. Given — no need to change it. */
class KeyedWeakReference(
    referent: Any,
    val name: String,
    queue: ReferenceQueue<Any>,
) : WeakReference<Any>(referent, queue)

class LeakWatcher {
    private val queue = ReferenceQueue<Any>()

    /**
     * 5.1 Start watching [obj] under [name].
     *  - create a KeyedWeakReference(obj, name, queue) and keep it in a collection
     *    (keep the REFERENCE strongly, never [obj] itself!)
     *  - watchedCount = how many references are currently stored
     */
    fun watch(obj: Any, name: String) {
        TODO("Ex05.1 watch")
    }

    val watchedCount: Int get() = TODO("Ex05.1 watchedCount")

    /**
     * 5.2 Names of the watched objects that are still in memory, in the order they were watched.
     *  - first drain the queue: `queue.poll()` returns a collected reference, or null when the queue is empty;
     *    remove every reference you get from your collection
     *  - then return the names of the references left
     */
    fun retainedNames(): List<String> = TODO("Ex05.2")
}
