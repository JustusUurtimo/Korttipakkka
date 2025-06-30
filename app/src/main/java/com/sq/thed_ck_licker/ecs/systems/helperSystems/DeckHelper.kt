package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.get

object DeckHelper {

    fun getEntityFullDeck(targetId: EntityId): MutableList<EntityId> {
        val deck = mutableListOf<EntityId>()
        deck.addAll((targetId get DrawDeckComponent::class).getDrawCardDeck())
        deck.addAll((targetId get DiscardDeckComponent::class).getDiscardDeck())
        return deck
    }
}