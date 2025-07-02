package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.helpers.displayInfo

object None : MiscEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Do nothing"
    }

    override fun execute(context: EffectContext): Float {
        displayInfo("Nothing happens")
        return 1f
    }
}