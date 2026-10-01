package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex05InterfaceTest {
    @Test fun `5_1 FakeNetwork`() {
        val net = FakeNetwork("AdMob", 10, AdLoadResult.NoFill)
        assertEquals("AdMob", net.name)
        assertEquals(10, net.priority)
        assertEquals(AdLoadResult.NoFill, net.load("home"))
        assertEquals(listOf("home"), net.requestedUnits)
    }

    @Test fun `5_2 waterfall picks first Loaded by priority`() {
        val low = FakeNetwork("Unity", 1, AdLoadResult.Loaded("Unity", 0.5))
        val high = FakeNetwork("AdMob", 10, AdLoadResult.NoFill)
        val mid = FakeNetwork("Meta", 5, AdLoadResult.Loaded("Meta", 1.1))

        val result = waterfall(listOf(low, high, mid), "inter")

        assertEquals(AdLoadResult.Loaded("Meta", 1.1), result)
        assertEquals(listOf("inter"), high.requestedUnits)
        assertEquals(listOf("inter"), mid.requestedUnits)
        assertTrue("networks after the Loaded one must not be called", low.requestedUnits.isEmpty())
    }

    @Test fun `5_2 waterfall returns NoFill when nothing loads`() {
        val a = FakeNetwork("A", 1, AdLoadResult.Failed(2, "net"))
        assertEquals(AdLoadResult.NoFill, waterfall(listOf(a), "x"))
        assertEquals(AdLoadResult.NoFill, waterfall(emptyList(), "x"))
    }
}
