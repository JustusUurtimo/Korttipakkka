package com.sq.thed_ck_licker.ecs.components.effectthing.conditionals

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

data class OnNumberedActivations(
    var current: Int = 0,
    val thresholds: List<Int>,
    val effect: Effect
) : ConditionalEffect() {
    override fun execute(context: EffectContext): Float {
        this.current++
        if (thresholds.contains(current)){
            effect.executeCall(context)
        }
        return 0f
    }
}
