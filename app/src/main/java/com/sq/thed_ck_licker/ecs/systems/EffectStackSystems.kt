package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.get

fun onTurnStartEffectStackSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val targetsWithEffectStack = componentManager.getEntitiesWithComponent(EffectStackComponent::class)
    if (targetsWithEffectStack == null) return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.effectEntities) {
            val effect = effectEntity get EffectComponent::class
            effect.onTurnStart(effectTarget.key)
            println("Activating ${effect} on ${effectTarget.key}")
        }
        println("effectTarget.key: ${effectTarget.key}")
        println("effectTarget.value: ${effectTarget.value}")
    }
}
