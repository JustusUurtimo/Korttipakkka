package com.sq.thed_ck_licker.ecs.components

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class HealthComponentTest {

    @Test
    fun `Heal health component over max health`() {
        val hpComp = HealthComponent(100f)
         hpComp.heal(100f)
        assertEquals(hpComp.health, 100f)
    }

    @Test
    fun `Use multiplier with heal`() {
        val hpComp = HealthComponent(100f, 1000f, 1.5f)
        hpComp.heal(100f)
        assertEquals(hpComp.health, 250f)
    }


    @Test
    fun `Add multiplier to health component`() {
        val hpComp = HealthComponent(100f, 1000f, 1f)
        hpComp.timesMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1.5f)
        hpComp.timesMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  2.25f)
    }

    @Test
    fun `Are two health components equal`() {
        val hpComp1 = HealthComponent(100f, 1000f)
        val hpComp2 = HealthComponent(100f, 1000f)
        assertEquals(hpComp1, hpComp2){"The Health components should be equal"}

        hpComp1.heal(100f)
        assertNotEquals(hpComp1, hpComp2){"The Health components should differ for now"}


        hpComp2.heal(100f)
        assertEquals(hpComp1, hpComp2){"The Health components should be equal now"}
    }


    @Test
    fun `Remove multiplier to health component`() {
        val hpComp = HealthComponent(100f, 1000f, 2.25f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1.5f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1f)
    }
}



