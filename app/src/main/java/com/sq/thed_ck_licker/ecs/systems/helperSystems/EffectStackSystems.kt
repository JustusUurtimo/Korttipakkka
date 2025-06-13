package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.TargetComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.get

fun onTurnStartEffectStackSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val targetsWithEffectStack =
        componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.getEffectEntities()) {
            val effect = (effectEntity get EffectComponent::class).onTurnStart.action
            effect(effectTarget.key)
            Log.i("onTurnStartEffectStackSystem", "Activating $effect on ${effectTarget.key}")
        }
        Log.i("onTurnStartEffectStackSystem", "Effect stack size: ${effectStack.getEffectEntities().size}")
        Log.i("onTurnStartEffectStackSystem", "effectTarget.key: ${effectTarget.key}")
        Log.i("onTurnStartEffectStackSystem", "effectTarget.value: ${effectTarget.value}")
    }
}

fun onTurnStart(componentManager: ComponentManager = ComponentManager.componentManager) {
    val possibleEntities =
        componentManager.getEntitiesWithComponent(EffectComponent::class) ?: return
    for (entry in possibleEntities) {
        val entityId = entry.key
        var target = entityId
        try {
            target = (entityId get TargetComponent::class).target
        } catch (_: Exception) {
            Log.i("onTurnStart", "No target component found for entity $entityId")
        }
        val effectComp = entry.value as EffectComponent
        effectComp.onTurnStart.action(target)
    }
}
