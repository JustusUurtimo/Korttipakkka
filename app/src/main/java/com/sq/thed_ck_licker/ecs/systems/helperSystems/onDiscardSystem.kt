package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.get

fun onDiscardSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val targetsWithEffectStack =
        componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.effectEntities) {
            val effect = effectEntity get EffectComponent::class
            effect.onDeactivate(effectTarget.key, effectEntity)
        }
    }
}

fun discardSystem(
    ownerId: Int,
    cardId: Int
) {
    try {
        val cardEffects = cardId get EffectComponent::class
        cardEffects.onDeactivate(ownerId, cardId)
    } catch (_: IllegalStateException) {
        Log.i("discardSystem", "No deactivation effect found for card $cardId")
    }
    val discardDeck = ownerId get DiscardDeckComponent::class
    discardDeck.discardDeck.add(cardId)
}