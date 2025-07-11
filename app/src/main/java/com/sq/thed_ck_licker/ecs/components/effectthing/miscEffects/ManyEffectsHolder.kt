package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

data class ManyEffectsHolder(val list: List<Effect>) : MiscEffect() {
    override fun execute(context: EffectContext): Float {
        list.forEach {
            it.executeCall(context) }
        return 0f
    }
}
