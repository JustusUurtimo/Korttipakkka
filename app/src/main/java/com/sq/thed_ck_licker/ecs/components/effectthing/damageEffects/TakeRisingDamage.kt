package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Resetable
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import kotlin.math.abs

class TakeRisingDamage(
    var takeDamage: TakeDamage,
    var risingAmount: Float,
    val originalAmount: TakeDamage = takeDamage,
    val originalRisingAmount: Float = risingAmount,
) : DamageEffect(),
    Resetable {
    constructor(
        amount: Float,
        risingAmount: Float
    ) : this(TakeDamage(amount), risingAmount)

    override fun describe(modifiedAmount: Float?): String {
        return "Take ($modifiedAmount) rising damage."
    }

    override fun reset() {
        takeDamage = originalAmount
        risingAmount = originalRisingAmount
    }

    override fun execute(context: EffectContext): Float {
        val damageDone = takeDamage.execute(context)

        takeDamage = TakeDamage(this.takeDamage.amount + this.risingAmount)

        val score = context.source get ScoreComponent::class
        score.addScore(abs((damageDone).toInt()))
        return damageDone
    }
}
