package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

data class TakeDamage(override val amount: Float) : DamageEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Take damage ($modifiedAmount)"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (this.amount * sourceMulti * targetMulti)
        return healthComp.damage(amount)
    }

}