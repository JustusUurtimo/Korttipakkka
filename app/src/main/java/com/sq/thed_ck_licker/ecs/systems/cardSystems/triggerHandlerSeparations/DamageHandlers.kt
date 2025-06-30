package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect.TakeRisingDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.damageDealtKey
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers

object DamageHandlers {

    fun applySelfDamage(
        context: EffectContext,
        effect: Effect.TakeSelfDamage,
    ) {
        val (sourceMulti, _) = getMultipliers(context)
        val healthComp = (context.source get HealthComponent::class)
        val amount = (effect.amount * sourceMulti).toFloat()
        healthComp.damage(amount)
    }

    fun applyPercentageDamage(
        context: EffectContext,
        effect: Effect.TakeDamagePercentage,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = healthComp.getHealth() * (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.damage(amount)
    }

    fun applyPercentageSelfDamage(
        context: EffectContext,
        effect: Effect.TakeSelfPercentageDamage ,
    ) {
        val (sourceMulti, _) = getMultipliers(context)
        val healthComp = (context.source get HealthComponent::class)
        val amount = healthComp.getMaxHealth() * (effect.amount * sourceMulti).toFloat()
        healthComp.damage(amount)
    }

    fun applyDamageEffect(
        context: EffectContext, effect: Effect.TakeDamage
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.damage(amount)
    }


    fun applyTakeRisingDamageEffect(
        context: EffectContext,
        effect: TakeRisingDamage,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        val damageDone = healthComp.damage(amount)

        effect.amount += effect.risingAmount
        val cumulativeDamageDealt = (context.contextClues[damageDealtKey] as? Float) ?: 0f
        context.contextClues[damageDealtKey] = damageDone + cumulativeDamageDealt
    }

    fun resetRisingDamageEffect(context: EffectContext) {
        val sourceEffectsComponent = (context.source get TriggeredEffectsComponent::class)
        val sourceEffects = sourceEffectsComponent.effectsByTrigger
        val mutableMap: MutableMap<Trigger, List<Effect>> = mutableMapOf()
        for (entry in sourceEffects) {
            val filteredEffects = mutableListOf<Effect>()
            for (effect in entry.value) {
                if (effect !is TakeRisingDamage) {
                    filteredEffects.add(effect)
                } else {
                    filteredEffects.add(
                        TakeRisingDamage(
                            effect.amount, effect.risingAmount
                        )
                    )
                }
            }
            mutableMap.put(entry.key, filteredEffects.toList())
        }
        context.source add TriggeredEffectsComponent(
            effectsByTrigger = mutableMap.toMap()
        )
    }
}