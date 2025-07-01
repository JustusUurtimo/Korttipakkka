package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.helpers.MyRandom

data class TakeDamageOrGainMaxHP(override val amount: Float) : MiscEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gain ($modifiedAmount) max health or might explode"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val destiny = MyRandom.getRandomInt()
        if (destiny <= 1) {
            val healthComp = (context.target get HealthComponent::class)
            val amount = healthComp.getHealth() * (0.5 * sourceMulti * targetMulti).toFloat()
            healthComp.damage(amount)

            (context.source get HealthComponent::class).kill()
        } else {
            val healthComp = (context.target get HealthComponent::class)
            val amount = (this.amount * sourceMulti * targetMulti).toFloat()
            healthComp.increaseMaxHealth(amount)
        }
        return 0f
    }
}