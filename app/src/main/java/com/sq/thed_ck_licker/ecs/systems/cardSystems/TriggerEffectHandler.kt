package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get

object TriggerEffectHandler {

    fun handleTriggerEffect (context: EffectContext) {
        val trigger = context.trigger
        val source = context.source
        val target = context.target

        val trigEffComp = (source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[trigger] ?: return

        val (sourceMulti, targetMulti) = getMultipliers(context)

        for (effect in effects) {
            when (effect) {
                is Effect.GainScore -> {
                    val score = context.target get ScoreComponent::class
                    val amount = (effect.amount * sourceMulti * targetMulti).toInt()
                    val playerScore = score.addScore(amount)
                    val rewardTier = score.getRewardTier()
                    if ((playerScore.div(100) > rewardTier)) {
                        score.setRewardTier(rewardTier + 1)
                        GameEvents.tryEmit(GameEvent.RewardTierChanged)
                    }
                }
                is Effect.GainHealth ->{
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti)
                    healthComp.heal(amount)
                }

                is Effect.TakeDamage -> {
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti)
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
                            effect.amount
                        }

                        is Effect.TakeDamage -> {
                            effect.amount
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