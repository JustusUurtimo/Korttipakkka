package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.get

fun onDiscardSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val targetsWithEffectStack =
        componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.getEffectEntities()) {
            val effect = effectEntity get EffectComponent::class
//            effect.onDeactivate(effectTarget.key, effectEntity)
        }
    }
}

fun discardSystem(
    ownerId: Int,
    cardId: Int
) {
    try {
            (cardId get EffectComponent::class).onDeactivate.action(ownerId)
//        val cardEffects =
//        cardEffects.onDeactivate(ownerId)
    } catch (_: IllegalStateException) {
        Log.i("discardSystem", "No deactivation effect found for card $cardId")
    }
    val discardDeck = ownerId get DiscardDeckComponent::class
    discardDeck.addCard(cardId)
}