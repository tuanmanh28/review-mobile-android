package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class KeyedWeakReference(
    referent: Any,
    val name: String,
    queue: ReferenceQueue<Any>,
) : WeakReference<Any>(referent, queue)

class LeakWatcher {
    private val queue = ReferenceQueue<Any>()
    private val watched = mutableListOf<KeyedWeakReference>()

    fun watch(obj: Any, name: String) {
        watched += KeyedWeakReference(obj, name, queue)
    }

    val watchedCount: Int get() = watched.size

    fun retainedNames(): List<String> {
        while (true) {
            val collected = queue.poll() ?: break
            watched.removeAll { it === collected }
        }
        return watched.map { it.name }
    }
}
