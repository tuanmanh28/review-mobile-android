package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 7 — Scope function
// The tests only check the result, but use the suggested function anyway for practice.

class AdRequestBuilder {
    var unitId: String = ""
    val keywords: MutableList<String> = mutableListOf()
    var testMode: Boolean = false
}

/** 7.1 Create a builder with unitId, add all keywords, set testMode = true. Use apply { }. */
fun buildRequest(unitId: String, keywords: List<String>): AdRequestBuilder = TODO("Ex07.1")

/** 7.2 "  Casual Game " → "casual game"; null or all whitespace → null. Use ?.let or ?.takeIf. */
fun normalizeKeyword(raw: String?): String? = TODO("Ex07.2")

/** 7.3 Create AdConfig(unitId), add "created:<unitId>" to log, return the config. Use also { }. */
fun createAndLog(unitId: String, log: MutableList<String>): AdConfig = TODO("Ex07.3")
