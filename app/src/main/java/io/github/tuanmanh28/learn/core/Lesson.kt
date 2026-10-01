package io.github.tuanmanh28.learn.core

/** A runnable example: returns output lines to show on screen or print when running tests. */
interface Demo {
    val id: String
    val title: String
    /** The equivalent concept on iOS */
    val counterpart: String
    fun run(): List<String>
}

/** A lesson in the roadmap. To add a new lesson: create a lessonNN package, then register it in [Lessons.all]. */
data class Lesson(
    val number: Int,
    val title: String,
    val summary: String,
    val demos: List<Demo>,
)
