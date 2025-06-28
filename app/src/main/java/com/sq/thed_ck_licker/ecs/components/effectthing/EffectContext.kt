package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager

data class EffectContext(
    val trigger: Trigger,
    val source: EntityId,
    val target: EntityId = EntityManager.getPlayerID(),
    val cm: ComponentManager = ComponentManager.componentManager,
    /**
     * Makes my eyes roll...
     * But this one can be used to carry information between effects...
     * So If some effects cares about damage dealt by other effects,
     * You can put thing like "damage dealt" and 69f here.
     *
     * This survives only this effect context.
     */
    val contextClues: MutableMap<String, Any> = mutableMapOf()
)
