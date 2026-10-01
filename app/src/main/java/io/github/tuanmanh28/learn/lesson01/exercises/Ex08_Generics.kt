package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 8 — Generics

/** 8.1 The second element, or null if the list has fewer than 2 elements. */
fun <T> List<T>.secondOrNull(): T? = TODO("Ex08.1")

/** 8.2 Count the events of type T. You need inline + reified to be able to use `is T`. */
inline fun <reified T : AdEvent> List<AdEvent>.countOf(): Int = TODO("Ex08.2")

/**
 * 8.3 Cache with an expiry time (TTL).
 *  - put(key, value): store it together with the current time = clock()
 *  - get(key): return value if (clock() - put time) < ttlMs; if expired, remove it and return null
 * `clock` is passed in so the tests can control time.
 */
class TtlCache<K, V>(
    private val ttlMs: Long,
    private val clock: () -> Long,
) {
    fun put(key: K, value: V) {
        TODO("Ex08.3 put")
    }

    fun get(key: K): V? = TODO("Ex08.3 get")

    val size: Int get() = TODO("Ex08.3 size")
}
