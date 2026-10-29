package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.Reference
import java.lang.ref.SoftReference

// Exercise 4 — Context leak + SoftReference cache

/**
 * 4.1 The ad SDK manager lives for the whole process (one instance, created on the first screen).
 * It is often initialised with `AdManager(this)` from an Activity → if it keeps that Activity, it leaks it forever.
 * Keep only the APPLICATION context: it lives as long as the process anyway, so holding it costs nothing.
 */
class AdManager(context: Context) {
    val context: Context = TODO("Ex04.1")
}

/**
 * 4.2 Cache of decoded creatives that the system may drop under memory pressure.
 *  - put(unitId, creative): store [newRef] (creative) under unitId
 *  - get(unitId): the creative if its reference has not been cleared yet; if it was cleared →
 *    remove the entry and return null
 *  - size: number of entries currently stored
 *
 * [newRef] creates a SoftReference by default; the tests pass their own factory so they can "simulate"
 * memory pressure by calling clear() on the references.
 * Hint: `private val map = HashMap<String, Reference<AdCreative>>()`
 */
class CreativeCache(
    private val newRef: (AdCreative) -> Reference<AdCreative> = { SoftReference(it) },
) {
    fun put(unitId: String, creative: AdCreative) {
        TODO("Ex04.2 put")
    }

    fun get(unitId: String): AdCreative? = TODO("Ex04.2 get")

    val size: Int get() = TODO("Ex04.2 size")
}
