package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.getRandomElement

data class GiveCardInDeckMultiplier(override val amount: Float) : Effect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gift card in your deck with multiplier of $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        val deck = DeckHelper.getEntityFullDeck(context.target)
        val card = deck.getRandomElement()
        val multiComp = (card get MultiplierComponent::class)
        multiComp.timesMultiplier(this.amount)
        return this.amount
    }
}
