package com.sq.thed_ck_licker.ecs.components

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HealthComponentTest {
    @BeforeEach
    fun setUp() {
//        TODO("Not yet implemented")
    }

    @AfterEach
    fun tearDown() {
//        TODO("Not yet implemented")
    }

    @Test
    fun `Heal health component over max health`() {
        val hpComp = HealthComponent(100f)
         hpComp.heal(100f)
        assertEquals(hpComp.health.floatValue, 100f)
    }

    @Test
    fun `Use multiplier with heal`() {
        val hpComp = HealthComponent(100f, 1000f, 1.5f)
        hpComp.heal(100f)
        assertEquals(hpComp.health.floatValue, 250f)
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
    fun `Remove multiplier to health component`() {
        val hpComp = HealthComponent(100f, 1000f, 2.25f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1.5f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1f)
    }




}



