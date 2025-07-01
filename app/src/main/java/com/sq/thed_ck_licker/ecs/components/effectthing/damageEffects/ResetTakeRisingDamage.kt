package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get

data class ResetTakeRisingDamage(override var amount: Float, var risingAmount: Float) :
    Effect() { //Not happy about this one... at all
    override fun describe(modifiedAmount: Float?): String {
        return "Reset rising damage to $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        return resetRisingDamageEffect2(context)
    }

    fun resetRisingDamageEffect2(context: EffectContext): Float {
        val trigComp = (context.source get TriggeredEffectsComponent::class)
        val takeRisingDamageEffects = trigComp.findEffect(TakeRisingDamage::class)
        takeRisingDamageEffects.forEach { it.reset() }
        return takeRisingDamageEffects.size.toFloat()
    }

    fun resetRisingDamageEffect1(context: EffectContext) {
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
                            amount = effect.amount!!,
                            risingAmount = effect.risingAmount,
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
