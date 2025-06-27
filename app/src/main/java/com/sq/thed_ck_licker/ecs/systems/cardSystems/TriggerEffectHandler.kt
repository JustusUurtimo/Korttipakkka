package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.MyRandom
import kotlin.math.min

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

                is Effect.GainMaxHealth -> {
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
                    healthComp.increaseMaxHealth(amount)
                }

                is Effect.TakeDamagePercentage -> {
                    val healthComp = (target get HealthComponent::class)
                    val amount =
                        healthComp.getHealth() * (effect.percentage * sourceMulti * targetMulti).toFloat()
                    healthComp.damage(amount)
                }

                is Effect.TakeDamageOrGainMaxHP -> {
                    val destiny = MyRandom.getRandomInt()
                    if (destiny <= 1) {
                        val healthComp = (target get HealthComponent::class)
                        val amount =
                            healthComp.getHealth() * (0.5 * sourceMulti * targetMulti).toFloat()
                        healthComp.damage(amount)

                        (source get HealthComponent::class).kill()
                    } else {
                        val healthComp = (target get HealthComponent::class)
                        val amount = (effect.maxHp * sourceMulti * targetMulti).toFloat()
                        healthComp.increaseMaxHealth(amount)
                    }
                }

                is Effect.HealOnUnderThreshold -> {
                    val targetHealthComp = (target get HealthComponent::class)
                    val sourceHealthComp = (source get HealthComponent::class)

                    if (targetHealthComp.getHealth() < targetHealthComp.getMaxHealth() * effect.threshold) {
                        var sourceHealth = sourceHealthComp.getHealth()
                        val healingPotential =
                            targetHealthComp.getMaxHealth() - targetHealthComp.getHealth()
                        val transferAmount = min(sourceHealth, healingPotential)
                        val finalTransferAmount =
                            (transferAmount * sourceMulti * targetMulti).toFloat()
                        targetHealthComp.heal(finalTransferAmount)
                        sourceHealthComp.damage(transferAmount)
                    }

                }

                is Effect.TakeSelfDamage -> {
                    val healthComp = (source get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti).toFloat()
                    healthComp.damage(amount)
                }

                is Effect.AddMultiplier -> {
                    val multiComp = (target get MultiplierComponent::class)
                    multiComp.timesMultiplier(effect.amount)
                }

                is Effect.RemoveMultiplier -> {
                    val multiComp = (target get MultiplierComponent::class)
                    multiComp.removeMultiplier(effect.amount)
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
                val amount =
                    when (effect) {
                        is Effect.GainScore -> {
                            (effect.amount * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.GainHealth -> {
                            (effect.amount * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.TakeDamage -> {
                            (effect.amount * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.GainMaxHealth -> {
                            (effect.amount * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.TakeDamagePercentage -> {
                            (effect.percentage * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.TakeDamageOrGainMaxHP -> {
                            (effect.maxHp * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.HealOnUnderThreshold -> {
                            (effect.limit * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.TakeSelfDamage -> {
                            (effect.amount * sourceMulti * targetMulti).toInt()
                        }

                        is Effect.AddMultiplier -> {
                            effect.amount.toFloat()
                        }

                        is Effect.RemoveMultiplier -> {
                            effect.amount.toFloat()
                        }
                    }
                result += effect.describe(amount) + "\n"
            }
        }
        result = result.trimEnd()

        return result
    }
}