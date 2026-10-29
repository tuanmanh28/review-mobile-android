package io.github.tuanmanh28.learn.lesson02

import io.github.tuanmanh28.learn.lesson02.GcTestSupport.awaitCleared
import io.github.tuanmanh28.learn.lesson02.GcTestSupport.gcRounds
import io.github.tuanmanh28.learn.lesson02.exercises.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class Ex01WeakListenerTest {
    /** A screen that listens to the loader. */
    class HomeActivity(app: Application) : Activity("Home", app), AdLoadListener {
        override fun onAdLoaded(unitId: String, ecpm: Double) {
            shown += "loaded $unitId @ $ecpm"
        }

        override fun onAdFailed(unitId: String, code: Int) {
            shown += "failed $unitId ($code)"
        }
    }

    private val app = Application()

    /** Creates a screen in a separate function, so only the loader (and the WeakReference) can see it. */
    private fun attachTemporaryScreen(loader: AdLoader): WeakReference<HomeActivity> {
        val screen = HomeActivity(app)
        loader.listener = screen
        screen.onDestroy()
        return WeakReference(screen)
    }

    @Test fun `1_1 listener get and set`() {
        val loader = AdLoader()
        val screen = HomeActivity(app)
        assertNull(loader.listener)
        loader.listener = screen
        assertSame(screen, loader.listener)
        loader.listener = null
        assertNull(loader.listener)
    }

    @Test fun `1_1 loader does not keep a destroyed screen alive`() {
        val loader = AdLoader()
        val ref = attachTemporaryScreen(loader)
        assertTrue("the Activity leaked: AdLoader still holds it strongly", awaitCleared(ref))
        assertNull(loader.listener)
    }

    @Test fun `1_1 a live screen is not collected`() {
        val loader = AdLoader()
        val screen = HomeActivity(app)
        loader.listener = screen
        gcRounds()
        assertSame(screen, loader.listener)
        Reference.reachabilityFence(screen)
    }

    @Test fun `1_2 deliverLoaded while the screen is alive`() {
        val loader = AdLoader()
        assertFalse("no listener → false", loader.deliverLoaded("banner_home", 1.0))
        val screen = HomeActivity(app)
        loader.listener = screen
        assertTrue(loader.deliverLoaded("banner_home", 1.25))
        assertEquals(listOf("loaded banner_home @ 1.25"), screen.shown)
    }

    @Test fun `1_2 deliverLoaded after the screen was collected`() {
        val loader = AdLoader()
        val ref = attachTemporaryScreen(loader)
        assertTrue("the Activity leaked: AdLoader still holds it strongly", awaitCleared(ref))
        assertFalse(loader.deliverLoaded("banner_home", 1.25))
    }
}
