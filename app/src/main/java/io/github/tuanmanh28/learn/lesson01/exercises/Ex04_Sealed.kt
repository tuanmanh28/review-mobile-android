package io.github.tuanmanh28.learn.lesson01.exercises

// Exercise 4 — sealed interface (see AdEvent in Models.kt)
// Rules: use `when` with NO else branch.

/** 4.1 Impression → "ad_impression", Click → "ad_click", LoadFailed → "ad_load_failed", Closed → "ad_closed" */
fun AdEvent.logName(): String = TODO("Ex04.1")

/** 4.2 Total USD revenue of all Impressions (revenueMicros / 1_000_000). */
fun totalRevenueUsd(events: List<AdEvent>): Double = TODO("Ex04.2")

/** 4.3 Retry only for a LoadFailed with code 0 (internal) or 2 (network). */
fun shouldRetry(event: AdEvent): Boolean = TODO("Ex04.3")
