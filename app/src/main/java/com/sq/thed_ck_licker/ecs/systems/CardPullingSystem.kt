package com.sq.thed_ck_licker.ecs.systems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class CardPullingSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val playerSystem: PlayerSystem
) {
    fun pullNewCard(
        latestCard: Int,
    ) {

        try {
            (latestCard get EffectComponent::class).onDeactivate.action.invoke(getPlayerID())
        } catch (_: IllegalStateException) {
            Log.i(
                "pullNewCardSystem",
                "No effect component found for pullNewCardSystem \n" +
                        "Yeah yeah, we get it, you are so cool there was no effect component"
            )
        }

        try {
            (latestCard get ActivationCounterComponent::class).deactivate()
        } catch (_: IllegalStateException) {
            Log.i(
                "pullNewCardSystem",
                "No actCounter component found for pullNewCardSystem \n" +
                        "Yeah yeah, we get it, you are so cool there was no actCounter component"
            )
        }

        val drawDeck2 = (getPlayerID() get DrawDeckComponent::class).getDrawCardDeck()
        if (latestCard > 0) {
            drawDeck2.add(latestCard)
        }

        putDiscardToDeckAndShuffle()
        playerSystem.setLatestCard(cardsSystem.pullRandomCardFromEntityDeck(getPlayerID()))
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
        //There is argument for shuffling only the discard deck but that is worry for another time.
        drawDeck.shuffle()
        //On shuffle effects here?
        discardDeck.clear()
    }
}