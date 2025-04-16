package com.sq.thed_ck_licker.ecs.systems

import android.R.attr.targetId
import android.util.Log
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.deactivate
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem

fun pullNewCardSystem(
    latestCard: MutableIntState,
    playerActiveMerchant: MutableIntState
): () -> Unit = {
    try {
        (latestCard.intValue get EffectComponent::class).onDeactivate.invoke(
            getPlayerID(),
            latestCard.intValue
        )
    } catch (_: IllegalStateException) {
        Log.i(
            "pullNewCardSystem",
            "No effect component found for pullNewCardSystem \nYeah yeah, we get it, you are so cool there was no effect component"
        )
    }

    try {
        (latestCard.intValue get ActivationCounterComponent::class).deactivate()
    } catch (_: IllegalStateException) {
        Log.i(
            "pullNewCardSystem",
            "No actCounter component found for pullNewCardSystem \nYeah yeah, we get it, you are so cool there was no actCounter component"
        )
    }
//    onDiscardSystem()

    playerActiveMerchant.intValue = -1


    reshuffleDecks()

    latestCard.intValue = cardsSystem.pullRandomCardFromEntityDeck(getPlayerID())

}

private fun reshuffleDecks(targetId: Int = getPlayerID()) {
    val drawDeck = targetId get DrawDeckComponent::class
    val discardDeck = targetId get DiscardDeckComponent::class
    if (drawDeck.drawCardDeck.isEmpty() || discardDeck.discardDeck.isNotEmpty()) {
        drawDeck.drawCardDeck += discardDeck.discardDeck.toMutableList()
        //There is argument for shuffling only the discard deck but that is worry for another time.
        drawDeck.drawCardDeck.shuffle()
        //On shuffle effects here?
        discardDeck.discardDeck.clear()
    }
}