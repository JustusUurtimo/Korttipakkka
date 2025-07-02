package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get

data class RemoveSelfMultiplier(override val amount: Float) : MultiplierEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Lose ($modifiedAmount) self multiplier"
    }

    override fun execute(context: EffectContext): Float {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.removeMultiplier(this.amount)
        return this.amount
    }
}
