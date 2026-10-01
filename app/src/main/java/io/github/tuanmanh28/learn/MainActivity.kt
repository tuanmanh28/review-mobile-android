package io.github.tuanmanh28.learn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tuanmanh28.learn.core.Demo
import io.github.tuanmanh28.learn.core.Lesson
import io.github.tuanmanh28.learn.core.Lessons

/**
 * One shared app for the whole roadmap: the first screen lists the lessons; tap a lesson to view and run its demos.
 * A new lesson only needs to be added to Lessons.all — no need to touch this file.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var selected by rememberSaveable { mutableStateOf<Int?>(null) }
                val lesson = Lessons.all.firstOrNull { it.number == selected }
                BackHandler(enabled = lesson != null) { selected = null }
                Scaffold { padding ->
                    Column(Modifier.fillMaxSize().padding(padding)) {
                        if (lesson == null) {
                            LessonList(onOpen = { selected = it.number })
                        } else {
                            LessonScreen(lesson, onBack = { selected = null })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonList(onOpen: (Lesson) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text("Mobile Learning · Android", style = MaterialTheme.typography.headlineSmall) }
        items(Lessons.all, key = { it.number }) { lesson ->
            Card(Modifier.fillMaxWidth().clickable { onOpen(lesson) }) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Lesson ${lesson.number} · ${lesson.title}", style = MaterialTheme.typography.titleMedium)
                    Text(lesson.summary, style = MaterialTheme.typography.bodySmall)
                    Text("${lesson.demos.size} demo", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun LessonScreen(lesson: Lesson, onBack: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TextButton(onClick = onBack) { Text("← All lessons") }
            Text("Lesson ${lesson.number} · ${lesson.title}", style = MaterialTheme.typography.headlineSmall)
        }
        items(lesson.demos, key = { it.id }) { DemoCard(it) }
    }
}

@Composable
fun DemoCard(demo: Demo) {
    var output by remember { mutableStateOf<List<String>?>(null) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { output = if (output == null) demo.run() else null },
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("${demo.id} · ${demo.title}", style = MaterialTheme.typography.titleMedium)
            Text("iOS: ${demo.counterpart}", style = MaterialTheme.typography.bodySmall)
            output?.forEach { line ->
                Text(line, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonListPreview() {
    MaterialTheme { LessonList(onOpen = {}) }
}
