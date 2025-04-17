package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.hasComponent


data class DrawDeckComponent(val drawCardDeck: MutableList<Int> = mutableListOf<Int>())

fun DrawDeckComponent.size() = this.drawCardDeck.size

data class DiscardDeckComponent(val discardDeck: MutableList<Int> = mutableListOf<Int>())

fun DiscardDeckComponent.size() = this.discardDeck.size


data class EffectStackComponent(val effectEntities: MutableList<EntityId> = mutableListOf<EntityId>())

infix fun EffectStackComponent.addEntity(effectEntity: EntityId) = {
    val componentsOfEntity =
        ComponentManager.componentManager.getAllComponentsOfEntity(effectEntity)
    Log.i("EffectStackComponent", "componentsOfEntity: $componentsOfEntity")
    check((componentsOfEntity.hasComponent<EffectComponent>())) { "Entity has no effect component, thus it can't be effect.\n It only has: $componentsOfEntity" }
    this.effectEntities.add(effectEntity)
}.invoke()


