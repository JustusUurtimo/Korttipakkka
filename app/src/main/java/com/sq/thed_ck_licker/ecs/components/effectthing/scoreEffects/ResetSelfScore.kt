package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get

data class ResetSelfScore(override val amount: Float = 0f) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Set Score to $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        val score = context.source get ScoreComponent::class
        score.setScore(this.amount.toInt())
        return this.amount
    }
}
