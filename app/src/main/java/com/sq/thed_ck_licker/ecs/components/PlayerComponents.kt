package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.hasComponent
import javax.inject.Inject


data class DrawDeckComponent @Inject constructor(val drawCardDeck: MutableList<Int> )

fun DrawDeckComponent.size() = this.drawCardDeck.size

data class DiscardDeckComponent @Inject constructor (val discardDeck: MutableList<Int>)

fun DiscardDeckComponent.size() = this.discardDeck.size


data class EffectStackComponent(val effectEntities: MutableList<EntityId> = mutableListOf<EntityId>())

infix fun EffectStackComponent.addEntity(effectEntity: EntityId) = {
    val componentsOfEntity =
        ComponentManager.componentManager.getAllComponentsOfEntity(effectEntity)
    Log.i("EffectStackComponent", "componentsOfEntity: $componentsOfEntity")
    check((componentsOfEntity.hasComponent<EffectComponent>())) { "Entity has no effect component, thus it can't be effect.\n It only has: $componentsOfEntity" }
    this.effectEntities.add(effectEntity)
}.invoke()


