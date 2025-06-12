package com.sq.thed_ck_licker.ecs.systems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class CardPullingSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val playerSystem: PlayerSystem
) {
    fun pullNewCard(ownerId: Int = getPlayerID()) {
        val ownerInfo = (ownerId get LatestCardComponent::class)
        val latestCard = ownerInfo.getLatestCard()
        try {
            (latestCard get EffectComponent::class).onDeactivate.action(ownerId)
        } catch (_: IllegalStateException) {
            Log.i("pullNewCardSystem", "No effect component found for pullNewCardSystem")
        }

        try {
            (latestCard get ActivationCounterComponent::class).deactivate()
        } catch (_: IllegalStateException) {
            Log.i("pullNewCardSystem", "No actCounter component found for pullNewCardSystem ")
        }

        val drawDeck2 = (ownerId get DrawDeckComponent::class).getDrawCardDeck()
        if (latestCard > 0) {
            drawDeck2.add(latestCard)
        }

        putDiscardToDeckAndShuffle(ownerId)
        ownerInfo.setLatestCard(cardsSystem.pullRandomCardFromEntityDeck(ownerId))
    }

}

private fun putDiscardToDeckAndShuffle(
    targetId: Int,
    forceReshuffle: Boolean = false
) {
    val drawDeck = (targetId get DrawDeckComponent::class).getDrawCardDeck()
    val discardDeck = (targetId get DiscardDeckComponent::class).getDiscardDeck()
    if ((drawDeck.isEmpty() && discardDeck.isNotEmpty()) || forceReshuffle) {
        drawDeck += discardDeck.toMutableList()
        //There is argument for shuffling only the discard deck but that is worry for another time.
        drawDeck.shuffle()
        //On shuffle effects here?
        discardDeck.clear()
    }
}