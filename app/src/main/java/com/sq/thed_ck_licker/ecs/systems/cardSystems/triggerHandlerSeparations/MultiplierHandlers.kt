package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2

object MultiplierHandlers {
    fun removeSelfMultiplier(
        context: EffectContext, effect: Effect.RemoveSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    fun applyRemoveMultiplier(
        context: EffectContext, effect: Effect.RemoveMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.removeMultiplier(effect.amount)
    }

    fun applyMultiplier(
        context: EffectContext, effect: Effect.AddMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    fun applyTemporaryMultiplier(
        context: EffectContext, effect: Effect.AddTempMultiplier
    ) {
        CardCreationHelperSystems2.addTemporaryMultiplierTo(
            context.target, effect.amount
        )
    }

    fun applySelfMultiplier(
        context: EffectContext, effect: Effect.AddSelfMultiplier
    ) {
        val multiComp = (context.source get MultiplierComponent::class)
        multiComp.timesMultiplier(effect.amount)
    }

    fun addFlatMultiplier(
        context: EffectContext,
        effect: Effect.AddFlatMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.increaseMultiplier(effect.amount)
    }

    fun removeFlatMultiplier(
        context: EffectContext,
        effect: Effect.RemoveFlatMultiplier
    ) {
        val multiComp = (context.target get MultiplierComponent::class)
        multiComp.decreaseMultiplier(effect.amount)
    }
}