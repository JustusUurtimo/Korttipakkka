package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2

data class AddScoreGainer(override val amount: Float) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain score gainer"
    }

    override fun execute(context: EffectContext): Float {
        CardCreationHelperSystems2.addPassiveScoreGainerToEntity(
            context.target, this.amount.toInt()
        )
        return this.amount
    }
}
