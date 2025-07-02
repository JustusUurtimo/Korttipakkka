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

//    abstract fun execute(context: EffectContext): Float

    open fun describe(): String? = null

    open fun describe(modifiedAmount: Float?): String? = null

    /**
     * Override this to provide a dynamic value (e.g. from a component),
     * instead of using the static `amount`.
     */
    open fun getDynamicAmountFromContext(context: EffectContext): Float? = null

    open fun describeWithContext(context: EffectContext): String? {
        val (sourceMulti, targetMulti) = TriggerEffectHandler.getMultipliers(context)

        val dynamicAmount = getDynamicAmountFromContext(context)
        val baseAmount = dynamicAmount ?: amount
        val modifiedAmount = baseAmount?.times(sourceMulti)?.times(targetMulti)

        return describe(modifiedAmount) ?: describe()
    }
    protected abstract fun execute(context: EffectContext): Float

    fun executeCall(context: EffectContext): Float {
        if (recursionLock) {
            println("Effect execution blocked due to recursion lock: ${this::class.simpleName}")
            return 0f
        }

        recursionLock = true
        try {
            return execute(context)
        } finally {
            recursionLock = false
        }
    }
    var recursionLock = false
}