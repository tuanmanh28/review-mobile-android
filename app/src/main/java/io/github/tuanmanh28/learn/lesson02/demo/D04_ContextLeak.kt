package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo
import java.lang.ref.WeakReference

/**
 * 4. The classic Android leak: something long-lived holds an Activity (= a Context)
 *
 * An Activity holds its whole view tree, bitmaps, adapters… and it is destroyed on every rotation/back press.
 * If a singleton, a companion object, a static list or a background thread still references it, the GC can
 * reach it from a root → it can never be freed.
 *
 * Hidden ways to hold `this@Activity`:
 *  - a non-static `inner class` (and an anonymous `object : Listener {}` inside a member function):
 *    the compiler adds a field `this$0` pointing to the outer Activity.
 *  - a lambda that uses a member (`title`, `finish()`, a view…): it captures `this`.
 *  - storing `this` as a Context in a singleton: `AdSdk.init(this)`.
 *
 * Fixes:
 *  1. Keep `applicationContext` instead of the Activity when you only need a Context.
 *  2. Unregister in onDestroy (or use a lifecycle-aware API that does it for you).
 *  3. Hold the Activity through a WeakReference (Kotlin's equivalent of `[weak self]`).
 *  4. Prefer a nested class (static in Java) + explicit parameters over an `inner class`.
 *
 * Swift: closures capture strongly too; you write `{ [weak self] in ... }`. Kotlin has no capture list.
 */
object ContextLeakDemo : Demo {
    override val id = "D04"
    override val title = "Context leak via lambda / inner class"
    override val counterpart = "closure capture list"

    // ── Tiny fakes of the Android types (the real ones need a device) ──
    open class Context {
        open val applicationContext: Context get() = this
    }

    object App : Context()

    open class Activity(val name: String) : Context() {
        override val applicationContext: Context get() = App
        val viewTree = ByteArray(256 * 1024)
        var title = "$name screen"

        /** Inner class → has a hidden `this$0` field pointing to this Activity. */
        inner class RewardListener {
            fun onReward(amount: Int) {
                title = "+$amount coins"
            }
        }

        /** Lambda that reads `title` → captures this Activity. */
        fun impressionCallback(): (String) -> Unit = { event -> title = "$event on $name" }

        /** Fix: capture a WeakReference, never `this`. */
        fun weakImpressionCallback(): (String) -> Unit {
            val ref = WeakReference(this)
            return { event -> ref.get()?.let { it.title = "$event on ${it.name}" } }
        }
    }

    /** A process-wide singleton: everything stored here lives as long as the app. */
    object AdSdk {
        var context: Context? = null
        val callbacks = mutableListOf<Any>()

        fun reset() {
            context = null
            callbacks.clear()
        }
    }

    // Each scenario opens a screen in its own function, "destroys" it, and returns only a WeakReference.

    private fun initWithActivity(): WeakReference<Activity> {
        val splash = Activity("Splash")
        AdSdk.context = splash                         // ❌ the singleton now holds the Activity
        return WeakReference(splash)
    }

    private fun initWithAppContext(): WeakReference<Activity> {
        val splash = Activity("Splash")
        AdSdk.context = splash.applicationContext      // ✅ only the Application
        return WeakReference(splash)
    }

    private fun registerLambda(): WeakReference<Activity> {
        val home = Activity("Home")
        AdSdk.callbacks += home.impressionCallback()   // ❌ the lambda captures `home`
        return WeakReference(home)
    }

    private fun registerInnerClass(): WeakReference<Activity> {
        val home = Activity("Home")
        AdSdk.callbacks += home.RewardListener()       // ❌ inner class → this$0 = home
        return WeakReference(home)
    }

    private fun registerWeakLambda(): WeakReference<Activity> {
        val home = Activity("Home")
        AdSdk.callbacks += home.weakImpressionCallback()  // ✅ captures only a WeakReference
        return WeakReference(home)
    }

    override fun run(): List<String> = buildList {
        AdSdk.reset()

        add("AdSdk.context = activity           → Activity ${Gc.status(initWithActivity())}  ❌")
        AdSdk.reset()
        add("AdSdk.context = applicationContext → Activity ${Gc.status(initWithAppContext())}  ✅")
        AdSdk.reset()

        val viaLambda = registerLambda()
        add("singleton keeps a lambda using title → Activity ${Gc.status(viaLambda)}  ❌")
        AdSdk.callbacks.clear()                        // = unregister in onDestroy
        add("  after unregister                   → Activity ${Gc.status(viaLambda)}  ✅")

        add("singleton keeps an inner class       → Activity ${Gc.status(registerInnerClass())}  ❌")
        val hidden = Activity.RewardListener::class.java.declaredFields.joinToString { "${it.name}: ${it.type.simpleName}" }
        add("  inner class hidden fields: $hidden")
        AdSdk.reset()

        add("singleton keeps a WeakReference lambda → Activity ${Gc.status(registerWeakLambda())}  ✅")
        AdSdk.reset()
    }
}
