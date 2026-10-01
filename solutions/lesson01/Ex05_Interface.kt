package io.github.tuanmanh28.learn.lesson01.exercises

class FakeNetwork(
    override val name: String,
    override val priority: Int,
    private val result: AdLoadResult,
) : AdNetwork {
    val requestedUnits = mutableListOf<String>()

    override fun load(unitId: String): AdLoadResult {
        requestedUnits += unitId
        return result
    }
}

fun waterfall(networks: List<AdNetwork>, unitId: String): AdLoadResult {
    for (network in networks.sortedByDescending { it.priority }) {
        val result = network.load(unitId)
        if (result is AdLoadResult.Loaded) return result
    }
    return AdLoadResult.NoFill
}
