package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.WeakReference

class AdLoader {
    private var listenerRef: WeakReference<AdLoadListener>? = null

    var listener: AdLoadListener?
        get() = listenerRef?.get()
        set(value) {
            listenerRef = value?.let { WeakReference(it) }
        }

    fun deliverLoaded(unitId: String, ecpm: Double): Boolean {
        val target = listener ?: return false     // read once: the GC can clear the ref between two get() calls
        target.onAdLoaded(unitId, ecpm)
        return true
    }
}
