package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

data class GainScalingScore(override val amount: Float, var scalingFactor: Float) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ${scalingFactor}x($modifiedAmount) points"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val score = context.target get ScoreComponent::class
        var amount = (this.amount * sourceMulti * targetMulti * this.scalingFactor).toInt()
        score.addScore(amount)
        return amount.toFloat()
    }
}
