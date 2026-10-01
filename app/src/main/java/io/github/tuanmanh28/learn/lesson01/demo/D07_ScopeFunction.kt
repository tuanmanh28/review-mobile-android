package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 7. Scope function
 *
 *  fn     | object in block    | returns         | use when
 *  -------|--------------------|-----------------|------------------------------------------
 *  let    | it                 | lambda result   | handling a nullable value: x?.let { ... }
 *  run    | this               | lambda result   | computing on an object, returning a result
 *  with   | this (argument)    | lambda result   | calling several functions on the same object
 *  apply  | this               | the object      | configuring an object (builder)
 *  also   | it                 | the object      | side effects: logging, validation
 *
 * They are all inline functions in the stdlib — no lambda object is created at runtime.
 * Swift has no built-in equivalent; the closest are: Optional.map / if let / initializer closures.
 */
object ScopeFunctionDemo : Demo {
    override val id = "D07"
    override val title = "Scope function"
    override val counterpart = "(none) → Optional.map, if let, closure"

    class AdRequest {
        var unitId: String = ""
        var keywords: MutableList<String> = mutableListOf()
        var testMode: Boolean = false
        override fun toString() = "AdRequest(unitId=$unitId, keywords=$keywords, testMode=$testMode)"
    }

    override fun run(): List<String> = buildList {
        val log = mutableListOf<String>()

        val request = AdRequest().apply {       // this = AdRequest, returns AdRequest
            unitId = "banner_home"
            keywords += "puzzle"
            testMode = true
        }.also { log += "also → created $it" }   // it = AdRequest, returns AdRequest
        add("apply: $request")

        val rawKeyword: String? = "  Casual Game "
        val normalized = rawKeyword?.let { it.trim().lowercase() }   // it = String, returns String
        add("let: \"$rawKeyword\" → \"$normalized\"")

        val summary = with(request) { "$unitId / ${keywords.size} keyword / test=$testMode" }
        add("with: $summary")

        val isValid = request.run { unitId.isNotBlank() && keywords.isNotEmpty() }
        add("run: isValid = $isValid")

        addAll(log)
    }
}
