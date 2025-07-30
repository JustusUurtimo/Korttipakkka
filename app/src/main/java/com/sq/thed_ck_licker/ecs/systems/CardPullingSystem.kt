package com.sq.thed_ck_licker.ecs.systems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.OnDeactivation
import com.sq.thed_ck_licker.ecs.components.effectthing.OnDraw
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.handleTriggerEffect
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class CardPullingSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val playerSystem: PlayerSystem
) {
    fun pullNewCard(
        latestCard: EntityId, ownerId: Int = getPlayerID()
    ) {

        try {
            val context = EffectContext(
                trigger = OnDeactivation, source = latestCard, target = ownerId
            )
            handleTriggerEffect(context)

        } catch (_: IllegalStateException) {
            Log.i(
                "pullNewCardSystem", "No Trigger Effect Component found for $latestCard on Deactivation"
            )
        }

        try {
            (latestCard get ActivationCounterComponent::class).deactivate()
        } catch (_: IllegalStateException) {
            Log.i(
                "pullNewCardSystem",
                "No actCounter component found for pullNewCardSystem for $latestCard\n" +
                        "Yeah yeah, we get it, you are so cool there was no actCounter component"
            )
        }

        val drawDeck2 = (ownerId get DrawDeckComponent::class).getDrawCardDeck()

        if (latestCard > 0) {
            drawDeck2.add(latestCard)
        }

        putDiscardToDeckAndShuffle()
        playerSystem.setLatestCard(cardsSystem.pullRandomCardFromEntityDeck(ownerId))

        try {
            val context = EffectContext(
                trigger = OnDraw, source = latestCard, target = ownerId
            )
            handleTriggerEffect(context)

        } catch (_: IllegalStateException) {
            Log.i(
                "pullNewCardSystem", "No Trigger Effect Component found for $latestCard on Draw" )
        }
    }

    private fun putDiscardToDeckAndShuffle(
        targetId: Int = getPlayerID(),
        forceReshuffle: Boolean = false
    ) {
        val drawDeck = (targetId get DrawDeckComponent::class).getDrawCardDeck()
        val discardDeck = (targetId get DiscardDeckComponent::class).getDiscardDeck()
        if ((drawDeck.isEmpty() && discardDeck.isNotEmpty()) || forceReshuffle) {
            drawDeck += discardDeck.toMutableList()
            drawDeck.shuffle()
            //On shuffle effects here?
            discardDeck.clear()
        }
    }
}