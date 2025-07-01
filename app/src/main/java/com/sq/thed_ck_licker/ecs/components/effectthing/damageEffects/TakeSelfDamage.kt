package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

/**
 * Differs from the Take damage as the source takes this damage instead of the target
 */
data class TakeSelfDamage(override val amount: Float) : Effect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Take self damage ($modifiedAmount)"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, _) = getMultipliers(context)
        val healthComp = (context.source get HealthComponent::class)
        val amount = (this.amount * sourceMulti).toFloat()
        return healthComp.damage(amount)
    }
}
