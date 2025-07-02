package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

object GainScoreFromScoreComp : ScoreEffect() {
    override fun getDynamicAmountFromContext(context: EffectContext): Float? {
        val sourceScore = context.source get ScoreComponent::class
        return sourceScore.getScore().toFloat()
    }

    override fun describe(modifiedAmount: Float?): String {
        return "Gain ($modifiedAmount) points"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = TriggerEffectHandler.getMultipliers(context)
        var sourceScore = context.source get ScoreComponent::class
        val targetScore = context.target get ScoreComponent::class
        var amount = (sourceScore.getScore() * sourceMulti * targetMulti)
        targetScore.addScore(amount.toInt())
        return amount
    }
}