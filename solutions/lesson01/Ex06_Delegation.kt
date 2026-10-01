package io.github.tuanmanh28.learn.lesson01.exercises

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ClampedDouble(
    initial: Double,
    private val range: ClosedFloatingPointRange<Double>,
) : ReadWriteProperty<Any?, Double> {
    private var value = initial.coerceIn(range)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Double = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        this.value = value.coerceIn(range)
    }
}

class BidSettings {
    var floorUsd: Double by ClampedDouble(0.0, 0.0..50.0)
}

class CountingNetwork(private val inner: AdNetwork) : AdNetwork by inner {
    var loadCount: Int = 0
        private set

    override fun load(unitId: String): AdLoadResult {
        loadCount++
        return inner.load(unitId)
    }
}
