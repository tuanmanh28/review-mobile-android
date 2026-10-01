package io.github.tuanmanh28.learn.lesson01.exercises

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Exercise 6 — Delegation

/** 6.1 Property delegate: always keeps the value within [range] (including the initial value). Hint: coerceIn */
class ClampedDouble(
    initial: Double,
    private val range: ClosedFloatingPointRange<Double>,
) : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double = TODO("Ex06.1 get")
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        TODO("Ex06.1 set")
    }
}

class BidSettings {
    var floorUsd: Double by ClampedDouble(0.0, 0.0..50.0)
}

/**
 * 6.2 Class delegation: CountingNetwork counts how many times load() is called.
 *  - Change `: AdNetwork` to `: AdNetwork by inner` → DELETE name/priority below entirely (the compiler forwards them)
 *  - Keep only the load() override: increment loadCount, then call inner.load()
 */
class CountingNetwork(private val inner: AdNetwork) : AdNetwork {
    var loadCount: Int = 0
        private set

    override val name: String get() = TODO("Ex06.2 — delete this line when using `by inner`")
    override val priority: Int get() = TODO("Ex06.2 — delete this line when using `by inner`")
    override fun load(unitId: String): AdLoadResult = TODO("Ex06.2 load")
}
