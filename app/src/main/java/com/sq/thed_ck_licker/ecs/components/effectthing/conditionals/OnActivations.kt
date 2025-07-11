package com.sq.thed_ck_licker.ecs.components.effectthing.conditionals

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

data class OnActivations(
    var current: Int = 0,
    val threshold: Int,
    val effect: Effect
) : ConditionalEffect() {
    override fun execute(context: EffectContext): Float {
        this.current++
        if (this.current > threshold) {
            this.current -= threshold
            return effect.executeCall(context)
        }
        return 0f
    }
}
