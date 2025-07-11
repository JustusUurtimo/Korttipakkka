package com.sq.thed_ck_licker.ecs.components.effectthing.conditionals

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

data class SelfTarget(val effect: Effect) : ConditionalEffect() {
    override fun execute(context: EffectContext): Float {
        val contextHack = context.copy(target = context.source)
        effect.executeCall(contextHack)
        return 0f
    }
}
