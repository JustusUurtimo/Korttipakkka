package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect.TakeRisingDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.navigation.Screen
import kotlin.math.abs
import kotlin.math.min

object TriggerEffectHandler {

    fun handleTriggerEffect(context: EffectContext) {
        val trigger = context.trigger
        val source = context.source
        val target = context.target

        val trigEffComp = (source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[trigger]?.toMutableList()

        if (effects == null) return

        val (sourceMulti, targetMulti) = getMultipliers(context)
        println("vnvnvm")
        for (effect in effects) {
            println("effect: $effect")
            when (effect) { //Its getting quite big...
                is Effect.GainScore -> {
                    val score = context.target get ScoreComponent::class
                    var amount = (effect.amount * sourceMulti * targetMulti).toInt()
                    score.addScore(amount)
                }
                is Effect.GainScoreFromScoreComp -> {
                    var sourceScore = context.source get ScoreComponent::class
                    val targetScore = context.target get ScoreComponent::class
                    var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
                    targetScore.addScore(amount)
                }
                is Effect.GainHealth -> {
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
                        healthComp.getHealth() * (effect.amount * sourceMulti * targetMulti).toFloat()
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
                        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
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
                is TakeRisingDamage -> {
                    val healthComp = (target get HealthComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
                    val damageDone = healthComp.damage(amount)

                    effect.amount += effect.risingAmount
                    val cumulativeDamageDealt = (context.contextClues["damage dealt"] as? Float) ?: 0f
                    context.contextClues["damage dealt"] = damageDone + cumulativeDamageDealt
                }
                is Effect.GainScalingScore -> {
                    val score = context.target get ScoreComponent::class
                    var amount =
                        (effect.amount * sourceMulti * targetMulti * effect.scalingFactor).toInt()
                    score.addScore(amount)
                }
                is Effect.StoreDamageDealtAsSelfScore -> {
                    val damage = context.contextClues["damage dealt"] as? Float ?: 0f
                    val score = context.source get ScoreComponent::class
                    score.addScore(abs((damage).toInt()))
                    context.contextClues["damage dealt"] = 0f
                }
                is Effect.ResetSelfScore -> {
                    val score = context.source get ScoreComponent::class
                    score.setScore(effect.amount.toInt())
                }
                is Effect.AddSelfMultiplier -> {
                    val multiComp = (source get MultiplierComponent::class)
                    multiComp.timesMultiplier(effect.amount)
                }
                is Effect.RemoveSelfMultiplier -> {
                    val multiComp = (source get MultiplierComponent::class)
                    multiComp.removeMultiplier(effect.amount)
                }
                is Effect.ResetTakeRisingDamage -> {
                    val sourceEffectsComponent =
                        (context.source get TriggeredEffectsComponent::class)
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
                                        effect.amount,
                                        effect.risingAmount
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
                is Effect.TakeRisingScore -> {
                    val scoreComponent = (target get ScoreComponent::class)
                    val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
                    scoreComponent.addScore(amount.toInt())
                    effect.amount += effect.risingAmount
                }
                is Effect.AddScoreGainer -> {
                    CardCreationHelperSystems2.addPassiveScoreGainerToEntity(
                        context.target,
                        effect.amount.toInt()
                    )
                }
                is Effect.AddBeerGoggles -> {
                    CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(
                        context.target,
                        effect.amount
                    )
                }
                is Effect.AddTempMultiplier -> {
                    CardCreationHelperSystems2.addTemporaryMultiplierTo(
                        context.target,
                        effect.amount
                    )
                }
                is Effect.Shovel -> {
                    GameEvents.tryEmit(GameEvent.ShovelUsed)
                }
                is Effect.OpenMerchant -> {
                    MerchantEvents.tryEmit(
                        MerchantEvent.MerchantShopOpened(
                            effect.amount.toInt(),
                            source
                        )
                    )
                    effect.gameNavigator.navigateTo(Screen.MerchantShop.route)
                }
                is Effect.OnRepeatActivationGainScore -> {
                    if (effect.current < effect.amount) {
                        effect.current++
                    }else{
                        effect.current = 0
                        var sourceScore = context.source get ScoreComponent::class
                        val targetScore = context.target get ScoreComponent::class
                        var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
                        targetScore.addScore(amount)
                    }
                }
                is Effect.SelfAddEffectsToTrigger -> {
                    source add (source get TriggeredEffectsComponent::class).addEffects(
                        effect.trigger,
                        effect.effects
                    )
                }

                is Effect.CorruptCards -> {
                    val efficiency = (effect.amount * sourceMulti * targetMulti).toInt()
                    val target = (context.target get effect.targetDeck)
                    val deck = when (target) {
                        is DrawDeckComponent -> {
                            var thing = target.getDrawCardDeck()
                            if(thing.isEmpty()){
                                thing  = (context.target get DiscardDeckComponent::class).getDiscardDeck()
                            }
                            thing
                        }

                        is DiscardDeckComponent -> {
                            var thing = target.getDiscardDeck()
                            if(thing.isEmpty()){
                                thing  = (context.target get DrawDeckComponent::class).getDrawCardDeck()
                            }
                            thing
                        }

                        else -> {
                            return
                        }
                    }
                    println("Moiii")
                    repeat(efficiency) {
                        println("Round: $it")
                        val card = deck.removeAt(MyRandom.random.nextInt(deck.size))
                        val trigEffComp = card get TriggeredEffectsComponent::class
                        println("trigEffComp: $trigEffComp")
                        val corruptedTriggeredEffect = trigEffComp.shuffleTo()
                        println("Corrupted: $corruptedTriggeredEffect")
                        card add corruptedTriggeredEffect
                        val tags = card get TagsComponent::class
                        tags.addTag(TagsComponent.CardTag.CORRUPTED)
                        deck.add(card)
                    }
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
                        is Effect.AddMultiplier -> {
                            effect.amount
                        }
                        is Effect.RemoveMultiplier -> {
                            effect.amount
                        }
                        is Effect.GainScoreFromScoreComp -> {
                            (context.source get ScoreComponent::class).getScore().toFloat()
                        }

                        is Effect.StoreDamageDealtAsSelfScore -> {
                            val asd = context.source get TriggeredEffectsComponent::class
                            val dddd = asd.findEffect(TakeRisingDamage::class)
                            dddd.first().amount
                        }
                        else -> {
                            (effect.amount?.times(sourceMulti)?.times(targetMulti))
                        }
                    }
                result += effect.describeWithContext(amount) + "\n"
            }
        }
        result = result.trimEnd()

        return result
    }
}