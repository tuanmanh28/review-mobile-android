package io.github.tuanmanh28.learn.sandbox

import io.github.tuanmanh28.learn.lesson01.demo.*
import org.junit.Test

/**
 * A scratchpad for trying out statements — no emulator needed.
 *
 * 1. Write code in the try1 function (every class in the project is available).
 * 2. Click ▶ next to the function name → the output shows up in the Run tab.
 * 3. To step line by line: click the left gutter to set a breakpoint → ▶ ▸ Debug → F8 (Step Over) each line,
 *    check values in the Variables tab, or use ⌥F8 (Evaluate Expression) to type any expression.
 */
class SandboxTest {
    @Test
    fun try1() {
        val tag: String? = null
        println(tag?.length ?: 0)

        val config = DataClassDemo.AdConfig("banner_home")
        val copy = config.copy(timeoutMs = 5_000)
        println(copy)
        println(config === copy)
    }

    @Test
    fun try2() {
        // Try more things here
    }
}
