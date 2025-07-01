package com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.damageDealtKey
import kotlin.math.abs

@Deprecated("Please no...")
object StoreDamageDealtAsSelfScore : ScoreEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Store ($modifiedAmount) points to self."
    }

    override fun execute(context: EffectContext): Float {
        val damage = context.contextClues[damageDealtKey] as? Float ?: 0f
        val score = context.source get ScoreComponent::class
        score.addScore(abs((damage).toInt()))
        context.contextClues[damageDealtKey] = 0f
        return damage
    }
}