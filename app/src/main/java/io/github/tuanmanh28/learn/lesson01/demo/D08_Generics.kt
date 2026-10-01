package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 8. Generic
 *
 * - Type erasure: on the JVM, List<String> and List<Int> are both just List at runtime.
 *   → you can't write `x is List<String>`.
 * - reified (inline functions only): the compiler inlines the real type at the call site → `is T` works.
 * - Declaration-site variance:  out T (only produced → covariant), in T (only consumed → contravariant).
 *   List<out E>, so List<Banner> can be assigned to List<AdFormat>; MutableList<E> CANNOT.
 *
 * Swift: generics are "specialized" (no erasure), `some P` (opaque) and `any P` (existential).
 */
object GenericsDemo : Demo {
    override val id = "D08"
    override val title = "Generic"
    override val counterpart = "generic, some / any"

    open class AdFormat(val name: String)
    class Banner : AdFormat("banner")
    class Interstitial : AdFormat("interstitial")

    class Slot<T : AdFormat>(val format: T) {          // upper bound
        fun describe() = "Slot<${format.name}>"
    }

    inline fun <reified T : AdFormat> List<AdFormat>.countOf(): Int = count { it is T }

    fun <T : Comparable<T>> maxOfTwo(a: T, b: T): T = if (a >= b) a else b

    // Producer: only returns T → declared out
    interface Source<out T> { fun next(): T }

    override fun run(): List<String> = buildList {
        val banners: List<Banner> = listOf(Banner(), Banner())
        val formats: List<AdFormat> = banners + Interstitial()      // OK because List<out E>
        add("List<Banner> assigned to List<AdFormat>: OK (covariant)")
        // val m: MutableList<AdFormat> = mutableListOf<Banner>()   // ❌ MutableList invariant

        add("formats.countOf<Banner>() = ${formats.countOf<Banner>()}   // reified")
        add("formats.countOf<Interstitial>() = ${formats.countOf<Interstitial>()}")

        add("maxOfTwo(3, 7) = ${maxOfTwo(3, 7)}, maxOfTwo(\"a\", \"b\") = ${maxOfTwo("a", "b")}")
        add(Slot(Banner()).describe())

        val strings: List<Any> = listOf("a")
        val ints: List<Any> = listOf(1)
        add("Type erasure: listOf(\"a\").javaClass == listOf(1).javaClass → ${strings.javaClass == ints.javaClass}")

        val bannerSource: Source<Banner> = object : Source<Banner> { override fun next() = Banner() }
        val anySource: Source<AdFormat> = bannerSource             // OK thanks to out T
        add("Source<Banner> → Source<AdFormat>: ${anySource.next().name}")
    }
}
