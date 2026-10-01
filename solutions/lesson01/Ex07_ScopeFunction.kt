package io.github.tuanmanh28.learn.lesson01.exercises

class AdRequestBuilder {
    var unitId: String = ""
    val keywords: MutableList<String> = mutableListOf()
    var testMode: Boolean = false
}

fun buildRequest(unitId: String, keywords: List<String>): AdRequestBuilder = AdRequestBuilder().apply {
    this.unitId = unitId          // this. is needed because the same-named parameter shadows the property
    this.keywords += keywords
    testMode = true
}

fun normalizeKeyword(raw: String?): String? =
    raw?.let { it.trim().lowercase() }?.takeIf { it.isNotEmpty() }

fun createAndLog(unitId: String, log: MutableList<String>): AdConfig =
    AdConfig(unitId).also { log += "created:${it.unitId}" }
