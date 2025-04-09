package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.hasComponent

// TODO this needs to be mutableStateListOf but i cant get the type to work
// mutableStateListOf is part of Compose's runtime and requires the Compose context
// this way we can keep our components free from compose dependencies and make them usable in non-UI logic
data class DrawDeckComponent(val cardIds: List<Int> = listOf(0))

data class DiscardDeckComponent(val cards: List<Int> = listOf(0))


data class EffectStackComponent(val effectEntities: MutableList<EntityId> = mutableListOf<EntityId>())

infix fun EffectStackComponent.addEntity(effectEntity: EntityId) = {
    val componentsOfEntity =
        ComponentManager.componentManager.getAllComponentsOfEntity(effectEntity)
    println("componentsOfEntity: $componentsOfEntity")
    if (!(componentsOfEntity.hasComponent<EffectComponent>())) {
        throw IllegalStateException("Entity has no effect component, thus it can't be effect.\n It only has: $componentsOfEntity")
    }
    this.effectEntities.add(effectEntity)
}.invoke()


