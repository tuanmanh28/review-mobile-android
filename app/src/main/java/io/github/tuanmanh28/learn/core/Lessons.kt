package io.github.tuanmanh28.learn.core

import io.github.tuanmanh28.learn.lesson01.Lesson01
import io.github.tuanmanh28.learn.lesson02.Lesson02

/** Lessons shown in the app. Each new lesson only needs 1 line here. */
object Lessons {
    val all: List<Lesson> = listOf(
        Lesson01.lesson,
        Lesson02.lesson,
    )
}
