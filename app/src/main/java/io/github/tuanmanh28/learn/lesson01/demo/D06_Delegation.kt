package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 6. Delegation — Kotlin has 2 kinds:
 *
 * a) Class delegation `: Interface by inner`
 *    The compiler generates forwarding functions to `inner`. You only override what you need.
 *    → Composition instead of inheritance (Decorator pattern) without writing boilerplate.
 *
 * b) Property delegation `val x by delegate`
 *    The property's get/set is handed off to another object: lazy, observable, or your own.
 *    → Swift equivalent: @propertyWrapper.
 */
object DelegationDemo : Demo {
    override val id = "D06"
    override val title = "Delegation"
    override val counterpart = "@propertyWrapper (+ manual forwarding)"

    // (a) Class delegation
    class LoggingNetwork(
        private val inner: InterfaceDemo.AdNetwork,
        private val log: MutableList<String>,
    ) : InterfaceDemo.AdNetwork by inner {                 // name, priority, describe() are forwarded automatically
        override fun load(unitId: String): SealedDemo.AdLoadResult {
            log += "→ ${inner.name}.load($unitId)"
            return inner.load(unitId).also { log += "← ${SealedDemo.render(it)}" }
        }
    }

    // (b) Hand-written property delegate
    class Clamped(initial: Int, private val range: IntRange) : ReadWriteProperty<Any?, Int> {
        private var value = initial.coerceIn(range)
        override fun getValue(thisRef: Any?, property: KProperty<*>) = value
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            this.value = value.coerceIn(range)
        }
    }

    class AdSettings(log: MutableList<String>) {
        var refreshSec: Int by Clamped(30, 15..120)

        var floorUsd: Double by Delegates.observable(0.0) { prop, old, new ->
            log += "observable: ${prop.name} $old → $new"
        }

        val heavyConfig: String by lazy {
            log += "lazy: computed only once, on first access"
            "config-loaded"
        }
    }

    override fun run(): List<String> = buildList {
        val log = mutableListOf<String>()
        val network = LoggingNetwork(InterfaceDemo.AdMobNetwork(), log)
        add("LoggingNetwork.name = ${network.name}, priority = ${network.priority}   // forwarded automatically")
        network.load("inter_level_end")

        val s = AdSettings(log)
        s.refreshSec = 5
        add("refreshSec = 5 → ${s.refreshSec}   // clamped to 15")
        s.refreshSec = 999
        add("refreshSec = 999 → ${s.refreshSec}   // clamped to 120")
        s.floorUsd = 0.8
        s.heavyConfig
        s.heavyConfig
        addAll(log)
    }
}
