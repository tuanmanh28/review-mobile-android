package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.WeakReference

class WeakListeners<T : Any> {
    private val refs = mutableListOf<WeakReference<T>>()

    fun add(listener: T) {
        if (refs.any { it.get() === listener }) return
        refs += WeakReference(listener)
    }

    fun forEach(action: (T) -> Unit) {
        // Copy the live ones first: `action` may add listeners, and each get() is read only once.
        refs.mapNotNull { it.get() }.forEach(action)
    }

    val liveCount: Int get() = refs.count { it.get() != null }

    val slotCount: Int get() = refs.size

    fun compact() {
        refs.removeAll { it.get() == null }
    }
}
