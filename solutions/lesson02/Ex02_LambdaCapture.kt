package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.WeakReference

class BannerController(private val bus: AdEventBus) {
    fun attach(activity: Activity) {
        // This lambda captures `activity` strongly — fine, because detach() removes it from the bus.
        bus.register(KEY) { event -> activity.shown += "banner: $event" }
    }

    fun detach() {
        bus.unregister(KEY)
    }

    companion object {
        const val KEY = "banner_home"
    }
}

fun weakCallback(activity: Activity, block: (Activity, String) -> Unit): (String) -> Unit {
    val ref = WeakReference(activity)          // the lambda below captures `ref`, never `activity`
    return { event -> ref.get()?.let { block(it, event) } }
}
