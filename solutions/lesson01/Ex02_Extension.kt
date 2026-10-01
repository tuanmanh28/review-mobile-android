package io.github.tuanmanh28.learn.lesson01.exercises

fun Int.toCompactString(): String {
    fun format(value: Double, suffix: String): String {
        val rounded = Math.round(value * 10) / 10.0
        return rounded.toString().removeSuffix(".0") + suffix
    }
    return when {
        this >= 1_000_000 -> format(this / 1_000_000.0, "M")
        this >= 1_000 -> format(this / 1_000.0, "K")
        else -> toString()
    }
}

private val adUnitRegex = Regex("""ca-app-pub-\d{16}/\d{10}""")

fun String.isValidAdUnitId(): Boolean = adUnitRegex.matches(this)   // matches() = matches the WHOLE string

fun String?.orDash(): String = if (isNullOrBlank()) "-" else this
