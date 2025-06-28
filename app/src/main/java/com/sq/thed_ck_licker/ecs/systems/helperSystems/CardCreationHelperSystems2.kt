package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity

object CardCreationHelperSystems2 {

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3): EntityId {
        val gainerEntity = generateEntity()
        val scoreComp = ScoreComponent(pointsPerCard)
        gainerEntity add scoreComp
        gainerEntity add TriggeredEffectsComponent(
            Trigger.OnTurnStart,
            Effect.GainScore(scoreComp.getScoreF())
        )
        gainerEntity add OwnerComponent(targetId)

        return gainerEntity
    }
}