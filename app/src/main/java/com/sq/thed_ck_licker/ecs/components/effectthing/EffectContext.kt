package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager

data class EffectContext(
    val trigger: Trigger,
    val source: EntityId,
    val target: EntityId = EntityManager.getPlayerID(),
    val cm: ComponentManager = ComponentManager.componentManager
)
