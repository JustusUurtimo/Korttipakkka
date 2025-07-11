package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

data class GainMaxHealth(override val amount: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ($modifiedAmount) max health"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (this.amount * sourceMulti * targetMulti)
        return healthComp.increaseMaxHealth(amount)
    }
}
