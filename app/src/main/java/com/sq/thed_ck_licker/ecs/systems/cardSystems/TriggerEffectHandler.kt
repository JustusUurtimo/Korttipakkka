package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeRisingDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.RemoveMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.GainScoreFromScoreComp
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.StoreDamageDealtAsSelfScore
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get

object TriggerEffectHandler {

   const val damageDealtKey = "damage dealt"

    fun handleTriggerEffect(context: EffectContext) {
        val trigEffComp = (context.source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[context.trigger]?.toMutableList()

        if (effects == null) return


        for (effect in effects) {
            effect.execute(context)
        }
    }

    /**
     *  @return Pair of multipliers for source and target
     *
     *  Usage:
     *  val (sourceMulti, targetMulti) = getMultipliers(context)
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
                        is AddMultiplier -> {
                            effect.amount
                        }

                        is RemoveMultiplier -> {
                            effect.amount
                        }

                        is GainScoreFromScoreComp -> {
                            (context.source get ScoreComponent::class).getScore().toFloat()
                        }

                        is StoreDamageDealtAsSelfScore -> {
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