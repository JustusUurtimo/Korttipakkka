package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers


/**
 * @param amount How much % to gain as score
 */
data class GainSelfHpAsScore(override val amount: Float) : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ${amount * 100}% health as score ($modifiedAmount)" //fuq... again the descriptions are not updating...
    }

    override fun execute(context: EffectContext): Float {
        val hp = context.source get HealthComponent::class
        val (sourceMulti, targetMulti) = getMultipliers(context)

        val targetScore = context.target get ScoreComponent::class
        var amount = ((hp.getHealth() * this.amount) * sourceMulti * targetMulti).toInt()
        targetScore.addScore(amount)
        return amount.toFloat()
    }
}
