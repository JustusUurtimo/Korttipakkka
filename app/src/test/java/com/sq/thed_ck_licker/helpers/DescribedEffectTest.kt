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

    @Test
    fun `Combine one empty and one non empty effects keep the non empty identity`() {
        var counter = 0
        val describedEffect1 = DescribedEffect(
            action = { counter += it },
            describe = { "moi $it" }
        )
        val describedEffect2 = DescribedEffect.EMPTY

        val combinedEffect = describedEffect1.combine(describedEffect2)
        assert(combinedEffect.action == describedEffect1.action) { "Combination of one empty and one non empty effects should have the same identity action as the one" }
        assert(combinedEffect.describe == describedEffect1.describe) { "Combination of one empty and one non empty effects should have the same identity describe as the one" }

        combinedEffect.action(10)
        assert(counter == 10) { "Counter should be 10, but was $counter, action not working" }
        val text = combinedEffect.describe(1)
        assert(text == "moi 1") { "Text should be 'moi 1', but was $text, describe not working" }


        val combinedEffect2 = describedEffect2.combine(describedEffect1)
        assert(combinedEffect2.action == describedEffect1.action) { "Combination of one empty and one non empty effects should have the same identity action as the one" }
        assert(combinedEffect2.describe == describedEffect1.describe) { "Combination of one empty and one non empty effects should have the same identity describe as the one" }

    }

}