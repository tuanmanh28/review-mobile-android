package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.lesson02.GcTestSupport.awaitCleared
import io.github.tuanmanh28.learn.lesson02.exercises.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

class Ex04ContextLeakTest {
    private val app = Application()

    private fun initFromScreen(): Pair<AdManager, WeakReference<Activity>> {
        val splash = Activity("Splash", app)
        val manager = AdManager(splash)       // the classic mistake: passing `this` from an Activity
        splash.onDestroy()
        return manager to WeakReference(splash)
    }

    @Test fun `4_1 AdManager keeps the application context`() {
        val manager = AdManager(Activity("Splash", app))
        assertSame(app, manager.context)
        assertSame(app, AdManager(app).context)
    }

    @Test fun `4_1 AdManager does not leak the Activity`() {
        val (manager, ref) = initFromScreen()
        assertTrue("the Activity leaked: AdManager still holds it", awaitCleared(ref))
        Reference.reachabilityFence(manager)
    }

    /** A factory that remembers every reference it creates, so the test can clear them = simulated memory pressure. */
    private class RecordingFactory : (AdCreative) -> Reference<AdCreative> {
        val created = mutableListOf<Reference<AdCreative>>()
        override fun invoke(creative: AdCreative): Reference<AdCreative> =
            SoftReference(creative).also { created += it }
    }

    @Test fun `4_2 put and get`() {
        val cache = CreativeCache()
        val banner = AdCreative("banner_home")
        cache.put("banner_home", banner)
        assertSame(banner, cache.get("banner_home"))
        assertNull(cache.get("inter_level_end"))
        assertEquals(1, cache.size)
    }

    @Test fun `4_2 cleared entries return null and are removed`() {
        val factory = RecordingFactory()
        val cache = CreativeCache(factory)
        val banner = AdCreative("banner_home")
        val inter = AdCreative("inter_level_end")
        cache.put("banner_home", banner)
        cache.put("inter_level_end", inter)
        assertEquals(2, factory.created.size)

        factory.created.first().clear()      // the GC dropped the banner under memory pressure

        assertNull(cache.get("banner_home"))
        assertEquals("the cleared entry must be removed", 1, cache.size)
        assertSame(inter, cache.get("inter_level_end"))
    }
}
