package com.sq.thed_ck_licker.ecs.components

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MultiplierComponentTest {


    @Test
    fun `Add multiplier to multi component`() {
        val multiComp = MultiplierComponent( 1f)
        multiComp.timesMultiplier(1.5f)
        assertEquals(multiComp.multiplier,  1.5f)
        multiComp.timesMultiplier(1.5f)
        assertEquals(multiComp.multiplier,  2.25f)
    }


    @Test
    fun `Remove multiplier to multi component`() {
        val hpComp = MultiplierComponent(2.25f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1.5f)
        hpComp.removeMultiplier(1.5f)
        assertEquals(hpComp.multiplier,  1f)
    }

}
