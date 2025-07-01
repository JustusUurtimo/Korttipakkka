package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents

object Shovel : MiscEffect() {
    override fun describe(): String {
        return "Open the fiery pit of doom!"
    }

    override fun execute(context: EffectContext): Float {
        GameEvents.tryEmit(GameEvent.ShovelUsed)
        return 1f
    }
}