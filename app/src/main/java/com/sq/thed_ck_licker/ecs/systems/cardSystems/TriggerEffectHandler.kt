package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect.TakeRisingDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.DamageHandlers.applyDamageEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.DamageHandlers.applyPercentageDamage
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.DamageHandlers.applySelfDamage
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.DamageHandlers.applyTakeRisingDamageEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.DamageHandlers.resetRisingDamageEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyFullHealToAmountEntities
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyGainMaxHealthEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyHealEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyHealOnUnderThreshold
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyLimitedSupplyAutoHeal
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.HealthHandlers.applyMultiplyTheMaxHp
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MiscHandler.addEffectsToSourceTrigger
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MiscHandler.applyCorruptCardsEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MiscHandler.applyDamageOrBoostMaxHp
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MiscHandler.openMerchant
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.addFlatMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.applyMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.applyRemoveMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.applySelfMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.applyTemporaryMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.removeFlatMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.MultiplierHandlers.removeSelfMultiplier
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.addScoreGainer
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.applyGainScalingScore
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.applyRisingScoreEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.gainScore
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.gainScoreFromComp
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.onRepeatActionGainScore
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.resetSelfScore
import com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations.ScoreHandlers.storeDamageDealtAsSelfScore
import com.sq.thed_ck_licker.helpers.displayInfo

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
                    gainScore(context, effect)
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
                    addEffectsToSourceTrigger(context, effect)
                }
                is Effect.CorruptCards -> {
                    applyCorruptCardsEffect(effect, context)
                }
                is Effect.AddFlatMultiplier -> {
                    addFlatMultiplier(context, effect)
                }
                is Effect.RemoveFlatMultiplier -> {
                    removeFlatMultiplier(context, effect)
                }
                is Effect.None -> {
                    displayInfo("Nothing happens")
                }
                is Effect.HealEntitiesInDeckToFull -> {
                    applyFullHealToAmountEntities(context, effect)
                }
                is Effect.MultiplyMaxHp -> {
                    applyMultiplyTheMaxHp(context, effect)
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