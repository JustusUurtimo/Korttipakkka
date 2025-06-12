package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.hasComponent
import javax.inject.Inject


data class DrawDeckComponent @Inject constructor(
    private val drawCardDeck: MutableList<Int>,
) {
    fun getSize(): Int {
        return this.drawCardDeck.size
    }

    fun getDrawCardDeck(): MutableList<Int> {
        return this.drawCardDeck
    }

    fun removeCards(cards: List<Int>) {
        this.drawCardDeck.removeAll(cards)
    }

    fun removeCard(cardId: Int) {
        this.drawCardDeck.remove(cardId)
    }
}

data class EffectStackComponent(private val effectEntities: MutableList<EntityId> = mutableListOf<EntityId>()) {
    fun getEffectEntities(): List<EntityId> {
        return this.effectEntities
    }

    fun removeEntities(entities: List<EntityId>) {
        this.effectEntities.removeAll(entities)
    }

    infix fun addEntity(effectEntity: EntityId) = {
        val componentsOfEntity =
            ComponentManager.componentManager.getAllComponentsOfEntity(effectEntity)
        Log.i("EffectStackComponent", "componentsOfEntity: $componentsOfEntity")
        check((componentsOfEntity.hasComponent<EffectComponent>())) { "Entity has no effect component, thus it can't be effect.\n It only has: $componentsOfEntity" }
        this.effectEntities.add(effectEntity)
    }.invoke()
}




