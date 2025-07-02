package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

data class OnRepeatActivationGainScore(
    override val amount: Float = 3f,
    var current: Int = 0
) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ($modifiedAmount) points if you manage to play this $amount ($current/$amount)"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        var result: Float
        if (this.current < this.amount) {
            this.current++
            result = -this.current.toFloat()
        } else {
            this.current = 0
            var sourceScore = context.source get ScoreComponent::class
            val targetScore = context.target get ScoreComponent::class
            var amount = (sourceScore.getScore() * sourceMulti * targetMulti)
            targetScore.addScore(amount.toInt())
            result = amount
        }
        return result
    }
}