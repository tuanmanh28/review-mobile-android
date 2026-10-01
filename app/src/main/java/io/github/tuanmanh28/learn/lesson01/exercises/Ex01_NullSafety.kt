package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 1 — Nullable type
// Run the tests: app/src/test/.../Ex01NullSafetyTest.kt  (click ▶ next to the class name)
// Rules: do NOT use !!  and no if/else — only ?.  ?:  let  takeIf

data class AdUser(val name: String?, val country: String?)

/** 1.1 Return name if it is not null and not empty/blank; otherwise "Guest". */
fun displayName(user: AdUser?): String = TODO("Ex01.1")

/** 1.2 Parse the bid price returned by the server, e.g. "0.35" → 0.35. Null, malformed, or negative → null. */
fun parseBid(raw: String?): Double? = TODO("Ex01.2")

/** 1.3 Return country; if user is null OR country is null → "VN". Write it as exactly 1 expression. */
fun countryOrDefault(user: AdUser?): String = TODO("Ex01.3")
