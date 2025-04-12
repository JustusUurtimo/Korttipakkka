package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.removeAllCardsFromMerchantHand
import com.sq.thed_ck_licker.ecs.get

fun onDiscardSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val merchant = getPlayerID() get MerchantComponent::class
    merchant.removeAllCardsFromMerchantHand()
    val targetsWithEffectStack =
        componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.effectEntities) {
            val effect = effectEntity get EffectComponent::class
            effect.onDeactivate(effectTarget.key)
        }
    }
}