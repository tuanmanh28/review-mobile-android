package io.github.tuanmanh28.learn.lesson02.exercises

// Exercise 2 — Lambda capture leak (see AdEventBus in Models.kt)
//
// A lambda that USES `activity` captures it: the lambda object keeps a strong reference to the Activity.
// Store that lambda in something long-lived (AdEventBus) and the Activity can no longer be collected.
// iOS: `bus.register { [weak self] event in ... }`
// Android/JVM: there is no capture list — either unregister in onDestroy, or capture a WeakReference instead.

/**
 * 2.1 Banner controller for one screen.
 *  - attach(activity): register a callback on [bus] under the key [KEY]. The callback appends
 *    "banner: <event>" to `activity.shown`.
 *  - detach(): unregister [KEY] so the bus no longer references the activity.
 * Do NOT keep the activity in a field of BannerController (the controller is cached by the SDK too).
 */
class BannerController(private val bus: AdEventBus) {
    fun attach(activity: Activity) {
        TODO("Ex02.1 attach")
    }

    fun detach() {
        TODO("Ex02.1 detach")
    }

    companion object {
        const val KEY = "banner_home"
    }
}

/**
 * 2.2 The "[weak self]" of Kotlin: build a callback that does NOT keep [activity] alive.
 *  - capture only a WeakReference to the activity (not the activity itself!)
 *  - when called: if the activity is still alive → call block(activity, event); if it is gone → do nothing
 * Usage: bus.register("x", weakCallback(activity) { act, event -> act.shown += event })
 */
fun weakCallback(activity: Activity, block: (Activity, String) -> Unit): (String) -> Unit = TODO("Ex02.2")
