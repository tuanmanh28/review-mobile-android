package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.lesson02.GcTestSupport.awaitCleared
import io.github.tuanmanh28.learn.lesson02.GcTestSupport.gcRounds
import io.github.tuanmanh28.learn.lesson02.exercises.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class Ex02LambdaCaptureTest {
    private val app = Application()

    private fun openAndCloseScreen(controller: BannerController, bus: AdEventBus): WeakReference<Activity> {
        val activity = Activity("Home", app)
        controller.attach(activity)
        bus.post("impression")
        assertEquals(listOf("banner: impression"), activity.shown)
        activity.onDestroy()
        controller.detach()
        return WeakReference(activity)
    }

    private fun registerWeakly(bus: AdEventBus): WeakReference<Activity> {
        val activity = Activity("Home", app)
        bus.register("home", weakCallback(activity) { act, event -> act.shown += event })
        activity.onDestroy()
        return WeakReference(activity)
    }

    @Test fun `2_1 attach delivers events, detach unregisters`() {
        val bus = AdEventBus()
        val controller = BannerController(bus)
        val activity = Activity("Home", app)
        controller.attach(activity)
        assertEquals(1, bus.size)
        bus.post("click")
        assertEquals(listOf("banner: click"), activity.shown)
        controller.detach()
        assertEquals(0, bus.size)
        bus.post("closed")
        assertEquals(listOf("banner: click"), activity.shown)
    }

    @Test fun `2_1 activity is collected after detach`() {
        val bus = AdEventBus()
        val controller = BannerController(bus)      // stays alive for the whole test, like an SDK singleton
        val ref = openAndCloseScreen(controller, bus)
        assertTrue("the Activity leaked: bus or controller still references it", awaitCleared(ref))
        Reference.reachabilityFence(controller)
    }

    @Test fun `2_2 weakCallback calls block while the activity is alive`() {
        val activity = Activity("Home", app)
        val callback = weakCallback(activity) { act, event -> act.shown += "got $event" }
        callback("impression")
        gcRounds()
        callback("click")
        assertEquals(listOf("got impression", "got click"), activity.shown)
    }

    @Test fun `2_2 weakCallback does not keep the activity alive`() {
        val bus = AdEventBus()
        val ref = registerWeakly(bus)
        assertTrue("the Activity leaked: the callback captured it strongly", awaitCleared(ref))
        bus.post("impression")      // must not crash once the activity is gone
        assertEquals(1, bus.size)
    }
}
