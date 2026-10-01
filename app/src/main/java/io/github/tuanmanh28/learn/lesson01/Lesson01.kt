package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.core.Lesson
import io.github.tuanmanh28.learn.lesson01.demo.*

object Lesson01 {
    val lesson = Lesson(
        number = 1,
        title = "Language · Kotlin ↔ Swift",
        summary = "nullable, extension, data class, sealed, interface, delegation, scope function, generic",
        demos = listOf(
            NullSafetyDemo,
            ExtensionDemo,
            DataClassDemo,
            SealedDemo,
            InterfaceDemo,
            DelegationDemo,
            ScopeFunctionDemo,
            GenericsDemo,
        ),
    )
}
