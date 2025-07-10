package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.get

object TriggerEffectHandler {

   const val damageDealtKey = "damage dealt"

    fun handleTriggerEffect(context: EffectContext) {
        val trigEffComp = (context.source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger[context.trigger]?.toMutableList()

        if (effects == null) return


        for (effect in effects) {
            effect.executeCall(context)
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
        val trigEffComp = ( context.source get TriggeredEffectsComponent::class)
        val effects = trigEffComp.effectsByTrigger
        val result = buildString {
            for ((trigger, effectsList) in effects) {
                append("$trigger:\n")
                for (effect in effectsList) {
                    append(effect.describeWithContext(context) ?: "(no description)")
                    append("\n")
                }
            }
        }
        return result.trimEnd()
    }

}