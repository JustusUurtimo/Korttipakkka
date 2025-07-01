package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

object Shovel : MiscEffect() {
    override fun describe(): String {
        return "Open the fiery pit of doom!"
    }

    override fun execute(context: EffectContext): Float {
        TODO("Not yet implemented")
    }
}