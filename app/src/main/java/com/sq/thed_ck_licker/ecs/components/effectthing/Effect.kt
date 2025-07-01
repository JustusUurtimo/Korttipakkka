package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

abstract class Effect {
    /**
     * I am not super happy with this.
     * But the idea is that every effect basically always has some base value.
     * So by implementing this the handling can be simplified.
     *
     * I am open to idea of no value ones too.
     * But for now if those are the minority, thy can have some bogus value there...
     */
    open val amount:Float? = null

    abstract fun execute(context: EffectContext): Float

    open fun describe(): String? = null

    open fun describe(modifiedAmount: Float?): String? = null

    open fun describeWithContext(context: EffectContext): String? {
        val (sourceMulti, targetMulti) = TriggerEffectHandler.getMultipliers(context)
        val modifiedAmount = amount?.times(sourceMulti)?.times(targetMulti)
        return describe(modifiedAmount) ?: describe()
    }
}