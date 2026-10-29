package io.github.tuanmanh28.learn.lesson02.exercises

import java.lang.ref.Reference
import java.lang.ref.SoftReference

class AdManager(context: Context) {
    val context: Context = context.applicationContext
}

class CreativeCache(
    private val newRef: (AdCreative) -> Reference<AdCreative> = { SoftReference(it) },
) {
    private val map = HashMap<String, Reference<AdCreative>>()

    fun put(unitId: String, creative: AdCreative) {
        map[unitId] = newRef(creative)
    }

    fun get(unitId: String): AdCreative? {
        val ref = map[unitId] ?: return null
        val creative = ref.get()
        if (creative == null) map.remove(unitId)
        return creative
    }

    val size: Int get() = map.size
}
