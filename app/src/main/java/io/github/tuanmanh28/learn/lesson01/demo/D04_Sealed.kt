package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 4. sealed class / sealed interface
 *
 * "Closed set of subtypes": every subtype must be declared in the same module + package.
 * That way `when` knows every case → no `else` needed, and when you add a new case
 * the compiler reports an error everywhere it isn't handled yet. This is how you model UI state / results.
 *
 * Swift equivalent: an enum with associated values.
 */
object SealedDemo : Demo {
    override val id = "D04"
    override val title = "sealed class"
    override val counterpart = "enum + associated value"

    sealed interface AdLoadResult {
        data class Loaded(val network: String, val ecpm: Double) : AdLoadResult
        data object NoFill : AdLoadResult
        data class Failed(val code: Int, val message: String) : AdLoadResult
    }

    fun render(result: AdLoadResult): String = when (result) {   // no else
        is AdLoadResult.Loaded -> "✅ ${result.network} eCPM=${result.ecpm}"   // smart cast → access the fields
        AdLoadResult.NoFill -> "∅ no fill"
        is AdLoadResult.Failed -> "❌ [${result.code}] ${result.message}"
    }

    override fun run(): List<String> {
        val results = listOf(
            AdLoadResult.Loaded("AdMob", 1.25),
            AdLoadResult.NoFill,
            AdLoadResult.Failed(2, "Network error"),
        )
        return results.map { render(it) } + listOf(
            "Try it: add the case `data object Timeout : AdLoadResult` → render() immediately fails to compile",
            "NoFill is a data object → there is only ONE instance: ${AdLoadResult.NoFill === AdLoadResult.NoFill}",
        )
    }
}
