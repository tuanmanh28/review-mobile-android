package io.github.tuanmanh28.learn.lesson01.exercises

data class AdConfig(
    val unitId: String,
    val timeoutMs: Long = 3_000,
    val refreshSec: Int = 30,
    val testMode: Boolean = false,
)

fun AdConfig.forTesting(): AdConfig = copy(testMode = true, timeoutMs = 10_000)

fun mergeUnique(configs: List<AdConfig>): List<AdConfig> = configs.distinct()

fun describe(config: AdConfig): String {
    val (unitId, timeoutMs) = config
    return "unit=$unitId, timeout=${timeoutMs}ms"
}
