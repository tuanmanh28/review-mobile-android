package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.core.Demo
import io.github.tuanmanh28.learn.lesson02.demo.*
import org.junit.Test

/**
 * Run each demo on the JVM (no emulator needed). Click ▶ next to each function → see the output in the Run tab.
 * Tip: set a breakpoint in the demo file, then choose "Debug" to inspect each variable's value.
 * Note: GC results ("collected" / "still alive") depend on the runtime; the JVM here and ART on a device may differ.
 */
class DemoTest {
    private fun show(demo: Demo) {
        println("── ${demo.id} · ${demo.title}  (iOS: ${demo.counterpart}) ──")
        demo.run().forEach { println("  $it") }
    }

    @Test fun d01_gcRoots() = show(GcRootsDemo)
    @Test fun d02_references() = show(ReferencesDemo)
    @Test fun d03_cycles() = show(CyclesDemo)
    @Test fun d04_contextLeak() = show(ContextLeakDemo)
    @Test fun d05_generationalGc() = show(GenerationalGcDemo)
    @Test fun d06_leakCanary() = show(LeakCanaryDemo)
}
