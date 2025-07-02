package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

data class GainHealth(override val amount: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Heal ($modifiedAmount)"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = TriggerEffectHandler.getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (this.amount * sourceMulti * targetMulti).toFloat()
        return healthComp.heal(amount)
    }
}