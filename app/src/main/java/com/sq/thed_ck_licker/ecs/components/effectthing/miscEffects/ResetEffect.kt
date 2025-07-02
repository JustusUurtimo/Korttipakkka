package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Resetable

data class ResetEffect(val effect: Resetable) : MiscEffect() {
    override fun execute(context: EffectContext): Float {
        effect.reset()
        return 1f
    }
}
