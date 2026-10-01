package io.github.tuanmanh28.learn.lesson01.exercises

fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null

inline fun <reified T : AdEvent> List<AdEvent>.countOf(): Int = count { it is T }

class TtlCache<K, V>(
    private val ttlMs: Long,
    private val clock: () -> Long,
) {
    private data class Entry<V>(val value: V, val storedAt: Long)

    private val entries = mutableMapOf<K, Entry<V>>()

    fun put(key: K, value: V) {
        entries[key] = Entry(value, clock())
    }

    fun get(key: K): V? {
        val entry = entries[key] ?: return null
        if (clock() - entry.storedAt >= ttlMs) {
            entries.remove(key)
            return null
        }
        return entry.value
    }

    val size: Int get() = entries.size
}
