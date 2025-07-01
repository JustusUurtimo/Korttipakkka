package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get

data class RemoveMultiplier(override val amount: Float) : Effect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Lose ($modifiedAmount) Multiplier"
    }

    override fun execute(context: EffectContext): Float {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.removeMultiplier(this.amount)
        return this.amount
    }
}
