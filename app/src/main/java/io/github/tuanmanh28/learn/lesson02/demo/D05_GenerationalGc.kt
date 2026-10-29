package io.github.tuanmanh28.learn.lesson02.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 5. Generational GC & GC pauses
 *
 * "Most objects die young": temporary strings, iterators, boxed numbers, lambdas…
 * So the heap is split:
 *  - Young generation: new objects. Collected often and cheaply (minor GC): only live objects are copied out.
 *  - Old generation: objects that survived several GCs (caches, singletons, the Activity while it is open).
 *    Collected rarely; a major/full GC is more expensive.
 * ART (Android) uses a concurrent copying collector; recent versions also collect the young generation separately.
 * Most of the work runs on a background thread, but every GC still needs short "pauses" and steals CPU.
 *
 * Why it matters for UI: one frame is 16 ms (60 Hz) or 8 ms (120 Hz). Allocating in a hot loop —
 * `onDraw`, `onBindViewHolder`, a scroll listener, an animation tick — creates "allocation churn":
 * many minor GCs → dropped frames (jank).
 *  - Pre-allocate objects (Paint, Rect, Path, StringBuilder) as fields, reuse them in onDraw.
 *  - Avoid boxing (List<Int> in a hot path), string concatenation per frame, `forEach` lambdas over huge lists.
 *  - Measure: Android Studio ▸ Profiler ▸ Memory (Record allocations), look for the GC events and the sawtooth.
 *
 * Swift has no generations and no GC pauses: ARC frees objects immediately — but retain/release has its own cost.
 * The closest tool is `autoreleasepool { }`, which drains temporary (Obj-C) objects inside a loop
 * instead of at the end of the run-loop iteration.
 */
object GenerationalGcDemo : Demo {
    override val id = "D05"
    override val title = "generational GC & GC pause"
    override val counterpart = "autorelease pool"

    class Paint(var color: Int = 0)                // pretend this is android.graphics.Paint

    /** ❌ allocates on every frame. */
    class ChurnyChartView {
        fun onDraw(ecpms: DoubleArray): Int {
            var drawn = 0
            for (ecpm in ecpms) {
                val paint = Paint(color = 0xFF00FF00.toInt())     // new object per bar, per frame
                val label = "eCPM: " + String.format("%.2f", ecpm) // new String + Object[] + boxed Double
                drawn += label.length + paint.color.countOneBits()
            }
            return drawn
        }
    }

    /** ✅ allocates once, reuses every frame. */
    class ReusingChartView {
        private val paint = Paint(color = 0xFF00FF00.toInt())
        private val label = StringBuilder(16)

        fun onDraw(ecpms: DoubleArray): Int {
            var drawn = 0
            for (ecpm in ecpms) {
                label.setLength(0)
                val cents = (ecpm * 100).toInt()           // primitive math, no boxing
                label.append("eCPM: ").append(cents / 100).append('.')
                if (cents % 100 < 10) label.append('0')
                label.append(cents % 100)
                drawn += label.length + paint.color.countOneBits()
            }
            return drawn
        }
    }

    private fun usedKb(): Long = Runtime.getRuntime().let { (it.totalMemory() - it.freeMemory()) / 1024 }

    /** Runs [frames] frames, returns elapsed ms. Rough numbers only: no JIT warm-up control, no isolation. */
    private inline fun measure(frames: Int, frame: () -> Int): Pair<Long, Int> {
        var sink = 0
        val start = System.nanoTime()
        repeat(frames) { sink += frame() }
        return (System.nanoTime() - start) / 1_000_000 to sink
    }

    override fun run(): List<String> = buildList {
        val ecpms = DoubleArray(200) { 0.5 + it * 0.01 }
        val churny = ChurnyChartView()
        val reusing = ReusingChartView()
        val frames = 300

        // Warm up both a bit so the JIT does not dominate the comparison.
        repeat(20) { churny.onDraw(ecpms); reusing.onDraw(ecpms) }

        System.gc()
        val before = usedKb()
        val (churnMs, _) = measure(frames) { churny.onDraw(ecpms) }
        val afterChurn = usedKb()
        add("allocating in onDraw: $frames frames in ${churnMs} ms, used heap ${before} KB → ${afterChurn} KB")
        add("  ≈ ${frames * ecpms.size * 3} short-lived objects → young-gen fills up → many minor GCs")

        System.gc()
        val before2 = usedKb()
        val (reuseMs, _) = measure(frames) { reusing.onDraw(ecpms) }
        val afterReuse = usedKb()
        add("reusing Paint + StringBuilder: $frames frames in ${reuseMs} ms, used heap ${before2} KB → ${afterReuse} KB")

        add("(Numbers are rough. If 'used heap' went DOWN, a GC ran in the middle — that's the sawtooth.)")
        add("Real measurement: Android Studio ▸ Profiler ▸ Memory ▸ Record allocations / look for GC events.")
        add("Logcat on a device also shows lines like: 'Background young concurrent copying GC freed …, paused …'")
    }
}
