package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.core.Demo
import io.github.tuanmanh28.learn.lesson01.demo.*
import org.junit.Test

/**
 * Run each demo on the JVM (no emulator needed). Click ▶ next to each function → see the output in the Run tab.
 * Tip: set a breakpoint in the demo file, then choose "Debug" to inspect each variable's value.
 */
class DemoTest {
    private fun show(demo: Demo) {
        println("── ${demo.id} · ${demo.title}  (iOS: ${demo.counterpart}) ──")
        demo.run().forEach { println("  $it") }
    }

    @Test fun d01_nullSafety() = show(NullSafetyDemo)
    @Test fun d02_extension() = show(ExtensionDemo)
    @Test fun d03_dataClass() = show(DataClassDemo)
    @Test fun d04_sealed() = show(SealedDemo)
    @Test fun d05_interface() = show(InterfaceDemo)
    @Test fun d06_delegation() = show(DelegationDemo)
    @Test fun d07_scopeFunction() = show(ScopeFunctionDemo)
    @Test fun d08_generics() = show(GenericsDemo)
}
