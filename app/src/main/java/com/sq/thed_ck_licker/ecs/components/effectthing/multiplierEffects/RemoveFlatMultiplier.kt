package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get

data class RemoveFlatMultiplier(override val amount: Float) : MultiplierEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Lose ($modifiedAmount) Multiplier"
    }

    override fun execute(context: EffectContext): Float {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.decreaseMultiplier(this.amount)
        return this.amount
    }
}
