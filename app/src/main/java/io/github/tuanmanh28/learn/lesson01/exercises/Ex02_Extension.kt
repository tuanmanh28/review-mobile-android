package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 2 — Extension function

/**
 * 2.1 Shorten a view count:
 *   999 → "999", 1000 → "1K", 1500 → "1.5K", 12300 → "12.3K", 2000000 → "2M", 3400000 → "3.4M"
 * Round to 1 decimal place and drop ".0". Hint: don't use String.format (a device with a VN locale will output "1,5").
 */
fun Int.toCompactString(): String = TODO("Ex02.1")

/** 2.2 A valid ad unit id looks like: ca-app-pub-<16 digits>/<10 digits> */
fun String.isValidAdUnitId(): Boolean = TODO("Ex02.2")

/** 2.3 Extension on a NULLABLE type: null or blank → "-", otherwise return the string itself. */
fun String?.orDash(): String = TODO("Ex02.3")
