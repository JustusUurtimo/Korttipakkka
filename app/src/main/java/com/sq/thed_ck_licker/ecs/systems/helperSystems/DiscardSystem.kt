package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.handleTriggerEffect

object DiscardSystem {
    fun handleDiscard(
        ownerId: Int,
        cardId: Int
    ) {
        try {
            (cardId get EffectComponent::class).onDeactivate.action(ownerId)
        } catch (_: IllegalStateException) {
            Log.i("discardSystem", "No deactivation effect found for card $cardId")
        }

        try {
            val context =
                EffectContext(trigger = Trigger.OnDiscard, source = cardId, target = ownerId)
            handleTriggerEffect(context)

        } catch (_: IllegalStateException) {
            Log.i(
                "discardSystem",
                "Entity $cardId has not been converted to new way as they have no TriggerEffect"
            )
        }

        val discardDeck = ownerId get DiscardDeckComponent::class
        discardDeck.addCard(cardId)
    }
}