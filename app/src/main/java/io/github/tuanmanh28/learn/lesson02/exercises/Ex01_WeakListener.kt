package io.github.tuanmanh28.learn.lesson02.exercises

// Exercise 1 — Weak listener
// Run the tests: app/src/test/.../lesson02/Ex01WeakListenerTest.kt  (click ▶ next to the class name)
//
// The AdLoader may live longer than the screen (it is cached by the ad SDK).
// If it holds the listener (= an Activity) with a normal, strong reference, the destroyed Activity can never be collected.
// iOS: `weak var delegate: AdLoaderDelegate?`
// Android/JVM: there is no `weak` keyword — you wrap the object in java.lang.ref.WeakReference.

class AdLoader {
    /**
     * 1.1 Store the listener WEAKLY.
     *  - set: keep it inside a WeakReference (null → forget the old listener)
     *  - get: return the listener if it is still alive, otherwise null
     * Hint: add a private field `private var listenerRef: WeakReference<AdLoadListener>? = null`.
     */
    var listener: AdLoadListener?
        get() = TODO("Ex01.1 get")
        set(value) {
            TODO("Ex01.1 set")
        }

    /**
     * 1.2 The ad network finished loading [unitId] with [ecpm].
     *  - if the listener is still alive → call onAdLoaded and return true
     *  - if there is no listener or it has been collected → return false (and do NOT crash)
     * Read the reference ONCE into a local val, then use that val (the GC may run between two get() calls).
     */
    fun deliverLoaded(unitId: String, ecpm: Double): Boolean = TODO("Ex01.2")
}
