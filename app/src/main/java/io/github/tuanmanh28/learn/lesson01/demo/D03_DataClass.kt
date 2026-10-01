package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 3. data class
 *
 * A data class is still a CLASS = a REFERENCE type. The compiler just generates:
 * equals() / hashCode() / toString() / copy() / component1..N() based on the constructor properties.
 *
 * Key difference from a Swift struct (a VALUE type):
 *   val b = a      → b and a point to the SAME object
 *   a.copy()       → a new object, but a SHALLOW copy: the list inside is still shared
 */
object DataClassDemo : Demo {
    override val id = "D03"
    override val title = "data class"
    override val counterpart = "struct"

    data class AdConfig(
        val unitId: String,
        val timeoutMs: Long = 3_000,
        val tags: MutableList<String> = mutableListOf(),
    )

    data class MutableCounter(var impressions: Int)

    override fun run(): List<String> = buildList {
        val a = AdConfig("banner_home")
        add("toString(): $a")

        val same = AdConfig("banner_home")
        add("a == same  (equals, compares content) = ${a == same}")
        add("a === same (same object?)             = ${a === same}")

        val b = a
        add("val b = a → a === b = ${a === b}   // assignment = sharing the reference")

        val c = a.copy(timeoutMs = 5_000)
        add("c = a.copy(timeoutMs = 5000) → c.timeoutMs=${c.timeoutMs}, a.timeoutMs=${a.timeoutMs}")
        c.tags.add("vip")
        add("c.tags.add(\"vip\") → a.tags = ${a.tags}   ⚠️ shallow copy: the list is shared")

        val (unit, timeout) = c   // destructuring = component1(), component2()
        add("val (unit, timeout) = c → unit=$unit, timeout=$timeout")

        val counter = MutableCounter(0)
        val alias = counter
        alias.impressions += 1
        add("data class with var: changing alias → counter.impressions = ${counter.impressions}   // a Swift struct would give 0")
    }
}
