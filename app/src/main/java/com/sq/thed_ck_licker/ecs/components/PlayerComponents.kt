package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.hasComponent

// TODO this needs to be mutableStateListOf but i cant get the type to work
data class DrawDeckComponent(val cards: MutableList<Int> = mutableListOf())

data class DiscardDeckComponent(val cards: MutableList<Int>)


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


