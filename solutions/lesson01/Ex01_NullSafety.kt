package io.github.tuanmanh28.learn.lesson01.exercises

data class AdUser(val name: String?, val country: String?)

fun displayName(user: AdUser?): String = user?.name?.takeIf { it.isNotBlank() } ?: "Guest"

fun parseBid(raw: String?): Double? = raw?.toDoubleOrNull()?.takeIf { it >= 0 }

fun countryOrDefault(user: AdUser?): String = user?.country ?: "VN"
