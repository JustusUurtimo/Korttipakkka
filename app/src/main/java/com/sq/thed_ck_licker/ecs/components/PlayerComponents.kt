package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.EntityId

// TODO this needs to be mutableStateListOf but i cant get the type to work
data class DrawDeckComponent(val cards: MutableList<Int> = mutableListOf())

data class DiscardDeckComponent(val cards: MutableList<Int>)


data class EffectStackComponent(val effectEntities: MutableList<EntityId> = mutableListOf<EntityId>())

infix fun EffectStackComponent.addEntity(effectEntity: EntityId) =
    this.effectEntities.add(effectEntity)