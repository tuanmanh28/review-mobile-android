package io.github.tuanmanh28.learn.lesson01.exercises

fun AdEvent.logName(): String = when (this) {
    is AdEvent.Impression -> "ad_impression"
    is AdEvent.Click -> "ad_click"
    is AdEvent.LoadFailed -> "ad_load_failed"
    AdEvent.Closed -> "ad_closed"
}

fun totalRevenueUsd(events: List<AdEvent>): Double =
    events.filterIsInstance<AdEvent.Impression>().sumOf { it.revenueMicros } / 1_000_000.0

fun shouldRetry(event: AdEvent): Boolean = event is AdEvent.LoadFailed && event.code in setOf(0, 2)
