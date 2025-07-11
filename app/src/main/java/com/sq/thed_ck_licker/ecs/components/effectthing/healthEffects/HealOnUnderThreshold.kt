package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import kotlin.math.min

data class HealOnUnderThreshold(override val amount: Float, val threshold: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Heal ($modifiedAmount) if health is under $threshold"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val targetHealthComp = (context.target get HealthComponent::class)
        val sourceHealthComp = (context.source get HealthComponent::class)

        if (targetHealthComp.getHealth() < targetHealthComp.getMaxHealth() * this.threshold) {
            val sourceHealth = sourceHealthComp.getHealth()
            val healingPotential = targetHealthComp.getMaxHealth() - targetHealthComp.getHealth()
            val transferAmount = min(sourceHealth, healingPotential)
            val finalTransferAmount = (transferAmount * sourceMulti * targetMulti).toFloat()
            val done = targetHealthComp.heal(finalTransferAmount)
            sourceHealthComp.damage(done)
            return done
        }
        return 0f
    }
}