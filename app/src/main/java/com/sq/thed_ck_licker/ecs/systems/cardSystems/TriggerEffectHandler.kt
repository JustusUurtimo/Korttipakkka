package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get

object TriggerEffectHandler {

    fun handleTriggerEffect(context: EffectContext) {
        val trigger = context.trigger
        val source = context.source
        val target = context.target

        val trigEffComp = (source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[trigger]

        if (effects == null) return

        val (sourceMulti, targetMulti) = getMultipliers(context)

        for (effect in effects) {
            when (effect) {
                is Effect.GainScore -> {
                    val score = context.target get ScoreComponent::class
                    var amount = (effect.amount * sourceMulti * targetMulti).toInt()
                    score.addScore(amount)
                }
                is Effect.GainHealth ->{
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
                    healthComp.heal(amount)
                }

                is Effect.TakeDamage -> {
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
                    healthComp.damage(amount)
                }
            }
        }
    }

    /**
     *  @return Pair of multipliers for source and target
     */
    fun getMultipliers(context: EffectContext): Pair<Float, Float> {
        val sourceMulti = try {
            (context.source get MultiplierComponent::class).multiplier
        } catch (_: Exception) {
            1f
        }
        val targetMulti = try {
            (context.target get MultiplierComponent::class).multiplier
        } catch (_: Exception) {
            1f
        }
        return Pair(sourceMulti, targetMulti)
    }

    fun describe(context: EffectContext): String {
        val entity = context.source
        val trigEffComp = (entity get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger
        var result = ""

        val (sourceMulti, targetMulti) = getMultipliers(context)

        for (entry in effects) {
            val trigger = entry.key
            result += "$trigger:\n"

            val effectsList = entry.value
            for (effect in effectsList) {
                val initialAmount =
                    when (effect) {
                        is Effect.GainScore -> {
                            effect.amount.toFloat()
                        }

                        is Effect.GainHealth -> {
                            effect.amount.toFloat()
                        }

                        is Effect.TakeDamage -> {
                            effect.amount.toFloat()
                        }
                    }
                var amount = (initialAmount * sourceMulti * targetMulti).toInt()
                result += effect.describe(amount) + "\n"
            }
        }
        result = result.trimEnd()

        return result
    }
}