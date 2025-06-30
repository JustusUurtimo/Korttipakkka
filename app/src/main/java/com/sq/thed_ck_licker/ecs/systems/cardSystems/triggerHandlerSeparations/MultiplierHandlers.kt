package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.managers.locationmanagers.ForestManager.addTemporaryForestMultiplierTo
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.getRandomElement
import com.sq.thed_ck_licker.helpers.getRandomSubset

object MultiplierHandlers {
    fun removeSelfMultiplier(
        context: EffectContext, effect: Effect.RemoveSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    fun applyRemoveMultiplier(
        context: EffectContext, effect: Effect.RemoveMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    fun applyMultiplier(
        context: EffectContext, effect: Effect.AddMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    fun applyTemporaryMultiplier(
        context: EffectContext, effect: Effect.AddTempMultiplier
    ) {
        CardCreationHelperSystems2.addTemporaryMultiplierTo(
            context.target, effect.amount
        )
    }

    fun applySelfMultiplier(
        context: EffectContext, effect: Effect.AddSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    fun addFlatMultiplier(
        context: EffectContext,
        effect: Effect.AddFlatMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.increaseMultiplier(effect.amount)
    }

    fun removeFlatMultiplier(
        context: EffectContext,
        effect: Effect.RemoveFlatMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.decreaseMultiplier(effect.amount)
    }
    fun giftMultiplierToCard(
        context: EffectContext, effect: Effect.GiveCardInDeckMultiplier
    ) {
        val deck = DeckHelper.getEntityFullDeck(context.target)
        val card = deck.getRandomElement()
        val multiComp = (card get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    fun giftTempMultiToCards(
        context: EffectContext,
        effect: Effect.AddTempMultiplierToCardsInDeck
    ) {
        val deck = DeckHelper.getEntityFullDeck(context.target)
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val amount = (effect.amount * sourceMulti * targetMulti).toInt()
        val subDeck = deck.getRandomSubset(amount)
        subDeck.forEach {card ->
            val temp = addTemporaryForestMultiplierTo(card, multiplier = effect.size)
            card add (card get TriggeredEffectsComponent::class).addEffects(
                Trigger.OnPlay,
                listOf(Effect.CoActivation(temp, Trigger.OnSpecial))
            )
        }

    }

}