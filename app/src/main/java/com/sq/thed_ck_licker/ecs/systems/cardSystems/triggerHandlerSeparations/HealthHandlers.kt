package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2
import kotlin.math.min

object HealthHandlers {

    fun applyHealEffect(
        context: EffectContext,
        effect: Effect.GainHealth,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.heal(amount)
    }

    fun applyHealOnUnderThreshold(
        context: EffectContext,
        effect: Effect.HealOnUnderThreshold,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val targetHealthComp = (context.target get HealthComponent::class)
        val sourceHealthComp = (context.source get HealthComponent::class)

        if (targetHealthComp.getHealth() < targetHealthComp.getMaxHealth() * effect.threshold) {
            var sourceHealth = sourceHealthComp.getHealth()
            val healingPotential = targetHealthComp.getMaxHealth() - targetHealthComp.getHealth()
            val transferAmount = min(sourceHealth, healingPotential)
            val finalTransferAmount = (transferAmount * sourceMulti * targetMulti).toFloat()
            targetHealthComp.heal(finalTransferAmount)
            sourceHealthComp.damage(transferAmount)
        }
    }

    fun applyGainMaxHealthEffect(
        context: EffectContext,
        effect: Effect.GainMaxHealth,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.increaseMaxHealth(amount)
    }

    fun applyLimitedSupplyAutoHeal(
        context: EffectContext, effect: Effect.AddBeerGoggles
    ) {
        CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(
            context.target, effect.amount
        )
    }

}