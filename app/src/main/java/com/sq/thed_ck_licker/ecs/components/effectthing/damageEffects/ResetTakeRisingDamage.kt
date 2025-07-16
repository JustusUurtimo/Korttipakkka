package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.get

data class ResetTakeRisingDamage(override var amount: Float, var risingAmount: Float) :
    Effect() { //Not happy about this one... at all
    override fun describe(modifiedAmount: Float?): String {
        return "Reset rising damage to $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        val trigComp = (context.source get TriggeredEffectsComponent::class)
        val takeRisingDamageEffects = trigComp.findEffect(TakeRisingDamage::class)
        takeRisingDamageEffects.forEach { it.reset() }
        return takeRisingDamageEffects.size.toFloat()
    }
}
