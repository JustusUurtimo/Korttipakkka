package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.MyRandom

object DeckHelper {

    fun getEntityFullDeck(targetId: EntityId): MutableList<EntityId> {
        val deck = mutableListOf<EntityId>()
        deck.addAll((targetId get DrawDeckComponent::class).getDrawCardDeck())
        deck.addAll((targetId get DiscardDeckComponent::class).getDiscardDeck())
        return deck
    }
    fun getDeck(targetId: EntityId) = getEntityFullDeck(targetId)

    fun getSubDeck(targetId: EntityId, amount: Int): List<EntityId> {
        val deck = getEntityFullDeck(targetId).toMutableList()
        deck.shuffle(MyRandom.random)
        return deck.subList(0, amount)
    }
}