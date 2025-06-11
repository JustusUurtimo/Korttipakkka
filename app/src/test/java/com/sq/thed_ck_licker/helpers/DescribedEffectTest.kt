package com.sq.thed_ck_licker.helpers

import org.junit.jupiter.api.Test

class DescribedEffectTest {


    @Test
    fun getDescribe() {
    }

    @Test
    fun `Are they empty`() {
    }

    @Test
    fun `Combine two empty effects stay is identity empty effect`() {
        val describedEffect1 = DescribedEffect.EMPTY
        val describedEffect2 = DescribedEffect.EMPTY

        val combinedEffect = describedEffect1.combine(describedEffect2)
        assert(combinedEffect.action == DescribedEffect.EMPTY.action){"Combination of two empty effects should have the same identity empty action"}
        assert(combinedEffect.describe == DescribedEffect.EMPTY.describe){"Combination of two empty effects should have the same identity empty describe"}
    }

}