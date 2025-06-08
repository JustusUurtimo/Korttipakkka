package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.managers.get


/* I decided to remove to unused one.
 * The difference seemed to be that the other one went through all the entities in effectStacks.
 */
fun discardSystem(
    ownerId: Int,
    cardId: Int
) {
    try {
        (cardId get EffectComponent::class).onDeactivate.action(ownerId)
    } catch (_: IllegalStateException) {
        Log.i("discardSystem", "No deactivation effect found for card $cardId")
    }
    val discardDeck = ownerId get DiscardDeckComponent::class
    discardDeck.addCard(cardId)
}