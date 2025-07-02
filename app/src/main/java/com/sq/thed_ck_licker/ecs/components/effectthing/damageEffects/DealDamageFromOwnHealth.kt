package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get

data class DealDamageFromOwnHealth(override val amount: Float) : DamageEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Deals (${modifiedAmount?.times(100)}) % self health as damage "
    }

    override fun execute(context: EffectContext): Float {
        val healthComp = (context.source get HealthComponent::class)
        val takeDamage = TakeDamage(healthComp.getMaxHealth() * this.amount)
        return takeDamage.executeCall(context)
    }
}