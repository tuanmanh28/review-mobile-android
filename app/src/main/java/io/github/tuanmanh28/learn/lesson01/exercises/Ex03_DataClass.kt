package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 3 — data class

data class AdConfig(
    val unitId: String,
    val timeoutMs: Long = 3_000,
    val refreshSec: Int = 30,
    val testMode: Boolean = false,
)

/** 3.1 Return a COPY with testMode = true and timeoutMs = 10_000. Don't modify the original object. Hint: copy(). */
fun AdConfig.forTesting(): AdConfig = TODO("Ex03.1")

/** 3.2 Remove configs with duplicate CONTENT, keeping first-occurrence order. Hint: equals/hashCode come for free. */
fun mergeUnique(configs: List<AdConfig>): List<AdConfig> = TODO("Ex03.2")

/** 3.3 Return "unit=<unitId>, timeout=<timeoutMs>ms". You must use destructuring: val (a, b) = config */
fun describe(config: AdConfig): String = TODO("Ex03.3")
