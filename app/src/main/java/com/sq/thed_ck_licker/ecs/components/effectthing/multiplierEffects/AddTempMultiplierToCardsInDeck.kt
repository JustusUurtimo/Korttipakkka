package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.CoActivation
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.managers.locationmanagers.ForestManager.addTemporaryForestMultiplierTo
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.getRandomSubset

data class AddTempMultiplierToCardsInDeck(override val amount: Float, val size: Float) :
    MultiplierEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gift $modifiedAmount cards in your deck with temp multiplier of $size"
    }

    override fun execute(context: EffectContext): Float {
        val deck = DeckHelper.getEntityFullDeck(context.target)
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val amount = (this.amount * sourceMulti * targetMulti)
        val subDeck = deck.getRandomSubset(amount.toInt())
        subDeck.forEach { card ->
            val temp = addTemporaryForestMultiplierTo(card, multiplier = this.size)
            card add (card get TriggeredEffectsComponent::class).addEffects(
                Trigger.OnPlay,
                listOf(CoActivation(temp, Trigger.OnSpecial))
            )
        }
        return amount
    }
}
