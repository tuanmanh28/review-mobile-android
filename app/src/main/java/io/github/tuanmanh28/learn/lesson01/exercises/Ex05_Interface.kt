package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 5 — interface (see AdNetwork in Models.kt)

/**
 * 5.1 Finish FakeNetwork: implement AdNetwork.
 *  - name and priority come from the constructor
 *  - load() always returns `result`, and also adds unitId to `requestedUnits`
 */
class FakeNetwork(
    name: String,
    priority: Int,
    private val result: AdLoadResult,
) : AdNetwork {
    val requestedUnits = mutableListOf<String>()

    override val name: String get() = TODO("Ex05.1 name")        // hint: move `override val` into the constructor
    override val priority: Int get() = TODO("Ex05.1 priority")
    override fun load(unitId: String): AdLoadResult = TODO("Ex05.1 load")
}

/**
 * 5.2 Waterfall mediation:
 *  - try the networks one by one in DESCENDING priority order
 *  - on the first Loaded → return it right away (later networks must not be called)
 *  - if no network returns Loaded → return NoFill
 */
fun waterfall(networks: List<AdNetwork>, unitId: String): AdLoadResult = TODO("Ex05.2")
