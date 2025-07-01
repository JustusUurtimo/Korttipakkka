package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

@Deprecated("Use GainScoreFromScoreComp instead...")
data class GainScore(override val amount: Float) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String { //Not super sure about this one...
        return "Gain ($modifiedAmount) points"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = TriggerEffectHandler.getMultipliers(context)
        val score = context.target get ScoreComponent::class
        var amount = (this.amount * sourceMulti * targetMulti).toInt()
        score.addScore(amount)
        return amount.toFloat()
    }
}
