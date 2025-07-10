package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.getRandomElement

data class MultiplyMaxHp(override val amount: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Multiply max health of card by $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val deck = DeckHelper.getEntityFullDeck(context.target)
        val card = deck.getRandomElement()
        val healthComp = (card get HealthComponent::class)
        val amount = (this.amount * sourceMulti * targetMulti)
        return healthComp.setMaxHealth(healthComp.getMaxHealth() * amount)
    }
}
