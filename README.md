# Review Mobile — Android

> Part of [review-mobile](https://github.com/tuanmanh28/review-mobile) · Android: [review-mobile-android](https://github.com/tuanmanh28/review-mobile-android) · iOS: [review-mobile-ios](https://github.com/tuanmanh28/review-mobile-ios)

Android project for the mobile learning roadmap (Kotlin).

```
app/src/main/java/io/github/tuanmanh28/learn/
├── MainActivity.kt          lesson list screen → tap a lesson → run each demo
├── core/Lessons.kt          LESSON REGISTRY (add 1 line for each new lesson)
└── lesson01/                Lesson 1: demo/ + exercises/ + Lesson01.kt
app/src/test/java/io/github/tuanmanh28/learn/lesson01/   grading tests + DemoTest
solutions/lesson01/          solutions (not built)
```

## First run

1. Android Studio ▸ File ▸ Open… ▸ select this repo folder. If asked for a Gradle JVM, pick **JVM 21**.
2. Run the app: select the `app` configuration + an emulator ▸ ▶.
3. Run demos/tests without an emulator: open `app/src/test/.../lesson01/DemoTest.kt` ▸ ▶ next to each function.
   Run a whole lesson: right-click the `lesson01` package in the test folder ▸ *Run 'Tests in lesson01'*.

## Working through a lesson

1. Run the lesson's demos (in the app or `DemoTest`).
2. Fill in the `TODO`s in `exercises/`.
3. Run the lesson's tests until they're green. Only look at `solutions/` after being stuck for 15 minutes.

## Adding a new lesson (e.g. Lesson 2)

- Code: create a `lesson02/` package (demo, exercises, `Lesson02.kt`)
- Register: add `Lesson02.lesson` to `core/Lessons.kt`
- Tests: `app/src/test/.../lesson02/`
- Solutions: `solutions/lesson02/`
