package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get

data class AddMultiplier(override val amount: Float) : MultiplierEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ($modifiedAmount) Multiplier"
    }

    override fun execute(context: EffectContext): Float {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.timesMultiplier(this.amount)
        return this.amount
    }
}
