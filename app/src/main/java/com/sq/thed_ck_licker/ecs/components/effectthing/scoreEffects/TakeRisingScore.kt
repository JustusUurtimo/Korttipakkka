package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext

data class TakeRisingScore(var gainScore: GainScore, var risingAmount: Float) : ScoreEffect() {
    constructor(amount: Float, risingAmount: Float) : this(GainScore(amount), risingAmount)

    override fun describe(modifiedAmount: Float?): String {
        return "Take ($modifiedAmount) rising score damage."
    }

    override fun execute(context: EffectContext): Float {
        val value = gainScore.executeCall(context)
        gainScore = GainScore(value + this.risingAmount)
        return value.toFloat()
    }
}
