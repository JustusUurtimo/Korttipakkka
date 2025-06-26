package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.get

object TriggerEffectHandler {

     fun handleTriggerEffect(context: EffectContext) {
        val trigger = context.trigger
        val source = context.source
        val target = context.target

        val trigEffComp = (source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[trigger]

        if (effects == null) return

        for (effect in effects) {
            when (effect) {
                is Effect.GainScore -> {
                    val score = target get ScoreComponent::class
                    score.addScore(effect.amount)
                }

                is Effect.GainHealth ->{
                    val healthComp = (target get HealthComponent::class)
                    healthComp.heal(effect.amount)
                }

                is Effect.TakeDamage -> {
                    val healthComp = (target get HealthComponent::class)
                    healthComp.damage(effect.amount)
                }
            }
        }
    }

    fun describe(entity: EntityId): String {
        val trigEffComp = (entity get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger
        var result = ""

        for (entry in effects) {
            val trigger = entry.key
            result += "$trigger:\n"

            val effectsList = entry.value
            for (effect in effectsList) {
                result += "$effect\n"
            }
        }
        result = result.trimEnd()

        return result
    }
}