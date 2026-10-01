package io.github.tuanmanh28.learn.lesson01

import io.github.tuanmanh28.learn.lesson01.exercises.*
import org.junit.Assert.*
import org.junit.Test

class Ex06DelegationTest {
    @Test fun `6_1 ClampedDouble`() {
        val s = BidSettings()
        assertEquals(0.0, s.floorUsd, 1e-9)
        s.floorUsd = 12.5
        assertEquals(12.5, s.floorUsd, 1e-9)
        s.floorUsd = -3.0
        assertEquals(0.0, s.floorUsd, 1e-9)
        s.floorUsd = 999.0
        assertEquals(50.0, s.floorUsd, 1e-9)
    }

    @Test fun `6_1 ClampedDouble clamps initial value`() {
        class Holder { var v: Double by ClampedDouble(100.0, 0.0..10.0) }
        assertEquals(10.0, Holder().v, 1e-9)
    }

    @Test fun `6_2 CountingNetwork`() {
        val inner = object : AdNetwork {
            override val name = "AdMob"
            override val priority = 7
            override fun load(unitId: String) = AdLoadResult.Loaded(name, 2.0)
        }
        val counting = CountingNetwork(inner)
        assertEquals("AdMob", counting.name)
        assertEquals(7, counting.priority)
        assertEquals(0, counting.loadCount)
        assertEquals(AdLoadResult.Loaded("AdMob", 2.0), counting.load("a"))
        counting.load("b")
        assertEquals(2, counting.loadCount)
    }
}
