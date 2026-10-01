package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 5. interface
 *
 * A Kotlin interface can contain: abstract functions, functions with a body (default), abstract properties
 * or getter-only properties. It CANNOT hold state (backing fields).
 *
 * Calling an interface function is always dynamic dispatch (via the JVM virtual table):
 * the subclass override is always called, even when the variable has the interface type.
 * (Unlike Swift: a function that only lives in a protocol extension and isn't declared in the protocol → static dispatch.)
 */
object InterfaceDemo : Demo {
    override val id = "D05"
    override val title = "interface"
    override val counterpart = "protocol + protocol extension"

    interface AdNetwork {
        val name: String
        val priority: Int get() = 0                     // default
        fun load(unitId: String): SealedDemo.AdLoadResult
        fun describe(): String = "$name (priority=$priority)"   // default
    }

    class AdMobNetwork : AdNetwork {
        override val name = "AdMob"
        override val priority = 10
        override fun load(unitId: String) = SealedDemo.AdLoadResult.Loaded(name, 1.4)
    }

    class MetaNetwork : AdNetwork {
        override val name = "Meta"
        override fun load(unitId: String) = SealedDemo.AdLoadResult.NoFill
        override fun describe() = "Meta Audience Network (custom describe)"
    }

    override fun run(): List<String> {
        val networks: List<AdNetwork> = listOf(MetaNetwork(), AdMobNetwork())
        val sorted = networks.sortedByDescending { it.priority }
        return networks.map { "describe(): ${it.describe()}" } +
            "Waterfall by priority: ${sorted.joinToString(" → ") { it.name }}" +
            sorted.map { "${it.name}.load() = ${SealedDemo.render(it.load("home"))}" }
    }
}
