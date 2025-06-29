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

   const val damageDealtKey = "damage dealt"

    fun handleTriggerEffect(context: EffectContext) {
        val trigEffComp = (context.source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[context.trigger]?.toMutableList()

        if (effects == null) return

        val (sourceMulti, targetMulti) = getMultipliers(context)
        for (effect in effects) {
            when (effect) { //Its getting quite big...
                is Effect.GainScore -> {
                    val score = context.target get ScoreComponent::class
                    var amount = (effect.amount * sourceMulti * targetMulti).toInt()
                    score.addScore(amount)
                }
                is Effect.GainScoreFromScoreComp -> {
                    gainScoreFromComp(context)
                }
                is Effect.GainHealth -> {
                    applyHealEffect(context, effect)
                }
                is Effect.TakeDamage -> {
                    applyDamageEffect(context, effect)
                }
                is Effect.GainMaxHealth -> {
                    applyGainMaxHealthEffect(context, effect)
                }
                is Effect.TakeDamagePercentage -> {
                    applyPercentageDamage(context, effect)
                }
                is Effect.TakeDamageOrGainMaxHP -> {
                    applyDamageOrBoostMaxHp(context, effect)
                }
                is Effect.HealOnUnderThreshold -> {
                    applyHealOnUnderThreshold(context, effect)
                }
                is Effect.TakeSelfDamage -> {
                    applySelfDamage(context, effect)
                }
                is Effect.AddMultiplier -> {
                    applyMultiplier(context, effect)
                }
                is Effect.RemoveMultiplier -> {
                    applyRemoveMultiplier(context, effect)
                }
                is TakeRisingDamage -> {
                    applyTakeRisingDamageEffect(context, effect)
                }
                is Effect.GainScalingScore -> {
                    applyGainScalingScore(context, effect)
                }
                is Effect.StoreDamageDealtAsSelfScore -> {
                    storeDamageDealtAsSelfScore(context)
                }
                is Effect.ResetSelfScore -> {
                    resetSelfScore(context, effect)
                }
                is Effect.AddSelfMultiplier -> {
                    applySelfMultiplier(context, effect)
                }
                is Effect.RemoveSelfMultiplier -> {
                    removeSelfMultiplier(context, effect)
                }
                is Effect.ResetTakeRisingDamage -> {
                    resetRisingDamageEffect(context)
                }
                is Effect.TakeRisingScore -> {
                    applyRisingScoreEffect(context, effect)
                }
                is Effect.AddScoreGainer -> {
                    addScoreGainer(context, effect)
                }
                is Effect.AddBeerGoggles -> {
                    applyLimitedSupplyAutoHeal(context, effect)
                }
                is Effect.AddTempMultiplier -> {
                    applyTemporaryMultiplier(context, effect)
                }
                is Effect.Shovel -> {
                    GameEvents.tryEmit(GameEvent.ShovelUsed)
                }
                is Effect.OpenMerchant -> {
                    openMerchant(effect, context)
                }
                is Effect.OnRepeatActivationGainScore -> {
                    onRepeatActionGainScore(effect, context)
                }
                is Effect.SelfAddEffectsToTrigger -> {
                    context.source add (context.source get TriggeredEffectsComponent::class).addEffects(
                        effect.trigger,
                        effect.effects
                    )
                }
                is Effect.CorruptCards -> {
                    applyCorruptCardsEffect(effect, context)
                }
            }
        }
    }

    private fun onRepeatActionGainScore(
        effect: Effect.OnRepeatActivationGainScore,
        context: EffectContext,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        if (effect.current < effect.amount) {
            effect.current++
        } else {
            effect.current = 0
            var sourceScore = context.source get ScoreComponent::class
            val targetScore = context.target get ScoreComponent::class
            var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
            targetScore.addScore(amount)
        }
    }

    private fun addScoreGainer(
        context: EffectContext, effect: Effect.AddScoreGainer
    ) {
        CardCreationHelperSystems2.addPassiveScoreGainerToEntity(
            context.target, effect.amount.toInt()
        )
    }

    private fun applyRisingScoreEffect(
        context: EffectContext,
        effect: Effect.TakeRisingScore,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val scoreComponent = (context.target get ScoreComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        scoreComponent.addScore(amount.toInt())
        effect.amount += effect.risingAmount
    }

    private fun resetSelfScore(
        context: EffectContext, effect: Effect.ResetSelfScore
    ) {
        val score = context.source get ScoreComponent::class
        score.setScore(effect.amount.toInt())
    }

    private fun applyGainScalingScore(
        context: EffectContext,
        effect: Effect.GainScalingScore,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val score = context.target get ScoreComponent::class
        var amount = (effect.amount * sourceMulti * targetMulti * effect.scalingFactor).toInt()
        score.addScore(amount)
    }

    private fun gainScoreFromComp(context: EffectContext) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        var sourceScore = context.source get ScoreComponent::class
        val targetScore = context.target get ScoreComponent::class
        var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
        targetScore.addScore(amount)
    }

    private fun storeDamageDealtAsSelfScore(context: EffectContext) {
        val damage = context.contextClues[damageDealtKey] as? Float ?: 0f
        val score = context.source get ScoreComponent::class
        score.addScore(abs((damage).toInt()))
        context.contextClues[damageDealtKey] = 0f
    }

    private fun removeSelfMultiplier(
        context: EffectContext, effect: Effect.RemoveSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    private fun applyRemoveMultiplier(
        context: EffectContext, effect: Effect.RemoveMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    private fun applyMultiplier(
        context: EffectContext, effect: Effect.AddMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    private fun applyTemporaryMultiplier(
        context: EffectContext, effect: Effect.AddTempMultiplier
    ) {
        CardCreationHelperSystems2.addTemporaryMultiplierTo(
            context.target, effect.amount
        )
    }

    private fun applyLimitedSupplyAutoHeal(
        context: EffectContext, effect: Effect.AddBeerGoggles
    ) {
        CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(
            context.target, effect.amount
        )
    }

    private fun resetRisingDamageEffect(context: EffectContext) {
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

    private fun applySelfMultiplier(
        context: EffectContext, effect: Effect.AddSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }


    private fun applyHealEffect(
        context: EffectContext,
        effect: Effect.GainHealth,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.heal(amount)
    }

    private fun applyHealOnUnderThreshold(
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

    private fun applyGainMaxHealthEffect(
        context: EffectContext,
        effect: Effect.GainMaxHealth,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.increaseMaxHealth(amount)
    }

    private fun applyDamageOrBoostMaxHp(
        context: EffectContext, effect: Effect.TakeDamageOrGainMaxHP
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val destiny = MyRandom.getRandomInt()
        if (destiny <= 1) {
            val healthComp = (context.target get HealthComponent::class)
            val amount = healthComp.getHealth() * (0.5 * sourceMulti * targetMulti).toFloat()
            healthComp.damage(amount)

            (context.source get HealthComponent::class).kill()
        } else {
            val healthComp = (context.target get HealthComponent::class)
            val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
            healthComp.increaseMaxHealth(amount)
        }
    }


    private fun applyTakeRisingDamageEffect(
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


    private fun applySelfDamage(
        context: EffectContext,
        effect: Effect.TakeSelfDamage,
    ) {
        val (sourceMulti, _) = getMultipliers(context)
        val healthComp = (context.source get HealthComponent::class)
        val amount = (effect.amount * sourceMulti).toFloat()
        healthComp.damage(amount)
    }

    private fun applyPercentageDamage(
        context: EffectContext,
        effect: Effect.TakeDamagePercentage,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = healthComp.getHealth() * (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.damage(amount)
    }

    private fun applyDamageEffect(
        context: EffectContext, effect: Effect.TakeDamage
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val healthComp = (context.target get HealthComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        healthComp.damage(amount)
    }


    private fun applyCorruptCardsEffect(
        effect: Effect.CorruptCards, context: EffectContext
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val efficiency = (effect.amount * sourceMulti * targetMulti).toInt()
        val target = (context.target get effect.targetDeck)
        val deck = when (target) {
            is DrawDeckComponent -> {
                var thing = target.getDrawCardDeck()
                if (thing.isEmpty()) {
                    thing = (context.target get DiscardDeckComponent::class).getDiscardDeck()
                }
                thing
            }

            is DiscardDeckComponent -> {
                var thing = target.getDiscardDeck()
                if (thing.isEmpty()) {
                    thing = (context.target get DrawDeckComponent::class).getDrawCardDeck()
                }
                thing
            }

            else -> {
                return
            }
        }
        repeat(efficiency) {
            val card = deck.removeAt(MyRandom.random.nextInt(deck.size))
            val trigEffComp = card get TriggeredEffectsComponent::class
            val corruptedTriggeredEffect = trigEffComp.shuffleTo()
            card add corruptedTriggeredEffect
            val tags = card get TagsComponent::class
            tags.addTag(TagsComponent.CardTag.CORRUPTED)
            deck.add(card)
        }
        return
    }


    private fun openMerchant(
        effect: Effect.OpenMerchant, context: EffectContext
    ) {
        MerchantEvents.tryEmit(
            MerchantEvent.MerchantShopOpened(
                effect.amount.toInt(), context.source
            )
        )
        effect.gameNavigator.navigateTo(Screen.MerchantShop.route)
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