package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.damageDealtKey
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2
import kotlin.math.abs

object ScoreHandlers {

    fun gainScore(
        context: EffectContext,
        effect: Effect.GainScore
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val score = context.target get ScoreComponent::class
        var amount = (effect.amount * sourceMulti * targetMulti).toInt()
        score.addScore(amount)
    }

    fun onRepeatActionGainScore(
        effect: Effect.OnRepeatActivationGainScore,
        context: EffectContext,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        if (effect.current < effect.amount) {
            effect.current++
        } else {
            effect.current = 0
            var sourceScore = context.source get ScoreComponent::class
            val targetScore = context.target get ScoreComponent::class
            var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
            targetScore.addScore(amount)
        }
    }

    fun addScoreGainer(
        context: EffectContext, effect: Effect.AddScoreGainer
    ) {
        CardCreationHelperSystems2.addPassiveScoreGainerToEntity(
            context.target, effect.amount.toInt()
        )
    }

    fun applyRisingScoreEffect(
        context: EffectContext,
        effect: Effect.TakeRisingScore,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val scoreComponent = (context.target get ScoreComponent::class)
        val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
        scoreComponent.addScore(amount.toInt())
        effect.amount += effect.risingAmount
    }

    fun resetSelfScore(
        context: EffectContext, effect: Effect.ResetSelfScore
    ) {
        val score = context.source get ScoreComponent::class
        score.setScore(effect.amount.toInt())
    }

    fun applyGainScalingScore(
        context: EffectContext,
        effect: Effect.GainScalingScore,
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val score = context.target get ScoreComponent::class
        var amount = (effect.amount * sourceMulti * targetMulti * effect.scalingFactor).toInt()
        score.addScore(amount)
    }

    fun gainScoreFromComp(context: EffectContext) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        var sourceScore = context.source get ScoreComponent::class
        val targetScore = context.target get ScoreComponent::class
        var amount = (sourceScore.getScore() * sourceMulti * targetMulti).toInt()
        targetScore.addScore(amount)
    }

    fun storeDamageDealtAsSelfScore(context: EffectContext) {
        val damage = context.contextClues[damageDealtKey] as? Float ?: 0f
        val score = context.source get ScoreComponent::class
        score.addScore(abs((damage).toInt()))
        context.contextClues[damageDealtKey] = 0f
    }

    fun applyGainHpAsScore(context: EffectContext, effect: Effect.GainSelfHpAsScore) {
        val hp = context.source get HealthComponent::class
        val (sourceMulti, targetMulti) = getMultipliers(context)

        val targetScore = context.target get ScoreComponent::class
        var amount = ((hp.getHealth() * effect.amount) * sourceMulti * targetMulti).toInt()
        targetScore.addScore(amount)
    }

}