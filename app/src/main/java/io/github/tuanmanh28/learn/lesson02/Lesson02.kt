package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.core.Lesson
import io.github.tuanmanh28.learn.lesson02.demo.*

object Lesson02 {
    val lesson = Lesson(
        number = 2,
        title = "Memory management · GC ↔ ARC",
        summary = "GC roots, weak/soft reference, cycles, Context leak, generational GC & pause, LeakCanary",
        demos = listOf(
            GcRootsDemo,
            ReferencesDemo,
            CyclesDemo,
            ContextLeakDemo,
            GenerationalGcDemo,
            LeakCanaryDemo,
        ),
    )
}
