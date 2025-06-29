package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.managers.get
@Deprecated("As of 0.1.2.146, Use TurnStartSystem instead")
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
