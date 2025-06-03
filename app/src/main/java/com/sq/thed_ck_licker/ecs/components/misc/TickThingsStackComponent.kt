package com.sq.thed_ck_licker.ecs.components.misc

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.hasComponent


data class TickThingsStackComponent(private val tickingEntities: MutableList<EntityId> = mutableListOf<EntityId>()) {
    fun getEffectEntities(): List<EntityId> {
        return this.tickingEntities
    }

    fun removeEntities(entities: List<EntityId>) {
        this.tickingEntities.removeAll(entities)
    }

    infix fun addEntity(tickEntity: EntityId) = {
        val componentsOfEntity =
            ComponentManager.componentManager.getAllComponentsOfEntity(tickEntity)
        Log.i("EffectStackComponent", "componentsOfEntity: $componentsOfEntity")
        check((componentsOfEntity.hasComponent<EffectComponent>())) { "Entity has no tick component, thus it can't be tick.\n It only has: $componentsOfEntity" }
        this.tickingEntities.add(tickEntity)
    }.invoke()

    fun addEntity2(tickEntity: EntityId) {
        val componentsOfEntity =
            ComponentManager.componentManager.getAllComponentsOfEntity(tickEntity)
        Log.i("EffectStackComponent", "componentsOfEntity: $componentsOfEntity")
        check((componentsOfEntity.hasComponent<EffectComponent>())) { "Entity has no tick component, thus it can't be tick.\n It only has: $componentsOfEntity" }
        this.tickingEntities.add(tickEntity)
    }
}