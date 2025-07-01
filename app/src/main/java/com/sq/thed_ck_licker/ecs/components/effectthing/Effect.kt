package com.sq.thed_ck_licker.ecs.components.effectthing

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

    open fun describeWithContext(modifiedAmount: Float?): String? {
        return describe(modifiedAmount) ?: describe()
    }

    open fun describe(): String? = null

    open fun describe(modifiedAmount: Float?): String? = null
}